package facebook.bot.app;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import facebook4j.FacebookException;
import facebook4j.Post;
import facebook4j.ResponseList;

public class TGroup extends Thread {

	final static Logger logger = LoggerFactory.getLogger(TGroup.class);
	private static LinkedList<String> groupIds = new LinkedList<String>();
	private long time = 0;
	public static String type = "post-group";
	
	public TGroup(){}
	public TGroup(long time){
		this.time = time;
	}
	
	@Override
    public void run() {
        while (true) {
            try {
            	if (groupIds.size() > 0) {
            		String groupId = getGroupId();
            		checkFeed(groupId);
            	} else {
            		logger.info("there no are groups for verification");
            	}
                TGroup.sleep(time);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
	
	public static void addGroupId(String groupId){
		groupIds.add(groupId);
	}
	
	public static void removeGroupId(String groupId){
		groupIds.remove(groupId);
	}
	
	public static void addGroupIdAll(List<String> groupIdsList){
		groupIds.addAll(groupIdsList);
	}
	
	public static LinkedList<String> getGroupIds() {
		return groupIds;
	}
	
	public static void setPostIds(LinkedList<String> groupIds) {
		TGroup.groupIds = groupIds;
	}
	
	private String getGroupId() {
		int p = groupIds.size() - 1;
		String groupId = groupIds.get(p);
		groupIds.remove(p);
		return groupId;
	}
	
	public void checkFeed(String groupId) {
		try {
			ResponseList<Post> posts = Facebook.getFacebook().getGroupFeed(groupId);
			for (Post post : posts) {
				Cassandra.insertPost(post, type);
				/*
				String postId = post.getId();
				TUser.addUserId(post.getFrom().getId());// TODO: another user to mine information
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