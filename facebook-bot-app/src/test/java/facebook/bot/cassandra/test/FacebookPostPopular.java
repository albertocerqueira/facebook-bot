package facebook.bot.cassandra.test;

import java.io.UnsupportedEncodingException;
import java.util.Random;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import facebook.bot.app.Cassandra;
import facebook.bot.app.objects.IdNameEntityImpl;
import facebook.bot.app.objects.PostImpl;

public class FacebookPostPopular {

	final static Logger logger = LoggerFactory.getLogger(FacebookPostPopular.class);
	
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
			
			logger.info("Insert data in facebook_post_popular [" + (x + 1) + "].");
		}
	}
}