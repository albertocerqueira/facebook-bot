package facebook.bot.cassandra;

import static facebook.bot.cassandra.Constants.CL_1;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.apache.cassandra.thrift.Cassandra;
import org.apache.cassandra.thrift.Column;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Finder implements IFinder {

	final static Logger logger = LoggerFactory.getLogger(Facebook.class);
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
			for(KeySlice ks : keysSlices){
				csc.addAll(ks.columns);
			}
			return csc;
		} finally {
			getClientClose();
		}
	}
}