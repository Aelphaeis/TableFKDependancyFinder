package com.cruat.tools.aide.database.driver;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.cruat.tools.aide.database.behaviors.impl.DeleteStatementResolver;
import com.cruat.tools.aide.database.behaviors.impl.DependencyMapper;
import com.cruat.tools.aide.database.behaviors.mysql.SchemaTableResolver;
import com.cruat.tools.aide.database.pojo.TableDependencyInfo;
import com.cruat.tools.aide.database.utilities.DatabaseSettings;
import com.cruat.tools.aide.database.utilities.QuerySettings;

import jmo.patterns.visitor.Stringifier;
import jmo.structures.TreeNode;

public class Program {
	
	private static final Logger logger = LogManager.getLogger();
	
	public static final String PK = QuerySettings.PRIMARY_KEY.getValue();
	public static final String SCHEMA = QuerySettings.SCHEMA.getValue();
	public static final String TABLE = QuerySettings.TABLE.getValue();

	
	public static void main(String... args) {
		try(Connection connection  = DatabaseSettings.getConnection()){
			connection.getMetaData();
		} catch (SQLException e) {
			logger.error("Error occured with connection", e);
		}
	}
	
	public static void listDependenciesMap() {
		Connection conn = getConnection();
		DependencyMapper dm = new DependencyMapper(conn, SCHEMA, TABLE, PK);
		TreeNode<String> tt = dm.getTableTree();
		logger.info(tt.transverseNodes(new Stringifier<>()));
	}
	
	public static void listAllTables()  {
		new SchemaTableResolver()
			.getAllTables(getConnection(), SCHEMA)
			.forEach(logger::info);
	}
	
	public static void printDeleteStatements() throws SQLException{
		//You need to specify schema table and primary key for specified table.
		Connection conn = getConnection();
		
		long start = System.currentTimeMillis();
		TreeNode<TableDependencyInfo> dependencies = DeleteStatementResolver.resolveDependencies(conn, SCHEMA, TABLE, PK);
		DeleteStatementResolver resolver = new DeleteStatementResolver();
		dependencies.transverseNodes(resolver);
		logger.info("\n" + resolver);
		
		long duration = System.currentTimeMillis() - start;
		
		String msg = "Resolved dependencies in %s milliseconds";
		logger.info(String.format(msg, duration));
	}
	
	public static Connection getConnection() {
		return DatabaseSettings.getConnection();
	}
}
