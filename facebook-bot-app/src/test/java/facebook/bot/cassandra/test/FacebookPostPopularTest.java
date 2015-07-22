package facebook.bot.cassandra.test;

import java.io.UnsupportedEncodingException;
import java.util.Random;

import org.apache.cassandra.thrift.ColumnOrSuperColumn;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import facebook.bot.app.Cassandra;
import facebook.bot.app.objects.IdNameEntityImpl;
import facebook.bot.app.objects.PostImpl;

public class FacebookPostPopularTest {

	final static Logger logger = LoggerFactory.getLogger(FacebookPostPopularTest.class);
	
	private String type = "test-post";
	
	@Test
	public void insert_facebook_post_popular() throws UnsupportedEncodingException {
		Random r = new Random();
		for (int x = 0; x < 1000; x++) {
			IdNameEntityImpl from = new IdNameEntityImpl();
			long userId = r.nextLong();
			from.setId((userId < 0 ? (userId * -1) : userId) + "");
			from.setName("Alberto Cerqueira");
			
			PostImpl post = new PostImpl();
			long postId = r.nextLong();
			post.setId((postId < 0 ? (postId * -1) : postId) + "");
			post.setFrom(from);
			post.setMessage("some text...");
			
			Cassandra.insertPostPopular(post, type, x);
			
			logger.info("insert data in facebook_post_popular [{}].", (x + 1));
		}
	}
	
	@Test
	public void remove_facebook_post_popular() {
		for (int x = 2; x < 1000; x++) {
			ColumnOrSuperColumn csc = Cassandra.getPostPopular(type, x);
			
			if (csc != null) {
				PostImpl post = new PostImpl();
				
				String[] column = Cassandra.createString(csc.column.getName()).split("-");
				String postId = column[0];
				String userId = column[1];
				String userName = column[2];
				
				IdNameEntityImpl from = new IdNameEntityImpl();
				from.setId(userId);
				from.setName(userName);
				
				post.setId(postId);
				post.setFrom(from);
				
				Cassandra.removePostPopular(type, x);
				
				logger.info("remove data in facebook_post_popular [{}]", Cassandra.createString(csc.column.getName()));
			}
		}
	}
}