package facebook.bot.cassandra;

import java.util.List;

import org.apache.cassandra.thrift.Column;
import org.apache.cassandra.thrift.ColumnOrSuperColumn;
import org.apache.cassandra.thrift.SuperColumn;

public interface ICassandra {

	// TODO: create methods to insert making use of CLI scripts and information no javadoc
	
	/**
	 * Responsible method to insert any kind of super column in DB Cassandra
	 * 
	 * @param columnFamily <code>String</code><br /> name of column family that will have its recorded informations
	 * @param rowKey <code>String</code><br /> name of row key with the columns will be persisted
	 * @param superColumn <code>String</code><br /> name of super column with the columns will be persisted 
	 * @param column <code>Column</code><br /> column for persistence on data base
	 * 
	 * @author Alberto Cequeira
	 * @since 1.0
	 */
	public void insertSuperColumn(String columnFamily, String rowKey, String superColumn, Column column);
	
	/**
	 * Responsible method to insert any kind of super column in DB Cassandra
	 * 
	 * @param columnFamily <code>String</code><br /> name of column family that will have its recorded informations
	 * @param rowKey <code>String</code><br /> name of row key with the columns will be persisted
	 * @param superColumn <code>String</code><br /> name of super column with the columns will be persisted 
	 * @param columns <code>List<Column></code><br /> columns for persistence on data base
	 * 
	 * @author Alberto Cequeira
	 * @since 1.0
	 */
	public void insertSuperColumns(String columnFamily, String rowKey, String superColumn, List<Column> columns);
	
	/**
	 * Responsible method to remove any kind of super column in DB Cassandra
	 * Need config time gc_grace_seconds on cassandra-cli for to remove marked for deletion
	 * 
	 * @param columnFamily <code>String</code><br /> name of column family that will have your information removed
	 * @param rowKey <code>String</code><br /> name of row key that will have your information removed
	 * @param superColumn <code>String</code><br /> name of super column with the columns will be removed 
	 * 
	 * @author Alberto Cequeira
	 * @since 1.0
	 */
	public void removeSuperColumn(String columnFamily, String rowKey, String superColumn);

	/**
	 * Responsible method to insert any kind of column in DB Cassandra
	 * 
	 * @param columnFamily <code>String</code><br /> name of column family that will have its recorded informations
	 * @param rowKey <code>String</code><br /> name of row key with the columns will be persisted 
	 * @param column <code>Column</code><br /> column for persistence on data base
	 * 
	 * @author Alberto Cequeira
	 * @since 1.0
	 */
	public void insertColumn(String columnFamily, String rowKey, Column column);
	
	/**
	 * Responsible method to insert any kind of column in DB Cassandra
	 * 
	 * @param columnFamily <code>String</code><br /> name of column family that will have its recorded informations
	 * @param rowKey <code>String</code><br /> name of row key with the columns will be persisted 
	 * @param columns <code>List<Column></code><br /> columns for persistence on data base
	 * 
	 * @author Alberto Cequeira
	 * @since 1.0
	 */
	public void insertColumns(String columnFamily, String rowKey, List<Column> columns);

	/**
	 * Responsible method to remove any kind of column in DB Cassandra
	 * Need config time gc_grace_seconds on cassandra-cli for to remove marked for deletion
	 * 
	 * @param columnFamily <code>String</code><br /> name of column family that will have your information removed
	 * @param rowKey <code>String</code><br /> name of row key that will have your information removed
	 * 
	 * @author Alberto Cequeira
	 * @since 1.0
	 */
	public void removeColumn(String columnFamily, String rowKey);
	
	/**
	 * how many super column count
	 * 
	 * @param columnFamily <code>String</code><br /> name of column family
	 * @param rowKey <code>String</code><br /> name of row key
	 * @param superColumn <code>String</code><br /> name of super column
	 * 
	 * @return quantity <code>Integer</code>
	 * 
	 * @author Alberto Cequeira
	 * @since 1.0
	 */
	public Integer countColumnsInSuperColumn(String columnFamily, String rowKey, String superColumn);
	
	/**
	 * how many columns or super columns count
	 * 
	 * @param columnFamily <code>String</code><br /> name of column family
	 * @param rowKey <code>String</code><br /> name of row key
	 * 
	 * @return quantity <code>Integer</code>
	 * 
	 * @author Alberto Cequeira
	 * @since 1.0
	 */
	public Integer countColumnOrSuperColumn(String columnFamily, String rowKey);
	
	/**
	 * search for column
	 * 
	 * @param columnFamily <code>String</code><br /> name of column family
	 * @param rowKey <code>String</code><br /> name of row key
	 * @param column <code>String</code><br /> name of column
	 * 
	 * @return column <code>Column</code>
	 * 
	 * @author Alberto Cequeira
	 * @since 1.0
	 */
	public Column findColumn(String columnFamily, String rowKey, String column);

	/**
	 * search for column
	 * 
	 * @param columnFamily <code>String</code><br /> name of column family
	 * @param rowKey <code>String</code><br /> name of row key
	 * @param superColumn <code>String</code><br /> name of super column
	 * @param column <code>String</code><br /> name of column
	 * 
	 * @return column <code>Column</code>
	 * 
	 * @author Alberto Cequeira
	 * @since 1.0
	 */
	public Column findColumn(String columnFamily, String rowKey, String superColumn, String column);
	
	/**
	 * search for columns
	 * 
	 * @param columnFamily <code>String</code><br /> name of column family
	 * @param rowKey <code>String</code><br /> name of row key
	 * @param columns <code>String...params</code><br /> names of columns
	 * 
	 * @return columns <code>List<Column></code>
	 * 
	 * @author Alberto Cequeira
	 * @since 1.0
	 */
	public List<Column> findColumns(String columnFamily, String rowKey, String...columns);
	
	/**
	 * search for super column
	 * 
	 * @param columnFamily <code>String</code><br /> name of column family
	 * @param rowKey <code>String</code><br /> name of row key
	 * @param superColumn <code>String</code><br /> name of super column
	 * 
	 * @return super column <code>SuperColumn</code>
	 * 
	 * @author Alberto Cequeira
	 * @since 1.0
	 */
	public SuperColumn findSuperColumn(String columnFamily, String rowKey, String superColumn);
	
	/**
	 * search for super columns
	 * 
	 * @param columnFamily <code>String</code><br /> name of column family
	 * @param rowKey <code>String</code><br /> name of row key
	 * @param superColumns <code>String...params</code><br /> names of super columns
	 * 
	 * @return super columns <code>List<SuperColumn></code>
	 * 
	 * @author Alberto Cequeira
	 * @since 1.0
	 */
	public List<SuperColumn> findSuperColumns(String columnFamily, String rowKey, String...superColumns);
	
	/**
	 * search for columns or super columns
	 * 
	 * @param columnFamily <code>String</code><br /> name of column family
	 * @param rowKey <code>String</code><br /> name of row key
	 * 
	 * @return column or super columns <code>List<ColumnOrSuperColumn></code>
	 * 
	 * @author Alberto Cequeira
	 * @since 1.0
	 */
	public List<ColumnOrSuperColumn> findColumnOrSuperColumn(String columnFamily, String rowKey);
}