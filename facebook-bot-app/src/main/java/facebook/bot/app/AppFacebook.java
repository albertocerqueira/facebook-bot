package facebook.bot.app;

import facebook4j.Facebook;
import facebook4j.FacebookFactory;
import facebook4j.conf.ConfigurationBuilder;

public class AppFacebook {

	private static Facebook facebook = null;
	
	public static Facebook getFacebook(){
		if(AppFacebook.facebook == null){
			ConfigurationBuilder cb = new ConfigurationBuilder();
			cb.setDebugEnabled(false);
			cb.setOAuthAppId("473090576198156");
			cb.setOAuthAppSecret("ab80e42df81c4aa24ec4ba237f8e666a");
			cb.setOAuthAccessToken("CAACEdEose0cBAHqZChNKBrLY1kdqf1048kfxhKaOUh76VEVokc9BqMVDtxG9xbvuk8otZB8sSC27RRX9DrDeF0jKv4OENjYoT5AZC3ZAlyA2petDLmBoBkOooU9ZASM0Tyyg1YQ1y03OIDSQFks4sxbudPO5gZBWfZBcK5kukKz6wZC1j2BcKfi1zH0DO4mv9HRHepTjjh3IZANNQIZCSTnvkSjzx8ngtpGp0ZD");
			cb.setOAuthPermissions("email, publish_actions, publish_stream, user_interests, friends_events, friends_location, manage_notifications");

			Facebook facebook = (new FacebookFactory(cb.build())).getInstance();
			AppFacebook.facebook = facebook;
		}
		return AppFacebook.facebook;
	}
}