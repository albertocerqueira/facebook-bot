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

public class FacebookPostTest {

	final static Logger logger = LoggerFactory.getLogger(FacebookPostTest.class);
	
	public static String type = "test-post";
	public Integer quantity = 2000;
	
	public FacebookPostTest() {}
	public FacebookPostTest(int quantity) {
		this.quantity = quantity;
	}
	
	@Test
	public void insert_facebook_post() {
		Random r = new Random();
		for (int x = 0; x < quantity; x++) {
			IdNameEntityImpl from = new IdNameEntityImpl();
			long userId = r.nextLong();
			from.setId((userId < 0 ? (userId * -1) : userId) + "");
			from.setName("Alberto Cerqueira");

			PostImpl post = new PostImpl();
			long postId = r.nextLong();
			post.setId((postId < 0 ? (postId * -1) : postId) + "");
			post.setFrom(from);
			post.setMessage("some text...");
			
			int s = r.nextInt(100000);

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
			
			logger.info("insert data in facebook_post [{}]", (x + 1));
		}
	}
	
	@Test
	public void remove_facebook_post() {
		List<ColumnOrSuperColumn> cscs = Cassandra.getPost(type);
		if (cscs != null && !cscs.isEmpty()) {
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
				
				Cassandra.removePost(post, type);
				
				logger.info("remove data in facebook_post [{}]", Cassandra.createString(csc.super_column.getName()));
			}
		}
	}
}