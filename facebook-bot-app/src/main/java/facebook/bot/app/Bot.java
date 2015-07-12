package facebook.bot.app;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.cassandra.thrift.ColumnOrSuperColumn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import facebook.bot.app.objects.IdNameEntityImpl;
import facebook.bot.app.objects.PostImpl;

public class Bot {

	final static Logger logger = LoggerFactory.getLogger(Cassandra.class);
	private static final Integer amount = 5;
	
	public static void main(String[] args) {
		Bot bot = new Bot();
		bot.insertPostPopular("test-post");
	}
	
	private void insertPostPopular(String type) {
		List<PostImpl> popularPosts = new ArrayList<PostImpl>();// TODO: check interface Post
		Set<Integer> rankingPosition = new TreeSet<Integer>();
		
		int smaller = 0, popular = 0, amount = Bot.amount;
		List<ColumnOrSuperColumn> cscs = Cassandra.getPost(type);
		for (ColumnOrSuperColumn csc : cscs) {
			PostImpl post = new PostImpl();
			
			String[] superColumn = Cassandra.createString(csc.super_column.getName()).split("-");
			String postId = superColumn[0];
			String userId = superColumn[1];
			String userName = superColumn[2];
			
			String[] column = Cassandra.createString(csc.super_column.columns.get(0).getName()).split("-");
			Integer commentsCount = Integer.parseInt(column[0]);
			Integer likeCount = Integer.parseInt(column[1]);
			int popularity = commentsCount + likeCount;// The popularity of posting is the sum of tanned with comments.
			
			String message = Cassandra.createString(csc.super_column.columns.get(0).getValue());
			
			IdNameEntityImpl from = new IdNameEntityImpl();
			from.setId(userId);
			from.setName(userName);

			post.setId(postId);
			post.setFrom(from);
			post.setPopularity(popularity);
			post.setMessage(message);
			
			boolean isPopular = false;
			if (smaller < popularity && popular < amount) {
				popularPosts.add(post);
				rankingPosition.add(popularity);
				popular++;
				isPopular = true;
			} else if (smaller < popularity) {
				int smallerCount = 0, positionSmaller = 0;
				for (int x = 0, y = popularPosts.size(); x < y; x++) {
					PostImpl p = popularPosts.get(x);
					
					if (p.getPopularity() == smaller) {
						smallerCount++;
						positionSmaller = x;
					}
				}
				
				if (smallerCount > 1) {// If more than a good low popularity, we will enter the post without removing any.
					amount = amount + 1;
				} else {
					popularPosts.remove(positionSmaller);
					rankingPosition.remove(smaller);
					popular--;
				}
				
				popularPosts.add(post);
				rankingPosition.add(popularity);
				popular++;
				isPopular = true;
				
				int m = 0;
				for (int x = 0, y = popularPosts.size(); x < y; x++) {
					PostImpl p = popularPosts.get(x);
					int pop = p.getPopularity();
					if (x == 0 || pop < m) {
						smaller = pop;
					}
				}
			}
			
			if ((smaller == 0 || smaller > popularity) && isPopular) {// lower among popular
				smaller = popularity;
			}
			
			Cassandra.removePost(post, type);// I can remove, we will no longer use. This data can be found again
		}
		
		int ranking = Bot.amount;
		for (Integer rp : rankingPosition) {
			for (int x = 0, y = popularPosts.size(); x < y; x++) {
				PostImpl post = popularPosts.get(x);
				int popularity = post.getPopularity();
				if (popularity == rp) {
					logger.info("posting the position " + ranking + "° " + post.toString());

					Cassandra.insertPostPopular(post, type, ranking);

					ranking--;
					popularPosts.remove(x);
					break;
				}
			}
		}
		logger.info(amount + " more popular posting stored in their positions");
	}
}