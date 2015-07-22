package facebook.bot.test;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import facebook.bot.app.Facebook;
import facebook4j.FacebookException;
import facebook4j.Group;
import facebook4j.Post;
import facebook4j.ResponseList;

public class GroupTest {

	final static Logger logger = LoggerFactory.getLogger(GroupTest.class);
	
	@Test
	public void user_get_groups() throws FacebookException {
		ResponseList<Group> groups = Facebook.getFacebook().getGroups();
		for (Group group : groups) {
			String groupId = group.getId();
			String name = group.getName();
			System.out.println("Group: " + name);
			ResponseList<Post> posts = Facebook.getFacebook().getGroupFeed(groupId);
			for (Post post : posts) {
				logger.info("Post: [{}] - [{}]", post.getFrom(), post.getMessage());
			}
		}
	}
}