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
import org.apache.cassandra.thrift.Compression;
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
	private static Integer sliceRangeCount = 999999999;

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
	public void insertSuperColumn(String columnFamily, String rowKey, String superColumn, Column column) {
		try {
			Map<ByteBuffer, Map<String, List<Mutation>>> mutationMapOuter = new HashMap<ByteBuffer, Map<String, List<Mutation>>>();
			Map<String, List<Mutation>> mutationMapInner = new HashMap<String, List<Mutation>>();
			List<Mutation> mutationList = new ArrayList<Mutation>();
			Mutation m = new Mutation();
			ColumnOrSuperColumn cosc = new ColumnOrSuperColumn();
			SuperColumn sc = new SuperColumn();
			sc.name = ByteBuffer.wrap(superColumn.getBytes());
			List<Column> columns = new ArrayList<Column>();
			columns.add(column);
			sc.columns = columns;
			cosc.super_column = sc;
			m.setColumn_or_supercolumn(cosc);
			mutationList.add(m);
			mutationMapInner.put(columnFamily, mutationList);
			mutationMapOuter.put(ByteBuffer.wrap(rowKey.getBytes()), mutationMapInner);
			getClientConect().batch_mutate(mutationMapOuter, CL_1);
			getClientClose();
		} catch (TTransportException e) {
			logger.info("unusual exception occurred");
			logger.error("[Info: db cassandra stopped] - [Error: " + e.getMessage() + "]", e);
		} catch (InvalidRequestException e) {
			logger.info("unusual exception occurred");//TODO: check return stack with problems
			logger.error("[Info: access to invalid method] - [Error: " + e.getMessage() + "]", e);
		} catch (UnavailableException e) {
			logger.info("unusual exception occurred");
			logger.error("[Info: servlet container is not active] - [Error: " + e.getMessage() + "]", e);
		} catch (TimedOutException e) {
			logger.info("unusual exception occurred");
			logger.error("[Info: time out for insert] - [Error: " + e.getMessage() + "]", e);
		} catch (TException e) {
			logger.error("[Info: generic exception of thrift] - [Error: " + e.getMessage() + "]", e);
		}
	}
	
	@Override
	public void insertSuperColumns(String columnFamily, String rowKey, String superColumn, List<Column> columns) {
		try {
			Map<ByteBuffer, Map<String, List<Mutation>>> mutationMapOuter = new HashMap<ByteBuffer, Map<String, List<Mutation>>>();
			Map<String, List<Mutation>> mutationMapInner = new HashMap<String, List<Mutation>>();
			List<Mutation> mutationList = new ArrayList<Mutation>();
			Mutation mutation = new Mutation();
			ColumnOrSuperColumn cosc = new ColumnOrSuperColumn();
			SuperColumn sc = new SuperColumn();
			sc.name = ByteBuffer.wrap(superColumn.getBytes());
			sc.columns = columns;
			cosc.super_column = sc;
			mutation.setColumn_or_supercolumn(cosc);
			mutationList.add(mutation);
			mutationMapInner.put(columnFamily, mutationList);
			mutationMapOuter.put(ByteBuffer.wrap(rowKey.getBytes()), mutationMapInner);
			getClientConect().batch_mutate(mutationMapOuter, CL_1);
			getClientClose();
		} catch (TTransportException e) {
			logger.info("unusual exception occurred");
			logger.error("[Info: db cassandra stopped] - [Error: " + e.getMessage() + "]", e);
		} catch (InvalidRequestException e) {
			logger.info("unusual exception occurred");//TODO: check return stack with problems
			logger.error("[Info: access to invalid method] - [Error: " + e.getMessage() + "]", e);
		} catch (UnavailableException e) {
			logger.info("unusual exception occurred");
			logger.error("[Info: servlet container is not active] - [Error: " + e.getMessage() + "]", e);
		} catch (TimedOutException e) {
			logger.info("unusual exception occurred");
			logger.error("[Info: time out for insert] - [Error: " + e.getMessage() + "]", e);
		} catch (TException e) {
			logger.error("[Info: generic exception of thrift] - [Error: " + e.getMessage() + "]", e);
		}
	}
	
	@Override
	public void insertColumn(String columnFamily, String rowKey, Column column) {
		try {
			ColumnParent parent = new ColumnParent(columnFamily);
	        getClientConect().insert(ByteBuffer.wrap(rowKey.getBytes()), parent, column, CL_1);
	        getClientClose();
		} catch (TTransportException e) {
			logger.info("unusual exception occurred");
			logger.error("[Info: db cassandra stopped] - [Error: " + e.getMessage() + "]", e);
		} catch (InvalidRequestException e) {
			logger.info("unusual exception occurred");
			logger.error("[Info: access to invalid method] - [Error: " + e.getMessage() + "]", e);
		} catch (UnavailableException e) {
			logger.info("unusual exception occurred");
			logger.error("[Info: servlet container is not active] - [Error: " + e.getMessage() + "]", e);
		} catch (TimedOutException e) {
			logger.info("unusual exception occurred");
			logger.error("[Info: time out for insert] - [Error: " + e.getMessage() + "]", e);
		} catch (TException e) {
			logger.error("[Info: generic exception of thrift] - [Error: " + e.getMessage() + "]", e);
		}
	}

	@Override
	public void insertColumns(String columnFamily, String rowKey, List<Column> columns) {
		try {
			ColumnParent parent = new ColumnParent(columnFamily);
	        ByteBuffer rowid = ByteBuffer.wrap(rowKey.getBytes());
	        for (Column column : columns) {
	        	getClientConect().insert(rowid, parent, column, CL_1);
			}
	        getClientClose();
		} catch (TTransportException e) {
			logger.info("unusual exception occurred");
			logger.error("[Info: db cassandra stopped] - [Error: " + e.getMessage() + "]", e);
		} catch (InvalidRequestException e) {
			logger.info("unusual exception occurred");//TODO: check return stack with problems
			logger.error("[Info: access to invalid method] - [Error: " + e.getMessage() + "]", e);
		} catch (UnavailableException e) {
			logger.info("unusual exception occurred");
			logger.error("[Info: servlet container is not active] - [Error: " + e.getMessage() + "]", e);
		} catch (TimedOutException e) {
			logger.info("unusual exception occurred");
			logger.error("[Info: time out for insert] - [Error: " + e.getMessage() + "]", e);
		} catch (TException e) {
			logger.error("[Info: generic exception of thrift] - [Error: " + e.getMessage() + "]", e);
		}
	}
	
	private SlicePredicate getSlicePredicate(byte[] start, byte[] finish, boolean reversed, int count) {
		SlicePredicate predicate = new SlicePredicate();
		SliceRange sliceRange = new SliceRange();
		sliceRange.setStart(start);
		sliceRange.setFinish(finish);
		sliceRange.setReversed(reversed);
		if (count > 0) {
			sliceRange.setCount(sliceRangeCount);
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
		SlicePredicate predicate = getSlicePredicate(new byte[0], new byte[0], false, sliceRangeCount);
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
	
	@Override
	public void removeSuperColumn(String columnFamily, String rowKey, String superColumn) {
		try {
			Clock clock = new Clock(System.nanoTime());
			Map<ByteBuffer, Map<String, List<Mutation>>> mutationMapOuter = new HashMap<ByteBuffer, Map<String, List<Mutation>>>();
			Map<String, List<Mutation>> mutationMapInner = new HashMap<String, List<Mutation>>();
			List<Mutation> mutationList = new ArrayList<Mutation>();
			Mutation mutation = new Mutation();
			Deletion deletion = new Deletion();
			deletion.setSuper_column(superColumn.getBytes());
			deletion.setTimestamp(clock.timestamp);
			mutation.setDeletion(deletion);
			mutationList.add(mutation);
			mutationMapInner.put(columnFamily, mutationList);
			mutationMapOuter.put(ByteBuffer.wrap(rowKey.getBytes()), mutationMapInner);
			getClientConect().batch_mutate(mutationMapOuter, CL_1);
		} catch (TTransportException e) {
			logger.info("unusual exception occurred");
			logger.error("[Info: db cassandra stopped] - [Error: " + e.getMessage() + "]", e);
		} catch (InvalidRequestException e) {
			logger.info("unusual exception occurred");//TODO: check return stack with problems
			logger.error("[Info: access to invalid method] - [Error: " + e.getMessage() + "]", e);
		} catch (UnavailableException e) {
			logger.info("unusual exception occurred");
			logger.error("[Info: servlet container is not active] - [Error: " + e.getMessage() + "]", e);
		} catch (TimedOutException e) {
			logger.info("unusual exception occurred");
			logger.error("[Info: time out for insert] - [Error: " + e.getMessage() + "]", e);
		} catch (TException e) {
			logger.error("[Info: generic exception of thrift] - [Error: " + e.getMessage() + "]", e);
		} finally {
			getClientClose();
		}
	}

	@Override
	public void removeColumn(String columnFamily, String rowKey) {
		try {
			StringBuilder cql = new StringBuilder();
			cql.append("delete ");
			cql.append(" from ");
			cql.append(columnFamily);
			cql.append(" where KEY = '");
			cql.append(rowKey);
			cql.append("'");
			String query = cql.toString();
			getClientConect().execute_cql_query(ByteBuffer.wrap(query.getBytes()), Compression.NONE);
		} catch (TTransportException e) {
			logger.info("unusual exception occurred");
			logger.error("[Info: db cassandra stopped] - [Error: " + e.getMessage() + "]", e);
		} catch (InvalidRequestException e) {
			logger.info("unusual exception occurred");//TODO: check return stack with problems
			logger.error("[Info: access to invalid method] - [Error: " + e.getMessage() + "]", e);
		} catch (UnavailableException e) {
			logger.info("unusual exception occurred");
			logger.error("[Info: servlet container is not active] - [Error: " + e.getMessage() + "]", e);
		} catch (TimedOutException e) {
			logger.info("unusual exception occurred");
			logger.error("[Info: time out for insert] - [Error: " + e.getMessage() + "]", e);
		} catch (TException e) {
			logger.error("[Info: generic exception of thrift] - [Error: " + e.getMessage() + "]", e);
		} finally {
			getClientClose();
		}
	}
}