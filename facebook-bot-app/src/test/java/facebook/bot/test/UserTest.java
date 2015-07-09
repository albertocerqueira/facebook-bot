package facebook.bot.test;

import org.junit.Test;

import facebook.bot.app.AppFacebook;
import facebook4j.FacebookException;
import facebook4j.Post;
import facebook4j.ResponseList;
import facebook4j.User;

public class UserTest {

	@Test
	public void get_me() throws FacebookException {
		User user = AppFacebook.getFacebook().getMe();
		System.out.println("User: " + user.getName());
		ResponseList<Post> posts = AppFacebook.getFacebook().getFeed();
		for (Post post : posts) {
			System.out.println("Post: [" + post.getFrom() + "] - [" + post.getMessage() + "]");
		}
	}
}