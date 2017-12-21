package driver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import jmo.structures.TreeNode;
import logos.DeleteStatementResolver;
import logos.TableLister;
import pojo.TableDependencyInfo;

public class Program {
	
	private static final Logger logger = Logger.getLogger(Program.class);
	
	public static final String SCHEMA = "";
	public static final String TABLE = "";
	public static final String PK = "";

	public static void main(String[] args) throws Exception {
		printDeleteStatements();
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
		return DriverManager.getConnection(
				"jdbc:mysql://localhost:3306/prod_prime", 
				"root",
				"wadetech");
	}
}
