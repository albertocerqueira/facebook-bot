package facebook.bot.cassandra.test;

import java.io.UnsupportedEncodingException;
import java.util.Random;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import facebook.bot.app.Cassandra;

public class FacebookPostPopular {

	final static Logger logger = LoggerFactory.getLogger(FacebookPostPopular.class);
	
	@Test
	public void insert_facebook_post_popular() throws UnsupportedEncodingException {
		Random random = new Random();
		for (int x = 0; x < 1000; x++) {
			String postId = random.nextLong() + "";
			String userId = random.nextLong() + "";
			String userName = "Alberto Cerqueira";
			String someText = "some text...";
			String type = "Tecnology";
			
			Cassandra.insertPostPopular(type, x, postId, userId, userName, someText);
			
			logger.info("Inserindo em facebook_post_popular registro [" + (x + 1) + "].");
		}
	}
}