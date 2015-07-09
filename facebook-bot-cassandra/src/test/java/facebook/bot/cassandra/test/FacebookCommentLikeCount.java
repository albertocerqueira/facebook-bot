package facebook.bot.cassandra.test;

import java.util.Random;

import org.junit.Test;

import facebook.bot.cassandra.Facebook;

public class FacebookCommentLikeCount {

	@Test
	public void insert_facebook_comment_like_count() {
		Random random = new Random();
		for (int x = 0; x < 1000; x++) {
			String commentId = random.nextLong() + "";
			String postId = random.nextLong() + "";
			String userId = random.nextLong() + "";
			String likeCount = random.nextInt() + "";

			Facebook.insertCommentLikeCount(commentId, postId, userId, likeCount);
			
			System.out.println("Inserindo em facebook_comment_likes_count registro [" + (x + 1) + "].");
		}
	}
}