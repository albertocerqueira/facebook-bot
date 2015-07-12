package facebook.bot.test;

import org.junit.Test;

import facebook.bot.app.Facebook;
import facebook4j.FacebookException;
import facebook4j.Group;
import facebook4j.Post;
import facebook4j.ResponseList;

public class SearchTest {

	@Test
	public void search_groups_by_key_word() throws FacebookException {
		ResponseList<Group> groups = Facebook.getFacebook().searchGroups("palavra chave");
		for (Group group : groups) {
			System.out.println("Group: " + group.getName());
		}
	}
	
	@Test
	public void search_posts_by_key_word() throws FacebookException {
		ResponseList<Post> posts = Facebook.getFacebook().searchPosts("palavra chave");
		for (Post post: posts) {
			System.out.println("Post message: " + post.getMessage());
		}
	}
}