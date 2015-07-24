package facebook.bot.cassandra;

import static facebook.bot.cassandra.Constants.CL_1;

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
		} catch (TTransportException e) {
			logger.info("unusual exception occurred");
			logger.error("[Info: db cassandra stopped] - [Error: {}]", e.toString());
		} catch (InvalidRequestException e) {
			logger.info("unusual exception occurred");
			logger.error("[Info: access to invalid method] - [Error: {}]", e.toString());
		} catch (UnavailableException e) {
			logger.info("unusual exception occurred");
			logger.error("[Info: servlet container is not active] - [Error: {}]", e.toString());
		} catch (TimedOutException e) {
			logger.info("unusual exception occurred");
			logger.error("[Info: time out for insert] - [Error: {}]", e.toString());
		} catch (TException e) {
			logger.error("[Info: generic exception of thrift] - [Error: {}]", e.toString());
		} finally {
			getClientClose();
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
		} catch (TTransportException e) {
			logger.info("unusual exception occurred");
			logger.error("[Info: db cassandra stopped] - [Error: {}]", e.toString());
		} catch (InvalidRequestException e) {
			logger.info("unusual exception occurred");
			logger.error("[Info: access to invalid method] - [Error: {}]", e.toString());
		} catch (UnavailableException e) {
			logger.info("unusual exception occurred");
			logger.error("[Info: servlet container is not active] - [Error: {}]", e.toString());
		} catch (TimedOutException e) {
			logger.info("unusual exception occurred");
			logger.error("[Info: time out for insert] - [Error: {}]", e.toString());
		} catch (TException e) {
			logger.error("[Info: generic exception of thrift] - [Error: {}]", e.toString());
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
			logger.error("[Info: db cassandra stopped] - [Error: {}]", e.toString());
		} catch (InvalidRequestException e) {
			logger.info("unusual exception occurred");
			logger.error("[Info: access to invalid method] - [Error: {}]", e.toString());
		} catch (UnavailableException e) {
			logger.info("unusual exception occurred");
			logger.error("[Info: servlet container is not active] - [Error: {}]", e.toString());
		} catch (TimedOutException e) {
			logger.info("unusual exception occurred");
			logger.error("[Info: time out for insert] - [Error: {}]", e.toString());
		} catch (TException e) {
			logger.error("[Info: generic exception of thrift] - [Error: {}]", e.toString());
		} finally {
			getClientClose();
		}
	}
	
	@Override
	public void insertColumn(String columnFamily, String rowKey, Column column) {
		try {
			ColumnParent parent = new ColumnParent(columnFamily);
	        getClientConect().insert(ByteBuffer.wrap(rowKey.getBytes()), parent, column, CL_1);
		} catch (TTransportException e) {
			logger.info("unusual exception occurred");
			logger.error("[Info: db cassandra stopped] - [Error: {}]", e.toString());
		} catch (InvalidRequestException e) {
			logger.info("unusual exception occurred");
			logger.error("[Info: access to invalid method] - [Error: {}]", e.toString());
		} catch (UnavailableException e) {
			logger.info("unusual exception occurred");
			logger.error("[Info: servlet container is not active] - [Error: {}]", e.toString());
		} catch (TimedOutException e) {
			logger.info("unusual exception occurred");
			logger.error("[Info: time out for insert] - [Error: {}]", e.toString());
		} catch (TException e) {
			logger.error("[Info: generic exception of thrift] - [Error: {}]", e.toString());
		} finally {
			getClientClose();
		}
	}

	@Override
	public void insertColumns(String columnFamily, String rowKey, List<Column> columns) {
		try {
			ColumnParent parent = new ColumnParent(columnFamily);
	        ByteBuffer rk = ByteBuffer.wrap(rowKey.getBytes());
	        Cassandra.Client client = getClientConect();
	        for (Column column : columns) {
	        	client.insert(rk, parent, column, CL_1);
			}
		} catch (TTransportException e) {
			logger.info("unusual exception occurred");
			logger.error("[Info: db cassandra stopped] - [Error: {}]", e.toString());
		} catch (InvalidRequestException e) {
			logger.info("unusual exception occurred");
			logger.error("[Info: access to invalid method] - [Error: {}]", e.toString());
		} catch (UnavailableException e) {
			logger.info("unusual exception occurred");
			logger.error("[Info: servlet container is not active] - [Error: {}]", e.toString());
		} catch (TimedOutException e) {
			logger.info("unusual exception occurred");
			logger.error("[Info: time out for insert] - [Error: {}]", e.toString());
		} catch (TException e) {
			logger.error("[Info: generic exception of thrift] - [Error: {}]", e.toString());
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
			logger.error("[Info: db cassandra stopped] - [Error: {}]", e.toString());
		} catch (InvalidRequestException e) {
			logger.info("unusual exception occurred");
			logger.error("[Info: access to invalid method] - [Error: {}]", e.toString());
		} catch (UnavailableException e) {
			logger.info("unusual exception occurred");
			logger.error("[Info: servlet container is not active] - [Error: {}]", e.toString());
		} catch (TimedOutException e) {
			logger.info("unusual exception occurred");
			logger.error("[Info: time out for insert] - [Error: {}]", e.toString());
		} catch (TException e) {
			logger.error("[Info: generic exception of thrift] - [Error: {}]", e.toString());
		} finally {
			getClientClose();
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
	public Integer countColumnsInSuperColumn(String columnFamily, String rowKey, String superColumn) {
		ColumnPath path = new ColumnPath();
		path.super_column = ByteBuffer.wrap(superColumn.getBytes());
		path.column_family = columnFamily;
		try {
			ColumnOrSuperColumn sc = getClientConect().get(ByteBuffer.wrap(rowKey.getBytes()), path, CL_1);
			return sc.super_column.getColumns().size();
		} catch (TTransportException e) {
			logger.info("unusual exception occurred");
			logger.error("[Info: db cassandra stopped] - [Error: {}]", e.toString());
		} catch (InvalidRequestException e) {
			logger.info("unusual exception occurred");
			logger.error("[Info: access to invalid method] - [Error: {}]", e.toString());
		} catch (UnavailableException e) {
			logger.info("unusual exception occurred");
			logger.error("[Info: servlet container is not active] - [Error: {}]", e.toString());
		} catch (TimedOutException e) {
			logger.info("unusual exception occurred");
			logger.error("[Info: time out for insert] - [Error: {}]", e.toString());
		} catch (TException e) {
			logger.error("[Info: generic exception of thrift] - [Error: {}]", e.toString());
		} finally {
			getClientClose();
		}
		return null;
	}
	
	@Override
	public Integer countColumnOrSuperColumn(String columnFamily, String rowKey) {
		SlicePredicate predicate = getSlicePredicate(new byte[0], new byte[0], false, sliceRangeCount);
		ColumnParent parent = new ColumnParent(columnFamily);
		KeyRange keyRange = getKeyRange(rowKey, 1);
		try {
			List<KeySlice> keysSlices = getClientConect().get_range_slices(parent, predicate, keyRange, CL_1);
			Integer c = 0;
			for (KeySlice ks : keysSlices) {
				c += ks.columns.size();
			}
			return c;
		} catch (TTransportException e) {
			logger.info("unusual exception occurred");
			logger.error("[Info: db cassandra stopped] - [Error: {}]", e.toString());
		} catch (InvalidRequestException e) {
			logger.info("unusual exception occurred");
			logger.error("[Info: access to invalid method] - [Error: {}]", e.toString());
		} catch (UnavailableException e) {
			logger.info("unusual exception occurred");
			logger.error("[Info: servlet container is not active] - [Error: {}]", e.toString());
		} catch (TimedOutException e) {
			logger.info("unusual exception occurred");
			logger.error("[Info: time out for insert] - [Error: {}]", e.toString());
		} catch (TException e) {
			logger.error("[Info: generic exception of thrift] - [Error: {}]", e.toString());
		} finally {
			getClientClose();
		}
		return 0;
	}
	
	@Override
	public Column findColumn(String columnFamily, String rowKey, String column) {
		ColumnPath path = new ColumnPath();
		path.column_family = columnFamily;
		path.column = ByteBuffer.wrap(column.getBytes());
		try {
			ColumnOrSuperColumn sc = getClientConect().get(ByteBuffer.wrap(rowKey.getBytes()), path, CL_1);
			return sc.column;
		} catch (TTransportException e) {
			logger.info("unusual exception occurred");
			logger.error("[Info: db cassandra stopped] - [Error: {}]", e.toString());
		} catch (InvalidRequestException e) {
			logger.info("unusual exception occurred");
			logger.error("[Info: access to invalid method] - [Error: {}]", e.toString());
		} catch (UnavailableException e) {
			logger.info("unusual exception occurred");
			logger.error("[Info: servlet container is not active] - [Error: {}]", e.toString());
		} catch (TimedOutException e) {
			logger.info("unusual exception occurred");
			logger.error("[Info: time out for insert] - [Error: {}]", e.toString());
		} catch (TException e) {
			logger.error("[Info: generic exception of thrift] - [Error: {}]", e.toString());
		} finally {
			getClientClose();
		}
		return null;
	}
	
	@Override
	public Column findColumn(String columnFamily, String rowKey, String superColumn, String column) {
		ColumnPath path = new ColumnPath();
		path.column_family = columnFamily;
		path.super_column = ByteBuffer.wrap(superColumn.getBytes());
		path.column = ByteBuffer.wrap(column.getBytes());
		try {
			ColumnOrSuperColumn sc = getClientConect().get(ByteBuffer.wrap(rowKey.getBytes()), path, CL_1);
			return sc.column;
		} catch (TTransportException e) {
			logger.info("unusual exception occurred");
			logger.error("[Info: db cassandra stopped] - [Error: {}]", e.toString());
		} catch (InvalidRequestException e) {
			logger.info("unusual exception occurred");
			logger.error("[Info: access to invalid method] - [Error: {}]", e.toString());
		} catch (UnavailableException e) {
			logger.info("unusual exception occurred");
			logger.error("[Info: servlet container is not active] - [Error: {}]", e.toString());
		} catch (TimedOutException e) {
			logger.info("unusual exception occurred");
			logger.error("[Info: time out for insert] - [Error: {}]", e.toString());
		} catch (TException e) {
			logger.error("[Info: generic exception of thrift] - [Error: {}]", e.toString());
		} finally {
			getClientClose();
		}
		return null;
	}
	
	@Override
	public List<Column> findColumns(String columnFamily, String rowKey, String...columns) {
		List<Column> cs = new ArrayList<Column>();
		ColumnPath path = new ColumnPath();
		path.column_family = columnFamily;
		try {
			Cassandra.Client client = getClientConect();
			for (String column : columns) {
				path.column = ByteBuffer.wrap(column.getBytes());
				try {
					ColumnOrSuperColumn sc = client.get(ByteBuffer.wrap(rowKey.getBytes()), path, CL_1);
					cs.add(sc.column);
				} catch (NotFoundException e) {
					logger.error("[Info: data not found] - [Column Family: " + columnFamily + "] - [Column: " + column + "] - [Error: {}]", e.toString());
				}
			}
		} catch (TTransportException e) {
			logger.info("unusual exception occurred");
			logger.error("[Info: db cassandra stopped] - [Error: {}]", e.toString());
		} catch (InvalidRequestException e) {
			logger.info("unusual exception occurred");
			logger.error("[Info: access to invalid method] - [Error: {}]", e.toString());
		} catch (UnavailableException e) {
			logger.info("unusual exception occurred");
			logger.error("[Info: servlet container is not active] - [Error: {}]", e.toString());
		} catch (TimedOutException e) {
			logger.info("unusual exception occurred");
			logger.error("[Info: time out for insert] - [Error: {}]", e.toString());
		} catch (TException e) {
			logger.error("[Info: generic exception of thrift] - [Error: {}]", e.toString());
		} finally {
			getClientClose();
		}
		return cs;
	}
	
	@Override
	public SuperColumn findSuperColumn(String columnFamily, String rowKey, String superColumn) {
		ColumnPath path = new ColumnPath();
		path.super_column = ByteBuffer.wrap(superColumn.getBytes());
		path.column_family = columnFamily;
		try {
			ColumnOrSuperColumn sc = getClientConect().get(ByteBuffer.wrap(rowKey.getBytes()), path, CL_1);
			return sc.super_column;
		} catch (TTransportException e) {
			logger.info("unusual exception occurred");
			logger.error("[Info: db cassandra stopped] - [Error: {}]", e.toString());
		} catch (InvalidRequestException e) {
			logger.info("unusual exception occurred");
			logger.error("[Info: access to invalid method] - [Error: {}]", e.toString());
		} catch (UnavailableException e) {
			logger.info("unusual exception occurred");
			logger.error("[Info: servlet container is not active] - [Error: {}]", e.toString());
		} catch (TimedOutException e) {
			logger.info("unusual exception occurred");
			logger.error("[Info: time out for insert] - [Error: {}]", e.toString());
		} catch (TException e) {
			logger.error("[Info: generic exception of thrift] - [Error: {}]", e.toString());
		} finally {
			getClientClose();
		}
		return null;
	}
	
	@Override
	public List<SuperColumn> findSuperColumns(String columnFamily, String rowKey, String...superColumns) {
		List<SuperColumn> scs = new ArrayList<SuperColumn>();
		ColumnPath path = new ColumnPath();
		path.column_family = columnFamily;
		try {
			Cassandra.Client client = getClientConect();
			for (String superColumn : superColumns) {
				path.super_column = ByteBuffer.wrap(superColumn.getBytes());
				try {
					ColumnOrSuperColumn sc = client.get(ByteBuffer.wrap(rowKey.getBytes()), path, CL_1);
					scs.add(sc.super_column);
				} catch (NotFoundException e) {
					logger.error("[Info: data not found] - [Column Family: " + columnFamily + "] - [Super Column: " + superColumn + "] - [Error: {}]", e.toString());
				}
			}
		} catch (TTransportException e) {
			logger.info("unusual exception occurred");
			logger.error("[Info: db cassandra stopped] - [Error: {}]", e.toString());
		} catch (InvalidRequestException e) {
			logger.info("unusual exception occurred");
			logger.error("[Info: access to invalid method] - [Error: {}]", e.toString());
		} catch (UnavailableException e) {
			logger.info("unusual exception occurred");
			logger.error("[Info: servlet container is not active] - [Error: {}]", e.toString());
		} catch (TimedOutException e) {
			logger.info("unusual exception occurred");
			logger.error("[Info: time out for insert] - [Error: {}]", e.toString());
		} catch (TException e) {
			logger.error("[Info: generic exception of thrift] - [Error: {}]", e.toString());
		} finally {
			getClientClose();
		}
		return scs;
	}
	
	@Override
	public List<ColumnOrSuperColumn> findColumnOrSuperColumn(String columnFamily, String rowKey) {
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
		} catch (TTransportException e) {
			logger.info("unusual exception occurred");
			logger.error("[Info: db cassandra stopped] - [Error: {}]", e.toString());
		} catch (InvalidRequestException e) {
			logger.info("unusual exception occurred");
			logger.error("[Info: access to invalid method] - [Error: {}]", e.toString());
		} catch (UnavailableException e) {
			logger.info("unusual exception occurred");
			logger.error("[Info: servlet container is not active] - [Error: {}]", e.toString());
		} catch (TimedOutException e) {
			logger.info("unusual exception occurred");
			logger.error("[Info: time out for insert] - [Error: {}]", e.toString());
		} catch (TException e) {
			logger.error("[Info: generic exception of thrift] - [Error: {}]", e.toString());
		} finally {
			getClientClose();
		}
		return null;
	}
}