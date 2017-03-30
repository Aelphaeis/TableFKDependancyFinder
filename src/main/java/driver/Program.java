package driver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import logos.DeleteStatementResolver;
import logos.TableLister;
import structures.TreeNode;
import pojo.TableDependencyInfo;

public class Program {
	
	public static final String schema = "invitely_design";
	public static final String table = "projects";
	public static final String tablePK = "idProjects";

	public static void main(String[] args) throws Exception {
		ListTables();
	}
	
	public static void printDeleteStatements() throws SQLException{
		//You need to specify schema table and primary key for specified table.
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
	
	public static void ListTables() throws SQLException{
		TreeNode<TableDependencyInfo> dependencies;
		Connection conn = getConnection();
		dependencies = DeleteStatementResolver.resolveDependencies(conn, schema, table, tablePK);
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
