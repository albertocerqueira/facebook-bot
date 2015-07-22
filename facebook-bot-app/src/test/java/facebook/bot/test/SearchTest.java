package facebook.bot.test;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import facebook.bot.app.Facebook;
import facebook4j.FacebookException;
import facebook4j.Group;
import facebook4j.Post;
import facebook4j.ResponseList;

public class SearchTest {

	final static Logger logger = LoggerFactory.getLogger(SearchTest.class);
	
	@Test
	public void search_groups_by_key_word() throws FacebookException {
		ResponseList<Group> groups = Facebook.getFacebook().searchGroups("palavra chave");
		for (Group group : groups) {
			logger.info("Group: {}", group.getName());
		}
	}
	
	@Test
	public void search_posts_by_key_word() throws FacebookException {
		ResponseList<Post> posts = Facebook.getFacebook().searchPosts("palavra chave");
		for (Post post: posts) {
			logger.info("Post message: {}", post.getMessage());
		}
	}
}