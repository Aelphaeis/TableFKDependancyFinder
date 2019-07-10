package com.cruat.tools.aide.database.behaviors.impl;

import static com.cruat.tools.aide.database.behaviors.impl.DeleteStatementResolver.resolveDependencies;

import java.sql.Connection;
import java.sql.SQLException;

import com.cruat.tools.aide.database.exceptions.FinderRuntimeException;
import com.cruat.tools.aide.database.pojo.TableDependencyInfo;

import jmo.structures.TreeNode;

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
			throw new FinderRuntimeException(e);
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
