package facebook.bot.app;

import static facebook.bot.cassandra.Constants.COLUMN_FAMILY_FACEBOOK_POST;
import static facebook.bot.cassandra.Constants.COLUMN_FAMILY_FACEBOOK_POST_POPULAR;
import static facebook.bot.cassandra.Constants.UTF8;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.cassandra.thrift.Column;
import org.apache.cassandra.thrift.ColumnOrSuperColumn;
import org.apache.cassandra.thrift.InvalidRequestException;
import org.apache.cassandra.thrift.TimedOutException;
import org.apache.cassandra.thrift.UnavailableException;
import org.apache.thrift.TException;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import facebook.bot.cassandra.CassandraImpl;
import facebook.bot.cassandra.Clock;
import facebook.bot.cassandra.ICassandra;
import facebook4j.IdNameEntity;
import facebook4j.Post;

public class Cassandra {

	final static Logger logger = LoggerFactory.getLogger(Cassandra.class);
	private static ICassandra cassandra = new CassandraImpl();
	
	// TODO: remove try/catch this class. 
	
	public static String createString(byte[] bytes) {
		try {
			return (new String(bytes, UTF8));
		} catch (UnsupportedEncodingException e) {
			logger.error("[Info: encoding invalid] - [Error: " + e.getMessage() + "]", e);
			return "erro [encoding invalid]";
		}
	}
	
	public static void insertPost(Post post, String type) {// pattern
		try {
			String postId = post.getId();
			IdNameEntity idNameEntity = post.getFrom();
			String userId = idNameEntity.getId();
			String userName = idNameEntity.getName();
			String commentsCount = post.getComments() == null ? "0" : post.getComments().size() + "";
			String likeCount = post.getLikes() == null ? "0" : post.getLikes().size() + "";
			
			String columnFamily = COLUMN_FAMILY_FACEBOOK_POST;
			String rowKey = type;
			String superColumn = postId + "-" + userId + "-" + userName;

			Clock clock = new Clock(System.nanoTime());
			Column column = new Column();
			String columnName = commentsCount + "-" + likeCount;
			column.setName(columnName.getBytes(UTF8));
			String columnValue = post.getMessage();
			column.setValue(columnValue.getBytes(UTF8));
			column.setTimestamp(clock.timestamp);
			
			cassandra.insertSuperColumn(columnFamily, rowKey, superColumn, column);
		} catch (UnsupportedEncodingException e) {
			logger.error("[Info: encoding invalid] - [Error: " + e.getMessage() + "]", e);
		}
	}
	
	public static void insertPostPopular(Post post, String type, Integer position) {
		try {
			String postId = post.getId();
			IdNameEntity idNameEntity = post.getFrom();
			String userId = idNameEntity.getId();
			String userName = idNameEntity.getName();
			
			String columnFamily = COLUMN_FAMILY_FACEBOOK_POST_POPULAR;
			String rowKey = (type + "-" + position);
			
			Clock clock = new Clock(System.nanoTime());
			Column column = new Column();
			String columnName = (postId + "-" + userId + "-" + userName);
			column.setName(columnName.getBytes(UTF8));
			String columnValue = post.getMessage();
			column.setValue(columnValue.getBytes(UTF8));
			column.setTimestamp(clock.timestamp);
			
			cassandra.insertColumn(columnFamily, rowKey, column);
		} catch (UnsupportedEncodingException e) {
			logger.error("[Info: encoding invalid] - [Error: " + e.getMessage() + "]", e);
		}
	}
	
	// TODO: this method needs to be tested
	// when deleting a column, you can no longer add anything in it
	public static void updatePostPopular(Post post, String type, Integer position) {
		removePostPopular(type, position);
		insertPostPopular(post, type, position);
	}
	
	public static void removePost(Post post, String type) {
		String postId = post.getId();
		IdNameEntity idNameEntity = post.getFrom();
		String userId = idNameEntity.getId();
		String userName = idNameEntity.getName();
		
		String columnFamily = COLUMN_FAMILY_FACEBOOK_POST;
		String rowKey = type;
		String superColumn = postId + "-" + userId + "-" + userName;
		
		cassandra.removeSuperColumn(columnFamily, rowKey, superColumn);
	}
	
	public static void removePostPopular(String type, Integer position) {
		String columnFamily = COLUMN_FAMILY_FACEBOOK_POST_POPULAR;
		String rowKey = (type + "-" + position);
		
		cassandra.removeColumn(columnFamily, rowKey);
	}

	public static List<ColumnOrSuperColumn> getPost(String type) {
		try {
			return cassandra.findColumnOrSuperColumn(COLUMN_FAMILY_FACEBOOK_POST, type);
		} catch (TTransportException e) {
			logger.info("unusual exception occurred");
			logger.error("[Info: db cassandra stopped] - [Error: " + e.getMessage() + "]", e);
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
		return null;
	}
	
	public static ColumnOrSuperColumn getPostPopular(String type, Integer rankingPosition) {
		try {
			String rowKey = (type + "-" + rankingPosition);
			List<ColumnOrSuperColumn> cscs = cassandra.findColumnOrSuperColumn(COLUMN_FAMILY_FACEBOOK_POST_POPULAR, rowKey);
			return cscs.get(0);
		} catch (TTransportException e) {
			logger.info("unusual exception occurred");
			logger.error("[Info: db cassandra stopped] - [Error: " + e.getMessage() + "]", e);
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
		return null;
	}
}