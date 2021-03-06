package database.utilities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import database.pojo.TableDependencyInfo;
import jmo.structures.TreeNode;

public class MySqlUtils {
	
	private static final Logger logger = LogManager.getLogger();
	private static String query = Queries.getQuery("list_dependents");
	
	public static List<TableDependencyInfo> listTableChildren(Connection connection, String schemaName, String tableName) throws SQLException{
		logger.trace("START : ListTableChildren");
		logger.debug("Finding information for {}.{}", schemaName, tableName);
		PreparedStatement stmt = null;
		try{
			logger.trace("Preparing Statement");
			stmt = connection.prepareStatement(query);
			
			logger.trace("Setting variables in prepared statement");
			stmt.setString(1, schemaName);
			stmt.setString(2, tableName);
			
			logger.trace("Executing prepared statement");
			ResultSet results = stmt.executeQuery();
			List<TableDependencyInfo> tableDependencies = new ArrayList<>();
			
			logger.trace("Storing results into list");
			while(results.next()){
				TableDependencyInfo tableChild = new TableDependencyInfo();
				tableChild.setTableName(results.getString("TABLE_NAME"));
				tableChild.setColumnName(results.getString("COLUMN_NAME"));
				tableChild.setConstantName(results.getString("CONSTRAINT_NAME"));
				tableChild.setReferencedTableName(results.getString("REFERENCED_TABLE_NAME"));
				tableChild.setReferencedColumnName(results.getString("REFERENCED_COLUMN_NAME"));
				tableChild.setConstraintSchema(results.getString("CONSTRAINT_SCHEMA"));
				tableDependencies.add(tableChild);
			}
			logger.debug("information query {}.{} is complete", schemaName, tableName);
			logger.trace("END : ListTableChildren");
			return tableDependencies;
		}
		catch(Exception e){
			logger.error(e.getMessage(), e);
			throw e;
		}
		finally {
			if(stmt != null) {
				stmt.close();
			}
		}
	}
	
	public static TreeNode<TableDependencyInfo> listChildHierarchy(Connection connection, String schema, String tableName) throws SQLException{
		logger.trace("START : listChildHierarchy");

		logger.debug("Computing table foreign key dependencies for {}.{}", schema, tableName);
		TreeNode<TableDependencyInfo> tree = new TreeNode<>();
		tree.addChildren(listTableChildren(connection, schema, tableName));
		
		logger.trace("Computing table foreign key dependencides for children");
		for(TreeNode<TableDependencyInfo> child : tree.getChildren()){
			if(!child.getValue().getTableName().equals(tableName)){
				listChildHierarchy(connection, child);
			}
		}
		logger.debug("END : listChildHierarchy");
		return tree;
	}

	private static TreeNode<TableDependencyInfo> listChildHierarchy(Connection connection, TreeNode<TableDependencyInfo> n) throws SQLException{
		logger.trace("START : listChildHierarchy");
		TableDependencyInfo info = n.getValue();
		
		logger.debug("Computing table foreign key dependencies for {}.{}",
				info::getConstraintSchema,
				info::getTableName);
		n.addChildren(listTableChildren(connection, info.getConstraintSchema(), info.getTableName()));
		
		logger.trace("Computing table foreign key dependencides for children");
		for(TreeNode<TableDependencyInfo> child : n.getChildren()){
			if(!child.getValue().getTableName().equals(info.getReferencedTableName())){
				listChildHierarchy(connection, child);
			}
		}
		
		logger.trace("END : listChildHierarchy");
		return n;
	}
	
	private MySqlUtils(){ }
	
}
