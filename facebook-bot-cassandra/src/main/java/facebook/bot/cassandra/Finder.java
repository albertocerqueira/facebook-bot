package facebook.bot.cassandra;

import static facebook.bot.cassandra.Constants.CL_1;
import static facebook.bot.cassandra.Constants.UTF8;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.apache.cassandra.thrift.Cassandra;
import org.apache.cassandra.thrift.ColumnOrSuperColumn;
import org.apache.cassandra.thrift.ColumnParent;
import org.apache.cassandra.thrift.ColumnPath;
import org.apache.cassandra.thrift.InvalidRequestException;
import org.apache.cassandra.thrift.KeyRange;
import org.apache.cassandra.thrift.KeySlice;
import org.apache.cassandra.thrift.NotFoundException;
import org.apache.cassandra.thrift.SlicePredicate;
import org.apache.cassandra.thrift.SliceRange;
import org.apache.cassandra.thrift.SuperColumn;
import org.apache.cassandra.thrift.TimedOutException;
import org.apache.cassandra.thrift.UnavailableException;
import org.apache.thrift.TException;
import org.apache.thrift.transport.TTransportException;

public class Finder implements IFinder {

	private static Connector connector = null;

	private static Connector getConnector() {
		if(connector == null){
			connector = new Connector();
		}
		return connector;
	}
	
	private static Cassandra.Client getClientConect() throws TTransportException, InvalidRequestException, TException {
		return getConnector().connect();
	}
	
	private void getClientClose() {
		getConnector().close();
	}
	
	private SlicePredicate getSlicePredicate(byte[] start, byte[] finish, boolean reversed, int count){
		SlicePredicate predicate = new SlicePredicate();
		SliceRange sliceRange = new SliceRange();
		sliceRange.setStart(start);
		sliceRange.setFinish(finish);
		sliceRange.setReversed(reversed);
		if(count > 0){
			sliceRange.setCount(count);
		}
		predicate.setSlice_range(sliceRange);
		return predicate;
	}
	
	private KeyRange getKeyRange(String rowKey, int count){
		KeyRange keyRange = new KeyRange(count);
		keyRange.setStart_key(rowKey.getBytes());
		keyRange.setEnd_key(rowKey.getBytes());
		return keyRange;
	}
	
	public List<SuperColumn> findSuperColumnByRowKeyAndSuperColumn(String columnFamily, String rowKey, String... superColumns) throws InvalidRequestException, NotFoundException, UnavailableException, TimedOutException, TException, UnsupportedEncodingException {
		List<SuperColumn> scs = new ArrayList<SuperColumn>();
		ColumnPath path = new ColumnPath();
		path.column_family = columnFamily;
		Cassandra.Client client = getClientConect();
		try{
			for(String superColumn : superColumns){
				path.super_column = ByteBuffer.wrap(superColumn.getBytes());
				try{
					ColumnOrSuperColumn sc = client.get(ByteBuffer.wrap(rowKey.getBytes()), path, CL_1);
					scs.add(sc.super_column);
				}catch(NotFoundException e){
					/*
					String messageSystem = GerenciadorMensagem.getMessage("system.db.cassandra.exception.data.invalid", superColumn, columnFamily);
					getLog().debug(messageSystem);
					throw new CriticalUserException(GenericFinder.class, messageSystem, e);//Erro critico!
					*/
				}
			}
		}finally{
			getClientClose();
		}
		return scs;
	}
	
	public Integer findCountDataSuperColumnByRowKey(String columnFamily, String rowKey, String superColumn) throws InvalidRequestException, NotFoundException, UnavailableException, TimedOutException, TException, UnsupportedEncodingException, TTransportException {
		ColumnPath path = new ColumnPath();
		path.super_column = ByteBuffer.wrap(superColumn.getBytes());
		path.column_family = columnFamily;
		ColumnOrSuperColumn sc = null;
		try{
			sc = getClientConect().get(ByteBuffer.wrap(rowKey.getBytes()), path, CL_1);
		}finally{
			getClientClose();
		}
		return sc.super_column.getColumns().size();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	//TODO: Ajustes e comentarios
	public SuperColumn findSuperColumnByRowKeyAndSuperColumn(String columnFamily, String rowKey, String superColumn) throws InvalidRequestException, NotFoundException, UnavailableException, TimedOutException, TException, UnsupportedEncodingException {
		ColumnPath path = new ColumnPath();
		path.super_column = ByteBuffer.wrap(superColumn.getBytes());
		path.column_family = columnFamily;
		ColumnOrSuperColumn sc = null;
		try{
			sc = getClientConect().get(ByteBuffer.wrap(rowKey.getBytes()), path, CL_1);
		}finally{
			getClientClose();
		}
		return sc.super_column;
	}
	
	public List<KeySlice> findKeysSlicesByRowKey(String columnFamily, String rowKey) throws InvalidRequestException, NotFoundException, UnavailableException, TimedOutException, TException, UnsupportedEncodingException {
		SlicePredicate predicate = getSlicePredicate(new byte[0], new byte[0], false, 0);
		ColumnParent parent = new ColumnParent(columnFamily);
		KeyRange keyRange = getKeyRange(rowKey, 1);
		List<KeySlice> keysSlices = null;
		try{
			keysSlices = getClientConect().get_range_slices(parent, predicate, keyRange, CL_1);
		}finally{
			getClientClose();
		}
		return keysSlices;
	}
	
	public Boolean isExistingRowKey(String columnFamily, String rowKey) throws InvalidRequestException, NotFoundException, UnavailableException, TimedOutException, TException, UnsupportedEncodingException {
		ColumnPath path = new ColumnPath();
		path.super_column = ByteBuffer.wrap("security".getBytes());
		path.column_family = columnFamily;
		path.column = ByteBuffer.wrap("ip".getBytes());
		try{
			try{
				getClientConect().get(ByteBuffer.wrap(rowKey.getBytes()), path, CL_1);
			}finally{
				getClientClose();
			}
			return true;
		}catch(NotFoundException not){
			getClientClose();
			return false;
		}
	}

	public String findMyIPByLogin(String columnFamily, String login, String password) throws InvalidRequestException, NotFoundException, UnavailableException, TimedOutException, TException, UnsupportedEncodingException {
		ColumnPath path = new ColumnPath();
		path.super_column = ByteBuffer.wrap("security".getBytes());
		path.column_family = columnFamily;
		ColumnOrSuperColumn sc = null;
		try{
			sc = getClientConect().get(ByteBuffer.wrap(login.getBytes()), path, CL_1);
		}finally{
			getClientClose();
		}
		if((new String(sc.super_column.columns.get(2).getValue().clone(), UTF8)).equals(login) &&
				(new String(sc.super_column.columns.get(3).getValue().clone(), UTF8)).equals(password)){
			return login;
		}else{
			throw new NotFoundException();
		}
	}
}