package facebook.bot.cassandra.test;

import java.util.Random;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import facebook.bot.cassandra.Facebook;

public class FacebookCommentLikeCount {

	final static Logger logger = LoggerFactory.getLogger(FacebookCommentLikeCount.class);
	
	@Test
	public void insert_facebook_comment_like_count() {
		Random random = new Random();
		for (int x = 0; x < 1000; x++) {
			String commentId = random.nextLong() + "";
			String postId = random.nextLong() + "";
			String userId = random.nextLong() + "";
			String likeCount = random.nextInt() + "";

			Facebook.insertCommentLikeCount(commentId, postId, userId, likeCount);
			
			logger.info("Inserindo em facebook_comment_likes_count registro [" + (x + 1) + "].");
		}
	}
}