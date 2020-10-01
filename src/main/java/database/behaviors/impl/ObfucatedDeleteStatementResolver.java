package database.behaviors.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import database.pojo.TableDependencyInfo;
import database.utilities.MySqlUtils;
import jmo.patterns.visitor.Visitor;
import jmo.structures.TreeNode;
import jmo.util.Randomizer;

public class ObfucatedDeleteStatementResolver implements Visitor<TreeNode<TableDependencyInfo>>{
	
	public static TreeNode<TableDependencyInfo> resolveDependencies(Connection connection, String schema, String table, String idColumn) throws SQLException{
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
	
	private static final Logger logger = LogManager.getLogger();
	private static final String KEY = "@id";
	Map<String, String> obfuscated;
	Randomizer randomizer;
	Deque<String> out;
	int visitedNodeCount = 0;
	
	public ObfucatedDeleteStatementResolver() {
		randomizer = new Randomizer();
		obfuscated = new HashMap<>();
		out = new ArrayDeque<>();
	}

	@Override
	public void visit(TreeNode<TableDependencyInfo> element) {
		logger.debug("Visiting node {}", ++visitedNodeCount);
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
			
			String schema = addObfuscationIfNotExist(value.getConstraintSchema());
			String table = addObfuscationIfNotExist(value.getTableName());
			String col = addObfuscationIfNotExist(value.getColumnName());
			
			sql += ((schema != null) ? schema + "." : "") + table + " " + placeHolder;
			sql += " where " + placeHolder + "." + col;

			if (!n.hasParent()) {
				sql += " = " + KEY;
			} else {
				sql += " in (%s)";
			}
			statements.push(sql);
			ref = addObfuscationIfNotExist(value.getReferencedColumnName());
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
	
	protected String addObfuscationIfNotExist(String key){
		if(key == null){
			return null;
		}
		
		if(!obfuscated.containsKey(key)){
			String value;
			do{
				value = randomizer.getString(8).replaceAll("\\d", "");
			}
			while(obfuscated.containsValue(value));
			obfuscated.put(key, value);
			return value;
		}
		return obfuscated.get(key);
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