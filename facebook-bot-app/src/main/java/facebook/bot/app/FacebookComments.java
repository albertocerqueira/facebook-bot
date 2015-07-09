package facebook.bot.app;

import facebook.bot.cassandra.Facebook;
import facebook4j.Comment;

public class FacebookComments {

	public static void postLikeCount(String postId, Comment comment) {
		String likeCount = comment.getLikeCount() + "";
		String userId = comment.getFrom().getId();
		String commentId = comment.getId();
		
		Facebook.insertCommentLikeCount(commentId, postId, userId, likeCount);
	}
}