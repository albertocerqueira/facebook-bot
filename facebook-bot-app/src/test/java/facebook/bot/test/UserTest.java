package facebook.bot.test;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import facebook.bot.app.Facebook;
import facebook4j.FacebookException;
import facebook4j.Post;
import facebook4j.ResponseList;
import facebook4j.User;

public class UserTest {

	final static Logger logger = LoggerFactory.getLogger(UserTest.class);
	
	@Test
	public void get_me() throws FacebookException {
		User user = Facebook.getFacebook().getMe();
		System.out.println("User: " + user.getName());
		ResponseList<Post> posts = Facebook.getFacebook().getFeed();
		for (Post post : posts) {
			logger.info("Post: [{}] - [{}]", post.getFrom(), post.getMessage());
		}
	}
}