package facebook.bot.app;

import java.util.LinkedList;
import java.util.List;

import facebook4j.Comment;
import facebook4j.FacebookException;
import facebook4j.PagableList;
import facebook4j.Post;
import facebook4j.ResponseList;

public class TGroup extends Thread {

	private static LinkedList<String> groupIds = new LinkedList<String>();
	private long time = 0;
	
	public TGroup(){}
	public TGroup(long time){
		this.time = time;
	}
	
	@Override
    public void run() {
        while (true) {
            try {
            	String groupId = getGroupId();
            	
            	if (groupIds.size() > 0) {
            		checkFeed(groupId);
            	} else {
            		// TODO: Inserir log...
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
			ResponseList<Post> posts = AppFacebook.getFacebook().getGroupFeed(groupId);
			for (Post post : posts) {
				String postId = post.getId();
				TUser.addUserId(post.getFrom().getId());// Mais um usuario para minerar informacao
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