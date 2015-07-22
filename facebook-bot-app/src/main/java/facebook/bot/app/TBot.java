package facebook.bot.app;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.cassandra.thrift.ColumnOrSuperColumn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import facebook.bot.app.objects.IdNameEntityImpl;
import facebook.bot.app.objects.PostImpl;

public class TBot extends Thread {

	final static Logger logger = LoggerFactory.getLogger(TBot.class);
	private static LinkedList<String> types = new LinkedList<String>();
	private long time = 0;
	private static Integer amount = 0;
	
	public TBot(){}
	public TBot(long time){
		this.time = time;
		TBot.amount = 50;// default
	}
	public TBot(int amount){
		this.time = Process.in30Minutes.getTime();// default
		TBot.amount = amount;
	}
	public TBot(long time, int amount){
		this.time = time;
		TBot.amount = amount;
	}
	
	@Override
    public void run() {
        while (true) {
            try {
            	if (types.size() > 0) {
            		String type = getType();
            		processMostPopularPosting(type);
            	} else {
            		logger.info("there no are types for process");
            	}
                TGroup.sleep(time);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
	
	public static void addType(String type){
		types.add(type);
	}
	
	public static void removeType(String type){
		types.remove(type);
	}
	
	public static void addTypeAll(List<String> typesList){
		types.addAll(typesList);
	}
	
	public static LinkedList<String> getTypes() {
		return types;
	}
	
	public static void setPostIds(LinkedList<String> types) {
		TBot.types = types;
	}
	
	private String getType() {
		int p = types.size() - 1;
		String type = types.get(p);
		types.remove(p);
		return type;
	}
	
	private void processMostPopularPosting(String type) {
		logger.info("starting scan misread the post {} type", type);
		List<PostImpl> popularPosts = new ArrayList<PostImpl>();// TODO: check interface Post
		Set<Integer> rankingPosition = new TreeSet<Integer>();// TreeSet() values are not repeated
		
		List<ColumnOrSuperColumn> cscs = Cassandra.getPost(type);
		if (cscs != null && !cscs.isEmpty()) {
			for (ColumnOrSuperColumn csc : cscs) {
				PostImpl post = new PostImpl();
				
				String[] superColumn = Cassandra.createString(csc.super_column.getName()).split("-");
				String postId = superColumn[0];
				String userId = superColumn[1];
				String userName = superColumn[2];
				
				String[] column = Cassandra.createString(csc.super_column.columns.get(0).getName()).split("-");
				Integer commentsCount = Integer.parseInt(column[0]);
				Integer likeCount = Integer.parseInt(column[1]);
				int popularity = commentsCount + likeCount;// the popularity of posting is the sum of tanned with comments.
				
				String sc = postId + "-" + userId + "-" + userName;
				logger.info("processing super column {}", sc);
				
				String message = Cassandra.createString(csc.super_column.columns.get(0).getValue());
				
				IdNameEntityImpl from = new IdNameEntityImpl();
				from.setId(userId);
				from.setName(userName);
				
				post.setId(postId);
				post.setFrom(from);
				post.setPopularity(popularity);
				post.setMessage(message);
				
				popularPosts.add(post);
				rankingPosition.add(popularity);
				
				Cassandra.removePost(post, type);// I can remove, we will no longer use. This data can be found again
			}
			
			int rf = rankingPosition.size() - TBot.amount.intValue();// posts limit determined by system strategy
			int position = 0;
			Object[] rps = (Object[]) rankingPosition.toArray();
			for (int i = rps.length - 1; i >= 0 && rf <= i && position != TBot.amount.intValue(); i--) {// (rf > i) posts limit determined by system strategy
				Integer rp = (Integer) rps[i];
				for (int x = 0, y = popularPosts.size(); x < y; x++) {
					PostImpl post = popularPosts.get(x);
					int popularity = post.getPopularity();
					
					if (popularity == rp) {
						int p = ++position;
						logger.info("posting the position {}° {}", p, post.toString());
						
						Cassandra.insertPostPopular(post, type, p);// the largest timestamp value is the popular post that we want
						
						popularPosts.remove(x);
						x = 0;
						y = popularPosts.size();
						//break; // some poster with the same popularities
					}
				}
			}
			
			logger.info("{} more popular posting stored in their positions", amount);
		}
	}
}