package facebook.bot.lib;

public class StringUtils {

	public static boolean isBlank(String value) {
		return (value == null || value.equals("") || value.equals("null") || value.trim().equals(""));
	}
}