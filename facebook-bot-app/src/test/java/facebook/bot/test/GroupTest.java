package facebook.bot.test;

import org.junit.Test;

import facebook.bot.app.AppFacebook;
import facebook4j.FacebookException;
import facebook4j.Group;
import facebook4j.Post;
import facebook4j.ResponseList;

public class GroupTest {

	@Test
	public void user_get_groups() throws FacebookException {
		ResponseList<Group> groups = AppFacebook.getFacebook().getGroups();
		for (Group group : groups) {
			String groupId = group.getId();
			String name = group.getName();
			System.out.println("Group: " + name);
			ResponseList<Post> posts = AppFacebook.getFacebook().getGroupFeed(groupId);
			for (Post post : posts) {
				System.out.println("Post: [" + post.getFrom() + "] - [" + post.getMessage() + "]");
			}
		}
	}
}