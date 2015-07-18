package facebook.bot.app;

import facebook4j.FacebookFactory;
import facebook4j.conf.ConfigurationBuilder;

public class Facebook {

	private static facebook4j.Facebook facebook = null;
	
	//site for config https://developers.facebook.com/tools/explorer/
	
	public static facebook4j.Facebook getFacebook(){
		if(Facebook.facebook == null){
			ConfigurationBuilder cb = new ConfigurationBuilder();
			//cb.setDebugEnabled(false);
			cb.setOAuthAppId("473090576198156");
			cb.setOAuthAppSecret("ab80e42df81c4aa24ec4ba237f8e666a");
			cb.setOAuthAccessToken("CAACEdEose0cBAEmOo31SqKR7MgWKewbLHpYu3DBZCjoPjJqLt9t607CUbJDJpDu7idsSC2SCVHR7j9saP79jYqtnZCFqStkXsRGG3QWNazF6LcZAfWawrkzkEWQmjBrCh06W7C46o2uaQcbNZBo3hF3xnweuGYsKV6i5btgGp0p3eMsqoQBr6V1WWvUZBCOXIUxS0ZAYePcGAFDdfZAhb5ggd3R6yjUW7IZD");
			cb.setOAuthPermissions("email, publish_actions, publish_stream, user_interests, friends_events, friends_location, manage_notifications");

			facebook4j.Facebook facebook = (new FacebookFactory(cb.build())).getInstance();
			Facebook.facebook = facebook;
		}
		return Facebook.facebook;
	}
}