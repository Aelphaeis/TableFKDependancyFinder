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
import logos.TableLister;
import pojo.TableDependencyInfo;

public class Program {
	
	private static final Logger logger = Logger.getLogger(Program.class);
	
	//TODO set this information
	public static final String SCHEMA = "";
	public static final String TABLE = "";
	public static final String PK = "";

	public static void main(String[] args) throws Exception {
		listAllTables();
	}
	
	public static void listDependenciesMap() throws SQLException {
		Connection conn = getConnection();
		DependencyMapper dm = new DependencyMapper(conn, SCHEMA, TABLE, PK);
		TreeNode<String> tt = dm.getTableTree();
		logger.info(tt.transverseNodes(new Stringifier<>()));
	}
	
	public static void listAllTables () throws SQLException {
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
		logger.info(resolver);
		
		long duration = System.currentTimeMillis() - start;
		
		String msg = "Resolved dependencies in %s milliseconds";
		logger.info(String.format(msg, duration));
	}
	
	public static void listTables() throws SQLException{
		Connection conn = getConnection();
		TreeNode<TableDependencyInfo> dependencies = DeleteStatementResolver.resolveDependencies(conn, SCHEMA, TABLE, PK);
		TableLister visitor = new TableLister();
		dependencies.transverseNodes(visitor);
		visitor.print();
	}
	
	public static Connection getConnection() throws SQLException {
		//TODO driver manager
		return DriverManager.getConnection(
				"", 
				"",
				"");
	}
}
