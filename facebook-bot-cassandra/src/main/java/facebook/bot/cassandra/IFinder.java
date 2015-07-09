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

public interface IFinder {

	/**
	 * @throws InvalidRequestException chamada de metodos invalido.
	 * @throws UnavailableException servlet não está ativo.
	 * @throws TimedOutException tempo de bloqueo na acao excedido.
	 * @throws TException erro generico do Thrift. Pode ser de qualquer tipo.
	 * @throws UnsupportedEncodingException caracteres invalidos dentro da column.
	 * @throws TTransportException possivel erro de conexao no DB Cassandra, ou 
	 * seja o banco de dados estar com Status: Stop.
	 */
	
	
	
	public Integer countColumnsInSuperColumn(String columnFamily, String rowKey, String superColumn) throws InvalidRequestException, NotFoundException, UnavailableException, TimedOutException, TException, UnsupportedEncodingException, TTransportException;
	public Column findColumn(String columnFamily, String rowKey, String column) throws InvalidRequestException, NotFoundException, UnavailableException, TimedOutException, TException, UnsupportedEncodingException;
	public Column findColumn(String columnFamily, String rowKey, String superColumn, String column) throws InvalidRequestException, NotFoundException, UnavailableException, TimedOutException, TException, UnsupportedEncodingException;
	public List<Column> findColumns(String columnFamily, String rowKey, String...columns) throws InvalidRequestException, NotFoundException, UnavailableException, TimedOutException, TException, UnsupportedEncodingException;
	public SuperColumn findSuperColumn(String columnFamily, String rowKey, String superColumn) throws InvalidRequestException, NotFoundException, UnavailableException, TimedOutException, TException, UnsupportedEncodingException;
	public List<SuperColumn> findSuperColumns(String columnFamily, String rowKey, String...superColumns) throws InvalidRequestException, NotFoundException, UnavailableException, TimedOutException, TException, UnsupportedEncodingException;
	public List<ColumnOrSuperColumn> findColumnOrSuperColumn(String columnFamily, String rowKey) throws InvalidRequestException, NotFoundException, UnavailableException, TimedOutException, TException, UnsupportedEncodingException;
}