package facebook.bot.cassandra;

import org.apache.cassandra.thrift.ConsistencyLevel;

public final class Constants {
	
	private Constants(){}
	public static final String KEYSPACE = "social";
	public static final String COLUMN_FAMILY_FACEBOOK_POST = "facebook_post";// post - all
	public static final String COLUMN_FAMILY_FACEBOOK_POST_POPULAR = "facebook_post_popular";// post - tanned with
	public static final String KEY_MAP = "KEY";
	public static final String UTF8 = "UTF8";
	public static final ConsistencyLevel CL_1 = ConsistencyLevel.ONE;
	public static final ConsistencyLevel CL_2 = ConsistencyLevel.TWO;
	public static final String HOST = "localhost";
	public static final int PORT = 9160;
}