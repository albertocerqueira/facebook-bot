package facebook.bot.cassandra.test;

import java.util.List;
import java.util.Random;

import org.apache.cassandra.thrift.ColumnOrSuperColumn;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import facebook.bot.app.Cassandra;
import facebook.bot.app.objects.CommentImpl;
import facebook.bot.app.objects.IdNameEntityImpl;
import facebook.bot.app.objects.PostImpl;
import facebook.bot.app.utils.PagableListImpl;
import facebook4j.Comment;
import facebook4j.IdNameEntity;
import facebook4j.PagableList;

public class FacebookPost {

	final static Logger logger = LoggerFactory.getLogger(FacebookPost.class);
	
	private String type = "test-post";
	
	//@Test
	public void insert_facebook_post() {
		Random random = new Random();
		for (int x = 0; x < 1000; x++) {
			IdNameEntityImpl from = new IdNameEntityImpl();
			from.setId(random.nextLong() + "");
			from.setName("Alberto Cerqueira");

			PostImpl post = new PostImpl();
			post.setId(random.nextLong() + "");
			post.setFrom(from);
			post.setMessage("some text...");
			
			int s = random.nextInt(10000);

			PagableList<Comment> comments = new PagableListImpl<Comment>();
			for (int y = 0; y < s; y++) {
				comments.add(new CommentImpl());
			}
			post.setComments(comments);
			
			PagableList<IdNameEntity> likes = new PagableListImpl<IdNameEntity>();
			for (int y = 0; y < s; y++) {
				likes.add(new IdNameEntityImpl());
			}
			post.setLikes(likes);
			
			Cassandra.insertPost(post, type);
			
			logger.info("Insert data in facebook_post [" + (x + 1) + "]");
		}
	}
	
	@Test//TODO: It does not work
	public void remove_facebook_post() {
		List<ColumnOrSuperColumn> cscs = Cassandra.getPostsPages(type);
		for (ColumnOrSuperColumn csc : cscs) {
			PostImpl post = new PostImpl();
			
			String[] superColumn = Cassandra.createString(csc.super_column.getName()).split("-");
			String postId = superColumn[0];
			String userId = superColumn[1];
			String userName = superColumn[2];
			
			IdNameEntityImpl from = new IdNameEntityImpl();
			from.setId(userId);
			from.setName(userName);

			post.setId(postId);
			post.setFrom(from);
			
			String[] column = Cassandra.createString(csc.super_column.columns.get(0).getName()).split("-");
			String commentsCount = column[0];
			String likeCount = column[1];
			
			Cassandra.removePost(post, type);
			
			logger.info("Remove data in facebook_post [" + Cassandra.createString(csc.super_column.getName()) + "]");
		}
	}
}