package facebook.bot.cassandra;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.cassandra.thrift.Column;
import org.apache.cassandra.thrift.InvalidRequestException;
import org.apache.cassandra.thrift.TimedOutException;
import org.apache.cassandra.thrift.UnavailableException;
import org.apache.thrift.TException;

public interface Idao {

	/**
	 * Metodo responsavel por inserir dado no DB Cassandra. 
	 * 
	 * @param columnFamily String : Nome da column family que sera persistido os dados.
	 * @param rowKey String : Row key que tera suas informacoes gravada.
	 * @param superColumn String : Nome da super column com que as columns seram persistidas.
	 * @param column Column : Column para persistencia no banco.
	 * 
	 * @throws InvalidRequestException chamada de metodos invalido.
	 * @throws UnavailableException servlet não está ativo.
	 * @throws TimedOutException tempo de bloqueo na acao excedido.
	 * @throws TException erro generico do Thrift. Pode ser de qualquer tipo.
	 * @throws UnsupportedEncodingException caracteres invalidos dentro da column.
	 */
	public void insertSuperColumn(String columnFamily, String rowKey, String superColumn, Column column) throws InvalidRequestException, UnavailableException, TimedOutException, TException, UnsupportedEncodingException;
	
	/**
	 * Metodo responsavel por inserir dados no DB Cassandra.
	 * 
	 * @param columnFamily String : Nome da column family que sera persistido os dados.
	 * @param rowKey String : Row key que tera suas informacoes gravada.
	 * @param superColumn String : Nome da super column com que as columns seram persistidas.
	 * @param columns List<Column> : Columns para persistencia no banco.
	 * 
	 * @throws InvalidRequestException chamada de metodos invalido.
	 * @throws UnavailableException servlet não está ativo.
	 * @throws TimedOutException tempo de bloqueo na acao excedido.
	 * @throws TException erro generico do Thrift. Pode ser de qualquer tipo.
	 * @throws UnsupportedEncodingException caracteres invalidos dentro da column.
	 */
	public void insertSuperColumns(String columnFamily, String rowKey, String superColumn, List<Column> columns) throws InvalidRequestException, UnavailableException, TimedOutException, TException, UnsupportedEncodingException;

	
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