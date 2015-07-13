package facebook.bot.cassandra;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.cassandra.thrift.Column;
import org.apache.cassandra.thrift.ColumnOrSuperColumn;
import org.apache.cassandra.thrift.InvalidRequestException;
import org.apache.cassandra.thrift.NotFoundException;
import org.apache.cassandra.thrift.SuperColumn;
import org.apache.cassandra.thrift.TimedOutException;
import org.apache.cassandra.thrift.UnavailableException;
import org.apache.thrift.TException;
import org.apache.thrift.transport.TTransportException;

public interface ICassandra {

	// TODO: add javadoc
	
	public void insertSuperColumn(String columnFamily, String rowKey, String superColumn, Column column);
	public void insertSuperColumns(String columnFamily, String rowKey, String superColumn, List<Column> columns);
	/**
	 * Need config time gc_grace_seconds on cassandra-cli for to remove marked for deletion
	 */
	public void removeSuperColumn(String columnFamily, String rowKey, String superColumn);

	public void insertColumn(String columnFamily, String rowKey, Column column);
	public void insertColumns(String columnFamily, String rowKey, List<Column> columns);
	/**
	 * Need config time gc_grace_seconds on cassandra-cli for to remove marked for deletion
	 */
	public void removeColumn(String columnFamily, String rowKey);
	
	public Integer countColumnsInSuperColumn(String columnFamily, String rowKey, String superColumn) throws InvalidRequestException, NotFoundException, UnavailableException, TimedOutException, TException, UnsupportedEncodingException, TTransportException;
	public Column findColumn(String columnFamily, String rowKey, String column) throws InvalidRequestException, NotFoundException, UnavailableException, TimedOutException, TException, UnsupportedEncodingException;
	public Column findColumn(String columnFamily, String rowKey, String superColumn, String column) throws InvalidRequestException, NotFoundException, UnavailableException, TimedOutException, TException, UnsupportedEncodingException;
	public List<Column> findColumns(String columnFamily, String rowKey, String...columns) throws InvalidRequestException, NotFoundException, UnavailableException, TimedOutException, TException, UnsupportedEncodingException;
	public SuperColumn findSuperColumn(String columnFamily, String rowKey, String superColumn) throws InvalidRequestException, NotFoundException, UnavailableException, TimedOutException, TException, UnsupportedEncodingException;
	public List<SuperColumn> findSuperColumns(String columnFamily, String rowKey, String...superColumns) throws InvalidRequestException, NotFoundException, UnavailableException, TimedOutException, TException, UnsupportedEncodingException;
	public List<ColumnOrSuperColumn> findColumnOrSuperColumn(String columnFamily, String rowKey) throws InvalidRequestException, NotFoundException, UnavailableException, TimedOutException, TException, UnsupportedEncodingException;
	
}