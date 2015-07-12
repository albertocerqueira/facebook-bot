package facebook.bot.app;

import facebook4j.FacebookFactory;
import facebook4j.conf.ConfigurationBuilder;

public class Facebook {

	private static facebook4j.Facebook facebook = null;
	
	public static facebook4j.Facebook getFacebook(){
		if(Facebook.facebook == null){
			ConfigurationBuilder cb = new ConfigurationBuilder();
			//cb.setDebugEnabled(false);
			cb.setOAuthAppId("473090576198156");
			cb.setOAuthAppSecret("ab80e42df81c4aa24ec4ba237f8e666a");
			cb.setOAuthAccessToken("CAACEdEose0cBAHFZBaiFvtPRaLZBy0eEoWhi0qEZAMKdXi8xLZCQuz5AAD9WQ1n1S05ShM2whGqxEZAtoCtpb5CZAIwL5m423acmy7loKOGZCXglUx9Ci4gBzX7scTK6HbLbifFTQWqp56WsOaIcIBk5RyiUPefTyLZCRsCVVaKgeKNw5baX6TjWipnAriv8ZBQFbHBFUZAICnnfbRf4wYLEL0u417ZBtEvbaUZD");
			cb.setOAuthPermissions("email, publish_actions, publish_stream, user_interests, friends_events, friends_location, manage_notifications");

			facebook4j.Facebook facebook = (new FacebookFactory(cb.build())).getInstance();
			Facebook.facebook = facebook;
		}
		return Facebook.facebook;
	}
}