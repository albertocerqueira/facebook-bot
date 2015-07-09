package facebook.bot.app;

import java.util.LinkedList;
import java.util.List;

import facebook4j.Comment;
import facebook4j.FacebookException;
import facebook4j.PagableList;
import facebook4j.Post;
import facebook4j.ResponseList;

public class TUser extends Thread {

	private static LinkedList<String> userIds = new LinkedList<String>();
	private long time = 0;
	
	public TUser(){}
	public TUser(long time){
		this.time = time;
	}
	
	@Override
    public void run() {
        while (true) {
            try {
            	String userId = getUserId();
            	
            	if (userIds.size() > 0) {
            		checkFeed(userId);
            	} else {
            		// TODO: Inserir log...
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
			ResponseList<Post> posts = AppFacebook.getFacebook().getFeed(userId);
			for (Post post : posts) {
				String postId = post.getId();
				PagableList<Comment> comments = post.getComments();
				for (Comment comment : comments) {
					TUser.addUserId(comment.getFrom().getId());// Mais um usuario para minerar informacao
					FacebookComments.postLikeCount(postId, comment);
				}
			}
		} catch (FacebookException e) {
			// TODO: Inserir log...
			e.printStackTrace();
		}
	}
}