package facebook.bot.cassandra;

import static facebook.bot.cassandra.Constants.COLUMN_FAMILY_FACEBOOK_COMMENT_LIKE_COUNT;
import static facebook.bot.cassandra.Constants.UTF8;

import java.io.UnsupportedEncodingException;

import org.apache.cassandra.thrift.Column;
import org.apache.cassandra.thrift.InvalidRequestException;
import org.apache.cassandra.thrift.TimedOutException;
import org.apache.cassandra.thrift.UnavailableException;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Facebook {

	final static Logger logger = LoggerFactory.getLogger(Facebook.class);
	
	public static void insertCommentLikeCount(String commentId, String postId, String userId, String likeCount) {
		try {
			Idao dao = new Dao();
			Clock clock = new Clock(System.nanoTime());
			Column column = new Column();
			column.setName(userId.getBytes(UTF8));
			column.setValue(likeCount.getBytes(UTF8));
			column.setTimestamp(clock.timestamp);
			dao.insertSuperColumn(COLUMN_FAMILY_FACEBOOK_COMMENT_LIKE_COUNT, commentId, postId, column);
		} catch (InvalidRequestException e) {
			logger.info("unusual exception occurred");
			logger.error("[Info: access to invalid method] - [Error: " + e.getMessage() + "]", e);
		} catch (UnavailableException e) {
			logger.info("unusual exception occurred");
			logger.error("[Info: servlet container is not active] - [Error: " + e.getMessage() + "]", e);
		} catch (TimedOutException e) {
			logger.info("unusual exception occurred");
			logger.error("[Info: time out for insert] - [Error: " + e.getMessage() + "]", e);
		} catch (TException e) {
			logger.error("[Info: generic exception of thrift] - [Error: " + e.getMessage() + "]", e);
		} catch (UnsupportedEncodingException e) {
			logger.error("[Info: encoding invalid] - [Error: " + e.getMessage() + "]", e);
		}
	}
}