package logos;

import java.sql.Connection;
import java.sql.SQLException;

import jmo.structures.TreeNode;
import pojo.TableDependencyInfo;
import static logos.DeleteStatementResolver.resolveDependencies;

public class DependencyMapper {

	private Connection connection;
	private String primaryKey;
	private String schema;
	private String table;
	
	public DependencyMapper(Connection c, String s, String t, String pk) {
		setConnection(c);
		setSchema(s);
		setTable(t);
		setPrimaryKey(pk);
	}
	
	
	private TreeNode<TableDependencyInfo> resolve() throws SQLException{
		return resolveDependencies(connection, schema, table, primaryKey);
	}
	
	public TreeNode<String> getTableTree(){
		try {
			TreeNode<TableDependencyInfo> dependencies =  resolve();
			return dependencies.map(TableDependencyInfo::getTableName);
		}
		catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	protected void setConnection(Connection connection) {
		this.connection = connection;
	}

	protected void setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
	}

	protected void setSchema(String schema) {
		this.schema = schema;
	}

	protected void setTable(String table) {
		this.table = table;
	}
}