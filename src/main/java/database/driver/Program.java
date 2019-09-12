package database.driver;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import database.behaviors.impl.DeleteStatementResolver;
import database.behaviors.impl.DependencyMapper;
import database.behaviors.mysql.SchemaTableResolver;
import database.pojo.TableDependencyInfo;
import database.utilities.DatabaseSettings;
import database.utilities.QuerySettings;
import jmo.patterns.visitor.Stringifier;
import jmo.structures.TreeNode;

public class Program {
	
	private static final Logger logger = LogManager.getLogger();
	
	public static final String PK = QuerySettings.PRIMARY_KEY.getValue();
	public static final String SCHEMA = QuerySettings.SCHEMA.getValue();
	public static final String TABLE = QuerySettings.TABLE.getValue();

	
	public static void main(String... args) {
		printDeleteStatements();
	}
	
	public static void listDependenciesMap() {
		Connection conn = getConnection();
		DependencyMapper dm = new DependencyMapper(conn, SCHEMA, TABLE, PK);
		TreeNode<String> tt = dm.getTableTree();
		logger.info(tt.transverseNodes(new Stringifier<>()));
	}
	
	public static void listAllTables()  {
		new SchemaTableResolver()
			.resolve(getConnection(), SCHEMA)
			.forEach(logger::info);
	}
	
	public static void printDeleteStatements() {
		//You need to specify schema table and primary key for specified table.
		try {
			Connection conn = getConnection();
			
			long start = System.currentTimeMillis();
			TreeNode<TableDependencyInfo> dependencies = DeleteStatementResolver.resolveDependencies(conn, SCHEMA, TABLE, PK);
			DeleteStatementResolver resolver = new DeleteStatementResolver();
			dependencies.transverseNodes(resolver);
			logger.info("\n" + resolver);
			
			long duration = System.currentTimeMillis() - start;
			
			String msg = "Resolved dependencies in %s milliseconds";
			logger.info(String.format(msg, duration));
		} catch(SQLException e) {
			logger.error("unable to resolve delete statements", e);
		}
	}
	
	public static Connection getConnection() {
		return DatabaseSettings.getConnection();
	}
}
