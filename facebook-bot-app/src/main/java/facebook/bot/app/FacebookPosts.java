package facebook.bot.app;

import facebook.bot.cassandra.Facebook;
import facebook4j.IdNameEntity;
import facebook4j.Post;

public class FacebookPosts {

	public static void postPagePopular(String type, Integer rankingPosition, Post post) {
		String someText =  post.getMessage().substring(0, 150);// just 150 chars
		IdNameEntity user = post.getFrom();
		String postId = post.getId();
		String userId = user.getId();
		String userName = user.getName();
		
		Facebook.insertPostPagePopular(type, rankingPosition, postId, userId, userName, someText);
	}
}