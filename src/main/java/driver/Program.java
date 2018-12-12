package driver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import jmo.patterns.visitor.Stringifier;
import jmo.structures.TreeNode;
import logos.DeleteStatementResolver;
import logos.DependencyMapper;
import logos.SchemaTableResolver;
import pojo.TableDependencyInfo;

public class Program {
	
	private static final Logger logger = Logger.getLogger(Program.class);
	private static final Settings SETTINGS = Settings.getSettings();
	
	public static final String SCHEMA = SETTINGS.getSchema();
	public static final String TABLE = SETTINGS.getTable();
	public static final String PK = SETTINGS.getPrimaryKey();

	
	public static void main(String[] args) throws Exception {
		listDependenciesMap();
	}
	
	public static void listDependenciesMap() throws SQLException {
		Connection conn = getConnection();
		DependencyMapper dm = new DependencyMapper(conn, SCHEMA, TABLE, PK);
		TreeNode<String> tt = dm.getTableTree();
		logger.info(tt.transverseNodes(new Stringifier<>()));
	}
	
	public static void listAllTables() throws SQLException {
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
	
	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(
				SETTINGS.getConnectionString(), 
				SETTINGS.getUsername(),
				SETTINGS.getPassword());
	}
}
