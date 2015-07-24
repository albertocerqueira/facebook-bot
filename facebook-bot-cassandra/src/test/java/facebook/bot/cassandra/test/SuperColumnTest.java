package facebook.bot.cassandra.test;

import static facebook.bot.cassandra.Constants.UTF8;

import java.io.UnsupportedEncodingException;

import org.apache.cassandra.thrift.Column;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import facebook.bot.cassandra.CassandraImpl;
import facebook.bot.cassandra.Clock;
import facebook.bot.cassandra.ICassandra;

public class SuperColumnTest {

	final static Logger logger = LoggerFactory.getLogger(SuperColumnTest.class);
	private static ICassandra cassandra = new CassandraImpl();
	
	private String columnFamily = "cf_test_column_super";
	private Integer quantity = 50000;
	private Integer time = 10;
	
	@Test
	public void insert_super_columns() throws UnsupportedEncodingException, InterruptedException {
		for (int x = 0; x < quantity; x++) {
			int p = (x + 1);
			
			Clock clock = new Clock(System.nanoTime());
			Column column = new Column();
			column.setName(("column-" + p).getBytes(UTF8));
			column.setValue(("value-" + p).getBytes(UTF8));
			column.setTimestamp(clock.timestamp);
			
			cassandra.insertSuperColumn(columnFamily, ("row-key-" + p), ("superColumn-" + p), column);
			
			logger.info("insert data in {} [{}]", columnFamily, p);
			
			Thread.sleep(time);
		}
	}
	
	//@Test
	public void remove_super_columns() throws InterruptedException {
		for (int x = 0; x < quantity; x++) {
			int p = (x + 1);
			
			cassandra.removeSuperColumn(columnFamily, ("row-key-" + p), ("superColumn-" + p));
			
			logger.info("remove data in {} [{}]", columnFamily, p);
			
			Thread.sleep(time);
		}
	}
}