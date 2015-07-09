package facebook.bot.cassandra;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.cassandra.thrift.InvalidRequestException;
import org.apache.cassandra.thrift.KeySlice;
import org.apache.cassandra.thrift.NotFoundException;
import org.apache.cassandra.thrift.SuperColumn;
import org.apache.cassandra.thrift.TimedOutException;
import org.apache.cassandra.thrift.UnavailableException;
import org.apache.thrift.TException;
import org.apache.thrift.transport.TTransportException;

public interface IFinder {

	/**
	 * Metodo por buscar a quantidade de columns contida na super column
	 * solicitada.
	 * 
	 * @param columnFamily String : Nome da Column Family que contem a
	 * informacao. 
	 * @param rowKey String : rowKey da informacao para contagem do dados.
	 * @param superColumn Stirng : Super Column que tera suas column contadas.
	 * 
	 * @return Integer da quantidade de columns contida na Super Column
	 * 
	 * @throws InvalidRequestException chamada de metodos invalido.
	 * @throws UnavailableException servlet não está ativo.
	 * @throws TimedOutException tempo de bloqueo na acao excedido.
	 * @throws TException erro generico do Thrift. Pode ser de qualquer tipo.
	 * @throws UnsupportedEncodingException caracteres invalidos dentro da column.
	 * @throws TTransportException possivel erro de conexao no DB Cassandra, ou 
	 * seja o banco de dados estar com Status: Stop.
	 */
	public Integer findCountDataSuperColumnByRowKey(String columnFamily, String rowKey, String superColumn) throws InvalidRequestException, NotFoundException, UnavailableException, TimedOutException, TException, UnsupportedEncodingException, TTransportException;

	/**
	 * Metodo responsavel por buscar no cassandra dados de visualizacao.
	 * 
	 * @param columnFamily String : Nome da column family que com contem os dados.
	 * @param rowKey String : Key da column family que com contem os dados.
	 * @param superColumns String : Nomes das colunas que contem os dados.
	 * 
	 * @return List<SuperColumn> : Lista de dados no formato de SuperColumn contendo
	 * os dados de visualizacoes.
	 * 
	 * @throws InvalidRequestException chamada de metodos invalido.
	 * @throws UnavailableException servlet não está ativo.
	 * @throws TimedOutException tempo de bloqueo na acao excedido.
	 * @throws TException erro generico do Thrift. Pode ser de qualquer tipo.
	 * @throws UnsupportedEncodingException caracteres invalidos dentro da column.
	 * @throws TTransportException possivel erro de conexao no DB Cassandra, ou 
	 * seja o banco de dados estar com Status: Stop.
	 */
	public List<SuperColumn> findSuperColumnByRowKeyAndSuperColumn(String columnFamily, String rowKey, String... superColumns) throws InvalidRequestException, NotFoundException, UnavailableException, TimedOutException, TException, UnsupportedEncodingException;
	
	
	
	
	
	
	
	
	
	
	
	//TODO: Ajustes e comentarios...
	public SuperColumn findSuperColumnByRowKeyAndSuperColumn(String columnFamily, String rowKey, String superColumn) throws InvalidRequestException, NotFoundException, UnavailableException, TimedOutException, TException, UnsupportedEncodingException;
	public List<KeySlice> findKeysSlicesByRowKey(String columnFamily, String rowKey) throws InvalidRequestException, NotFoundException, UnavailableException, TimedOutException, TException, UnsupportedEncodingException;
	public Boolean isExistingRowKey(String columnFamily, String rowKey) throws InvalidRequestException, UnavailableException, TimedOutException, TException, UnsupportedEncodingException;
	public String findMyIPByLogin(String columnFamily, String login, String password) throws InvalidRequestException, NotFoundException, UnavailableException, TimedOutException, TException, UnsupportedEncodingException;
}