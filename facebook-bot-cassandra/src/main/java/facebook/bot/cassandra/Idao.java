package facebook.bot.cassandra;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.cassandra.thrift.Column;
import org.apache.cassandra.thrift.InvalidRequestException;
import org.apache.cassandra.thrift.TimedOutException;
import org.apache.cassandra.thrift.UnavailableException;
import org.apache.thrift.TException;

public interface Idao {

	public void insertSuperColumn(String columnFamily, String rowKey, String superColumn, Column column) throws InvalidRequestException, UnavailableException, TimedOutException, TException, UnsupportedEncodingException;
	public void insertSuperColumns(String columnFamily, String rowKey, String superColumn, List<Column> columns) throws InvalidRequestException, UnavailableException, TimedOutException, TException, UnsupportedEncodingException;
	public void insertColumn(String columnFamily, String rowKey, Column column) throws InvalidRequestException, UnavailableException, TimedOutException, TException, UnsupportedEncodingException;
	public void insertColumns(String columnFamily, String rowKey, List<Column> columns) throws InvalidRequestException, UnavailableException, TimedOutException, TException, UnsupportedEncodingException;
	
	//TODO: Ajustar dados abaixo...
	/**
	 * TODO: Adicionar comentarios.
	 * @param columnFamily
	 * @param rowKey
	 * @param superColumnName
	 * @throws InvalidRequestException
	 * @throws UnavailableException
	 * @throws TimedOutException
	 * @throws TException
	 * @throws UnsupportedEncodingException
	 */
	public void removeSuperColumn(String columnFamily, String rowKey, String superColumnName) throws InvalidRequestException, UnavailableException, TimedOutException, TException, UnsupportedEncodingException;
	
	/**
	 * TODO: Adicionar comentarios.
	 * @param columnFamily
	 * @param rowKey
	 * @param superColumnName
	 * @param columnName
	 * @param count
	 * @throws InvalidRequestException
	 * @throws UnavailableException
	 * @throws TimedOutException
	 * @throws TException
	 * @throws UnsupportedEncodingException
	 */
	public void removeColumnSuperColumn(String columnFamily, String rowKey, String superColumnName, String columnName, Integer count) throws InvalidRequestException, UnavailableException, TimedOutException, TException, UnsupportedEncodingException;
}