package driver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import logos.DeleteStatementResolver;
import structures.TreeNode;
import pojo.TableDependencyInfo;

public class Program {

	public static void main(String[] args) throws Exception {
		printDeleteStatements();
	}
	
	public static void printDeleteStatements() throws SQLException{
		//You need to specify schema table and primary key for specified table.
		String schema = "";
		String table = "";
		String tablePK = "";
		Connection conn = getConnection();
		
		long start = System.currentTimeMillis();
		TreeNode<TableDependencyInfo> dependencies;
		dependencies = DeleteStatementResolver.resolveDependencies(conn, schema, table, tablePK);
		DeleteStatementResolver resolver = new DeleteStatementResolver();
		dependencies.transverseNodes(resolver);
		System.out.println(resolver);
		
		long duration = System.currentTimeMillis() - start;
		
		String msg = "Resolved dependencies in %s milliseconds";
		System.out.println(String.format(msg, duration));
	}
	
	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(
				"jdbc:mysql://localhost:3306/prod_prime", 
				"root",
				"wadetech");
	}
}
