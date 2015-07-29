package facebook.bot.app;

import static facebook.bot.cassandra.Constants.COLUMN_FAMILY_FACEBOOK_POST;
import static facebook.bot.cassandra.Constants.COLUMN_FAMILY_FACEBOOK_POST_POPULAR;
import static facebook.bot.cassandra.Constants.UTF8;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.cassandra.thrift.Column;
import org.apache.cassandra.thrift.ColumnOrSuperColumn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import facebook.bot.cassandra.CassandraImpl;
import facebook.bot.cassandra.Clock;
import facebook.bot.cassandra.ICassandra;
import facebook.bot.lib.StringUtils;
import facebook4j.IdNameEntity;
import facebook4j.Post;

public class Cassandra {

	final static Logger logger = LoggerFactory.getLogger(Cassandra.class);
	private static ICassandra cassandra = new CassandraImpl();
	
	public static String createString(byte[] bytes) {
		try {
			return (new String(bytes, UTF8));
		} catch (UnsupportedEncodingException e) {
			logger.error("[Info: encoding invalid] - [Error: {}]", e.toString());
			return "erro [encoding invalid]";
		}
	}
	
	public static void insertPost(Post post, String type) {
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
			if (!StringUtils.isBlank(columnValue)) {
				column.setValue(columnValue.getBytes(UTF8));
			} else {
				column.setValue("".getBytes(UTF8));
			}
			column.setTimestamp(clock.timestamp);
			
			cassandra.insertSuperColumn(columnFamily, rowKey, superColumn, column);
		} catch (UnsupportedEncodingException e) {
			logger.error("[Info: encoding invalid] - [Error: {}]", e.toString());
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
			logger.error("[Info: encoding invalid] - [Error: {}]", e.toString());
		}
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

	public static Integer countPost(String type) {
		return cassandra.countColumnOrSuperColumn(COLUMN_FAMILY_FACEBOOK_POST, type);
	}
	
	public static List<ColumnOrSuperColumn> getPost(String type) {
		return cassandra.findColumnOrSuperColumn(COLUMN_FAMILY_FACEBOOK_POST, type);
	}
	
	public static ColumnOrSuperColumn getPostPopular(String type, Integer rankingPosition) {
		String rowKey = (type + "-" + rankingPosition);
		List<ColumnOrSuperColumn> cscs = cassandra.findColumnOrSuperColumn(COLUMN_FAMILY_FACEBOOK_POST_POPULAR, rowKey);
		if (cscs != null && !cscs.isEmpty()) {
			return cscs.get(0);// we are using "index_interval = 1", so we can get the first data
		} else {
			return null;
		}
	}
}