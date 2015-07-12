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
import org.apache.cassandra.thrift.ColumnPath;
import org.apache.cassandra.thrift.Deletion;
import org.apache.cassandra.thrift.InvalidRequestException;
import org.apache.cassandra.thrift.KeyRange;
import org.apache.cassandra.thrift.KeySlice;
import org.apache.cassandra.thrift.Mutation;
import org.apache.cassandra.thrift.NotFoundException;
import org.apache.cassandra.thrift.SlicePredicate;
import org.apache.cassandra.thrift.SliceRange;
import org.apache.cassandra.thrift.SuperColumn;
import org.apache.cassandra.thrift.TimedOutException;
import org.apache.cassandra.thrift.UnavailableException;
import org.apache.thrift.TException;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CassandraImpl implements ICassandra {

	final static Logger logger = LoggerFactory.getLogger(CassandraImpl.class);
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
	
	private SlicePredicate getSlicePredicate(byte[] start, byte[] finish, boolean reversed, int count) {
		SlicePredicate predicate = new SlicePredicate();
		SliceRange sliceRange = new SliceRange();
		sliceRange.setStart(start);
		sliceRange.setFinish(finish);
		sliceRange.setReversed(reversed);
		if (count > 0) {
			sliceRange.setCount(count);
		}
		predicate.setSlice_range(sliceRange);
		return predicate;
	}
	
	private KeyRange getKeyRange(String rowKey, int count) {
		KeyRange keyRange = new KeyRange(count);
		keyRange.setStart_key(rowKey.getBytes());
		keyRange.setEnd_key(rowKey.getBytes());
		return keyRange;
	}
	
	@Override
	public Integer countColumnsInSuperColumn(String columnFamily, String rowKey, String superColumn) throws InvalidRequestException, NotFoundException, UnavailableException, TimedOutException, TException, UnsupportedEncodingException, TTransportException {
		ColumnPath path = new ColumnPath();
		path.super_column = ByteBuffer.wrap(superColumn.getBytes());
		path.column_family = columnFamily;
		ColumnOrSuperColumn sc = null;
		try {
			sc = getClientConect().get(ByteBuffer.wrap(rowKey.getBytes()), path, CL_1);
		} finally {
			getClientClose();
		}
		return sc.super_column.getColumns().size();
	}
	
	@Override
	public Column findColumn(String columnFamily, String rowKey, String column) throws InvalidRequestException, NotFoundException, UnavailableException, TimedOutException, TException, UnsupportedEncodingException {
		ColumnPath path = new ColumnPath();
		path.column_family = columnFamily;
		path.column = ByteBuffer.wrap(column.getBytes());
		try {
			ColumnOrSuperColumn sc = getClientConect().get(ByteBuffer.wrap(rowKey.getBytes()), path, CL_1);
			return sc.column;
		} finally {
			getClientClose();
		}
	}
	
	@Override
	public Column findColumn(String columnFamily, String rowKey, String superColumn, String column) throws InvalidRequestException, NotFoundException, UnavailableException, TimedOutException, TException, UnsupportedEncodingException {
		ColumnPath path = new ColumnPath();
		path.column_family = columnFamily;
		path.super_column = ByteBuffer.wrap(superColumn.getBytes());
		path.column = ByteBuffer.wrap(column.getBytes());
		try {
			ColumnOrSuperColumn sc = getClientConect().get(ByteBuffer.wrap(rowKey.getBytes()), path, CL_1);
			return sc.column;
		} finally {
			getClientClose();
		}
	}
	
	@Override
	public List<Column> findColumns(String columnFamily, String rowKey, String...columns) throws InvalidRequestException, NotFoundException, UnavailableException, TimedOutException, TException, UnsupportedEncodingException {
		List<Column> cs = new ArrayList<Column>();
		ColumnPath path = new ColumnPath();
		path.column_family = columnFamily;
		Cassandra.Client client = getClientConect();
		try {
			for (String column : columns) {
				path.column = ByteBuffer.wrap(column.getBytes());
				try {
					ColumnOrSuperColumn sc = client.get(ByteBuffer.wrap(rowKey.getBytes()), path, CL_1);
					cs.add(sc.column);
				} catch (NotFoundException e) {
					logger.error("[Info: data not found] - [Column Family: " + columnFamily + "] - [Column: " + column + "] - [Error: " + e.getMessage() + "]", e);
				}
			}
		} finally {
			getClientClose();
		}
		return cs;
	}
	
	@Override
	public SuperColumn findSuperColumn(String columnFamily, String rowKey, String superColumn) throws InvalidRequestException, NotFoundException, UnavailableException, TimedOutException, TException, UnsupportedEncodingException {
		ColumnPath path = new ColumnPath();
		path.super_column = ByteBuffer.wrap(superColumn.getBytes());
		path.column_family = columnFamily;
		try {
			ColumnOrSuperColumn sc = getClientConect().get(ByteBuffer.wrap(rowKey.getBytes()), path, CL_1);
			return sc.super_column;
		} finally {
			getClientClose();
		}
	}
	
	@Override
	public List<SuperColumn> findSuperColumns(String columnFamily, String rowKey, String...superColumns) throws InvalidRequestException, NotFoundException, UnavailableException, TimedOutException, TException, UnsupportedEncodingException {
		List<SuperColumn> scs = new ArrayList<SuperColumn>();
		ColumnPath path = new ColumnPath();
		path.column_family = columnFamily;
		Cassandra.Client client = getClientConect();
		try {
			for (String superColumn : superColumns) {
				path.super_column = ByteBuffer.wrap(superColumn.getBytes());
				try {
					ColumnOrSuperColumn sc = client.get(ByteBuffer.wrap(rowKey.getBytes()), path, CL_1);
					scs.add(sc.super_column);
				} catch (NotFoundException e) {
					logger.error("[Info: data not found] - [Column Family: " + columnFamily + "] - [Super Column: " + superColumn + "] - [Error: " + e.getMessage() + "]", e);
				}
			}
		} finally {
			getClientClose();
		}
		return scs;
	}
	
	@Override
	public List<ColumnOrSuperColumn> findColumnOrSuperColumn(String columnFamily, String rowKey) throws InvalidRequestException, NotFoundException, UnavailableException, TimedOutException, TException, UnsupportedEncodingException {
		SlicePredicate predicate = getSlicePredicate(new byte[0], new byte[0], false, 0);
		ColumnParent parent = new ColumnParent(columnFamily);
		KeyRange keyRange = getKeyRange(rowKey, 1);
		try {
			List<KeySlice> keysSlices = getClientConect().get_range_slices(parent, predicate, keyRange, CL_1);
			List<ColumnOrSuperColumn> csc = new ArrayList<ColumnOrSuperColumn>();
			for (KeySlice ks : keysSlices) {
				csc.addAll(ks.columns);
			}
			return csc;
		} finally {
			getClientClose();
		}
	}
	
	@Override// TODO: need tweaking
	public void removeSuperColumn(String columnFamily, String rowKey, String superColumn) throws InvalidRequestException, UnavailableException, TimedOutException, TException, UnsupportedEncodingException {
		Clock clock = new Clock(System.nanoTime());
		Map<ByteBuffer, Map<String, List<Mutation>>> outerMap = new HashMap<ByteBuffer, Map<String, List<Mutation>>>();
		Map<String, List<Mutation>> innerMap = new HashMap<String, List<Mutation>>();
		List<Mutation> columnsToAdd = new ArrayList<Mutation>();
		Mutation mutation = new Mutation();
		Deletion deletion = new Deletion();
		deletion.setSuper_column(superColumn.getBytes());
		deletion.setTimestamp(clock.timestamp);
		mutation.setDeletion(deletion);
		columnsToAdd.add(mutation);
		innerMap.put(columnFamily, columnsToAdd);
		outerMap.put(ByteBuffer.wrap(rowKey.getBytes()), innerMap);
		getClientConect().batch_mutate(outerMap, CL_1);
		getClientClose();
	}
	
	//@Override// TODO: need tweaking
	public void removeSuperColumn(String columnFamily, String rowKey, String superColumn, String column) throws InvalidRequestException, UnavailableException, TimedOutException, TException, UnsupportedEncodingException {
		Clock clock = new Clock(System.nanoTime());
        ColumnPath path = new ColumnPath();
		path.column_family = columnFamily;
		path.super_column = ByteBuffer.wrap(superColumn.getBytes());
		path.column = ByteBuffer.wrap(column.getBytes());
        getClientConect().send_remove(ByteBuffer.wrap(rowKey.getBytes()), path, clock.timestamp, CL_1);
        getClientClose();
        
        removeSuperColumn(columnFamily, rowKey, superColumn);
        removeSuperColumn(columnFamily, rowKey, superColumn, column, null);
		
	}
	
	//TODO: need tweaking
	public void removeSuperColumn(String columnFamily, String rowKey, String superColumn, String column, Long a) throws InvalidRequestException, UnavailableException, TimedOutException, TException, UnsupportedEncodingException {
		List<Mutation> mutations = new ArrayList<Mutation>();
		Map<String, List<Mutation>> keyMutations = new HashMap<String, List<Mutation>>();
		Map<ByteBuffer, Map<String, List<Mutation>>> mutationsMap = new HashMap<ByteBuffer, Map<String, List<Mutation>>>();
		Deletion deletion = new Deletion();
	    SlicePredicate slicePredicate = new SlicePredicate();
	    List<ByteBuffer> columns = new ArrayList<ByteBuffer>();
	    columns.add(ByteBuffer.wrap(superColumn.getBytes()));
	    slicePredicate.setColumn_names(columns);
	    deletion.setPredicate(slicePredicate);
	    deletion.setTimestamp(System.currentTimeMillis() * 1000);
	    Mutation m = new Mutation();
	    m.setDeletion(deletion);
	    mutations.add(m);
	    keyMutations.put("column_family_name", mutations);
	    mutationsMap.put(ByteBuffer.wrap(rowKey.getBytes()), keyMutations);
		getClientClose();
	}

	@Override// TODO: need tweaking
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