package facebook.bot.cassandra.test;

import java.util.Random;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import facebook.bot.cassandra.Facebook;

public class FacebookPostPagePopular {

	final static Logger logger = LoggerFactory.getLogger(FacebookCommentLikeCount.class);
	
	@Test
	public void insert_facebook_comment_like_count() {
		Random random = new Random();
		for (int x = 0; x < 1000; x++) {
			String postId = random.nextLong() + "";
			String userId = random.nextLong() + "";
			String userName = "Alberto Cerqueira";
			String someText = "some text...";
			String type = "Tecnology";
			
			Facebook.insertPostPagePopular(type, x, postId, userId, userName, someText);
			
			logger.info("Inserindo em facebook_post_page_popular registro [" + (x + 1) + "].");
		}
	}
}