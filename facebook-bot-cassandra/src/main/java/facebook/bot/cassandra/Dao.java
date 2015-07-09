package facebook.bot.cassandra;

import static facebook.bot.cassandra.Constants.CL_1;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.cassandra.thrift.Cassandra;
import org.apache.cassandra.thrift.Column;
import org.apache.cassandra.thrift.ColumnOrSuperColumn;
import org.apache.cassandra.thrift.ColumnParent;
import org.apache.cassandra.thrift.Deletion;
import org.apache.cassandra.thrift.InvalidRequestException;
import org.apache.cassandra.thrift.Mutation;
import org.apache.cassandra.thrift.SlicePredicate;
import org.apache.cassandra.thrift.SliceRange;
import org.apache.cassandra.thrift.SuperColumn;
import org.apache.cassandra.thrift.TimedOutException;
import org.apache.cassandra.thrift.UnavailableException;
import org.apache.thrift.TException;
import org.apache.thrift.transport.TTransportException;

public class Dao implements Idao {

	private static Connector connector = null;

	private static Connector getConnector() {
		if (connector == null) {
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
	
	@Override
	public void insertSuperColumn(String columnFamily, String rowKey, String superColumn, Column column) throws InvalidRequestException, UnavailableException, TimedOutException, TException, UnsupportedEncodingException {
		Map<ByteBuffer, Map<String, List<Mutation>>> outerMap = new HashMap<ByteBuffer, Map<String, List<Mutation>>>();
		List<Mutation> columnsToAdd = new ArrayList<Mutation>();
		Map<String, List<Mutation>> innerMap = new HashMap<String, List<Mutation>>();
		Mutation m = new Mutation();
		ColumnOrSuperColumn cosc = new ColumnOrSuperColumn();
		SuperColumn sc = new SuperColumn();
		sc.name = ByteBuffer.wrap(superColumn.getBytes());
		List<Column> columns = new ArrayList<Column>();
		columns.add(column);
		sc.columns = columns;
		cosc.super_column = sc;
		m.setColumn_or_supercolumn(cosc);
		columnsToAdd.add(m);
		innerMap.put(columnFamily, columnsToAdd);
		outerMap.put(ByteBuffer.wrap(rowKey.getBytes()), innerMap);
		getClientConect().batch_mutate(outerMap, CL_1);
		getClientClose();
	}
	
	@Override
	public void insertSuperColumns(String columnFamily, String rowKey, String superColumn, List<Column> columns) throws InvalidRequestException, UnavailableException, TimedOutException, TException, UnsupportedEncodingException {
		Map<ByteBuffer, Map<String, List<Mutation>>> outerMap = new HashMap<ByteBuffer, Map<String, List<Mutation>>>();
		List<Mutation> columnsToAdd = new ArrayList<Mutation>();
		Map<String, List<Mutation>> innerMap = new HashMap<String, List<Mutation>>();
		Mutation m = new Mutation();
		ColumnOrSuperColumn cosc = new ColumnOrSuperColumn();
		SuperColumn sc = new SuperColumn();
		sc.name = ByteBuffer.wrap(superColumn.getBytes());
		sc.columns = columns;
		cosc.super_column = sc;
		m.setColumn_or_supercolumn(cosc);
		columnsToAdd.add(m);
		innerMap.put(columnFamily, columnsToAdd);
		outerMap.put(ByteBuffer.wrap(rowKey.getBytes()), innerMap);
		getClientConect().batch_mutate(outerMap, CL_1);
		getClientClose();
	}
	
	@Override
	public void insertColumn(String columnFamily, String rowKey, Column column) throws InvalidRequestException, UnavailableException, TimedOutException, TException, UnsupportedEncodingException {
		ColumnParent parent = new ColumnParent(columnFamily);
        ByteBuffer rowid = ByteBuffer.wrap(rowKey.getBytes());
        getClientConect().insert(rowid, parent, column, CL_1);
        getClientClose();
	}

	@Override
	public void insertColumns(String columnFamily, String rowKey, List<Column> columns) throws InvalidRequestException, UnavailableException, TimedOutException, TException, UnsupportedEncodingException {
		ColumnParent parent = new ColumnParent(columnFamily);
        ByteBuffer rowid = ByteBuffer.wrap(rowKey.getBytes());
        for (Column column : columns) {
        	getClientConect().insert(rowid, parent, column, CL_1);
		}
        getClientClose();
	}
	
	
	//TODO: Ajustar metodos abaixo...
	@Override
	public void removeSuperColumn(String columnFamily, String rowKey, String superColumnName) throws InvalidRequestException, UnavailableException, TimedOutException, TException, UnsupportedEncodingException {
		Clock clock = new Clock(System.nanoTime());
		Map<ByteBuffer, Map<String, List<Mutation>>> outerMap = new HashMap<ByteBuffer, Map<String, List<Mutation>>>();
		Map<String, List<Mutation>> innerMap = new HashMap<String, List<Mutation>>();
		List<Mutation> columnsToAdd = new ArrayList<Mutation>();
		Mutation mutation = new Mutation();
		Deletion deletion = new Deletion();
		deletion.setSuper_column(superColumnName.getBytes());
		deletion.setTimestamp(clock.timestamp);
		mutation.setDeletion(deletion);
		columnsToAdd.add(mutation);
		innerMap.put(columnFamily, columnsToAdd);
		outerMap.put(ByteBuffer.wrap(rowKey.getBytes()), innerMap);
		getClientConect().batch_mutate(outerMap, CL_1);
		getClientClose();
	}

	@Override
	public void removeColumnSuperColumn(String columnFamily, String rowKey, String superColumnName, String columnName, Integer count) throws InvalidRequestException, UnavailableException, TimedOutException, TException, UnsupportedEncodingException {
		Clock clock = new Clock(System.nanoTime());
		Map<ByteBuffer, Map<String, List<Mutation>>> outerMap = new HashMap<ByteBuffer, Map<String, List<Mutation>>>();
		Map<String, List<Mutation>> innerMap = new HashMap<String, List<Mutation>>();
		List<Mutation> columnsToAdd = new ArrayList<Mutation>();
		Mutation mutation = new Mutation();
		Deletion deletion = new Deletion();
		deletion.setSuper_column(superColumnName.getBytes());
		SlicePredicate predicate = new SlicePredicate();
		SliceRange sliceRange = new SliceRange();
		sliceRange.setStart(columnName.getBytes());
		sliceRange.setFinish(columnName.getBytes());
		sliceRange.setReversed(false);
		if (count > 0) {
			sliceRange.setCount(count);
		}
		predicate.setSlice_range(sliceRange);
		deletion.setPredicate(predicate);
		deletion.setTimestamp(clock.timestamp);
		mutation.setDeletion(deletion);
		columnsToAdd.add(mutation);
		innerMap.put(columnFamily, columnsToAdd);
		outerMap.put(ByteBuffer.wrap(rowKey.getBytes()), innerMap);
		getClientConect().batch_mutate(outerMap, CL_1);
		getClientClose();
	}
}