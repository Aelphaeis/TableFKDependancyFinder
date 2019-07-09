package com.cruat.tools.aide.database.behaviors.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

import com.cruat.tools.aide.database.behaviors.mysql.SchemaTableResolver;
import com.cruat.tools.aide.database.pojo.TableDependencyInfo;
import com.cruat.tools.aide.database.utilities.MySqlUtils;

import jmo.patterns.visitor.Visitor;
import jmo.structures.TreeNode;

/**
 * This class implements the visitor pattern to transverse a {@link TreeNode}
 * and creates a delete statement
 * 
 * @author joseph.morain
 *
 */
public class DeleteStatementResolver implements Visitor<TreeNode<TableDependencyInfo>>{
	
	public static TreeNode<TableDependencyInfo> resolveDependencies(Connection connection, String schema, String table, String idColumn) throws SQLException{
		
		SchemaTableResolver tableResolver = new SchemaTableResolver();
		List<String> tables = tableResolver.getAllTables(connection, schema);

		boolean existing = tables.stream()
			.anyMatch(p -> p.equalsIgnoreCase(table));
		
		if(!existing) {
			String err = "[%s] is not a valid table";
			throw new IllegalArgumentException(String.format(err, table));
		}
		
		//Get dependency information
		TableDependencyInfo root = new TableDependencyInfo();
		TreeNode<TableDependencyInfo> info = MySqlUtils.listChildHierarchy(connection, schema, table);
		

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
	Deque<String> out;
	
	public DeleteStatementResolver() {
		out = new ArrayDeque<>();
	}

	@Override
	public void visit(TreeNode<TableDependencyInfo> element) {
		TreeNode<TableDependencyInfo> n = element;
		Deque<String> statements  = new ArrayDeque<>();
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
		while(statements.peekLast() != null) {
			String fragment = statements.pollLast();
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
