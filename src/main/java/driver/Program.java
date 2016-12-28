package driver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Stack;

import logos.DeleteStatementResolver;
import structures.TreeNode;
import pojo.TableDependencyInfo;
import utilities.MySqlUtils;

public class Program {

	public static void main(String[] args) throws Exception {
		
		long start = System.currentTimeMillis();
		TreeNode<TableDependencyInfo> dependencies;
		dependencies = DeleteStatementResolver.resolveDependencies(getConnection(), "local_prime", "Company", "idCompany");
		DeleteStatementResolver resolver = new DeleteStatementResolver();
		dependencies.transverseNodes(resolver);
		System.out.println(resolver);
		
		long duration = System.currentTimeMillis() - start;
		
		String msg = "Resolved dependencies in %s milliseconds";
		System.out.println(String.format(msg, duration));
	}


	public static void ShowHierarchy(String table, String schema, String idColumn) throws SQLException {
		TableDependencyInfo root = new TableDependencyInfo();
		TreeNode<TableDependencyInfo> info = MySqlUtils.listChildHierarchy(getConnection(), schema, table);

		root.setReferencedColumnName(idColumn);
		root.setReferencedTableName(table);
		root.setConstraintSchema(schema);
		root.setColumnName(idColumn);
		root.setTableName(table);
		info.setValue(root);

		Stack<String> out = new Stack<String>();

//		info.transverse(new Callable() {
//			@SuppressWarnings("unchecked")
//			@Override
//			public void function(Object... args) {
//				TreeNode<TableDependencyInfo> n = (TreeNode<TableDependencyInfo>) args[1];
//				StringBuilder sb = new StringBuilder("");
//				for(int i = n.getLevel(); i > 1; i--){
//					sb.append("   ");
//				}
//				sb.append("|--- " + n.getValue().getTableName());
//				out.push(sb.toString());
//			}
//
//		});
		
		for(String s : out){
			System.out.println(s);
		}
	}

	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(
				"jdbc:mysql://localhost:3306/prod_prime", 
				"root",
				"wadetech");
	}
}
