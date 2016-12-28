package driver;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Stack;

import jmo.patterns.visitor.Visitor;
import jmo.structures.TreeNode;

public class DeleteStatementResolver implements Visitor<TreeNode<TableDependencyInfo>>{
	
	public static TreeNode<TableDependencyInfo> resolveDependencies(Connection connection, String schema, String table, String idColumn) throws SQLException{
		//Get dependency information
		TableDependencyInfo root = new TableDependencyInfo();
		MySqlTableDependencies helper = new MySqlTableDependencies();
		TreeNode<TableDependencyInfo> info = helper.listChildHierarchy(connection, schema, table);
		

		// Fill the root with the correct information
		root.setReferencedColumnName(idColumn);
		root.setReferencedTableName(table);
		root.setConstraintSchema(schema);
		root.setColumnName(idColumn);
		root.setTableName(table);
		info.setValue(root);

		//return entire tree
		return info;
	}
	
	private static final String KEY = "@id";
	Stack<String> out;
	
	public DeleteStatementResolver() {
		out = new Stack<String>();
	}

	@Override
	public void visit(TreeNode<TableDependencyInfo> element) {
		TreeNode<TableDependencyInfo> n = element;
		Stack<String> statements  = new Stack<String>();
		char placeHolder = 'a';
		String ref = "";
		for(int c = 0; n != null; c++, placeHolder++){
			TableDependencyInfo value = n.getValue();
			String sql = "";
			
			// we are deleting this node.
			if (c == 0) {
				sql += "delete " + placeHolder + " from ";
			} 
			else {
				sql += "select " + placeHolder + "." + ref + " from ";
			}
			String schema = value.getConstraintSchema();
			sql += ((schema != null) ? schema + "." : "") + value.getTableName() + " " + placeHolder;
			sql += " where " + placeHolder + "." + value.getColumnName();

			if (!n.hasParent()) {
				sql += " = " + KEY;
			} else {
				sql += " in (%s)";
			}
			statements.push(sql);
			ref = value.getReferencedColumnName();
			n = n.getParent();
		}
		
		String sql = "";
		for(String fragment  : statements){
			if(sql.isEmpty()){
				sql = fragment;
			}
			else{
				sql = String.format(sql, fragment);
			}
		}
		out.push(sql);
	}
	
	@Override
	public String toString(){
		StringBuilder builder = new StringBuilder();
		while(!out.isEmpty()){
			builder.append(out.pop());
			builder.append(";");
			builder.append("\n");
		}
		return builder.toString();
	}
}
