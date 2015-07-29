package facebook.bot.app;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import facebook4j.FacebookException;
import facebook4j.Post;
import facebook4j.ResponseList;

public class TUser extends Thread {

	final static Logger logger = LoggerFactory.getLogger(TUser.class);
	private static LinkedList<String> userIds = new LinkedList<String>();
	private long time = 0;
	public static String type = "post-user";
	
	public TUser(){}
	public TUser(long time){
		this.time = time;
	}
	
	@Override
    public void run() {
        while (true) {
            try {
            	if (userIds.size() > 0) {
            		String userId = getUserId();
            		checkFeed(userId);
            	} else {
            		logger.info("there no are users for verification");
            	}
                TUser.sleep(time);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
	
	public static void addUserId(String userId){
		userIds.add(userId);
	}
	
	public static void removeUserId(String userId){
		userIds.remove(userId);
	}
	
	public static void addUserIdAll(List<String> userIdsList){
		userIds.addAll(userIdsList);
	}
	
	public static LinkedList<String> getUserIds() {
		return userIds;
	}
	
	public static void setPostIds(LinkedList<String> userIds) {
		TUser.userIds = userIds;
	}
	
	private String getUserId() {
		int p = userIds.size() - 1;
		String userId = userIds.get(p);
		userIds.remove(p);
		return userId;
	}
	
	public void checkFeed(String userId) {
		try {
			ResponseList<Post> posts = Facebook.getFacebook().getFeed(userId);
			for (Post post : posts) {
				Cassandra.insertPost(post, type);
				/*
				String postId = post.getId();
				PagableList<Comment> comments = post.getComments();
				for (Comment comment : comments) {
					TUser.addUserId(comment.getFrom().getId());// TODO: another user to mine information
					//FacebookComments.postLikeCount(postId, comment);
				}
				*/
			}
		} catch (FacebookException e) {
			logger.error("[Error Code: " + e.getErrorCode() + "] - [Error: " + e.getErrorMessage() + "]", e);
		}
	}
}