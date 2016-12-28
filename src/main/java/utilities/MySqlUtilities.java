package utilities;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import structures.TreeNode;
import pojo.TableDependencyInfo;

import com.mysql.jdbc.PreparedStatement;
import org.apache.log4j.Logger;

public class MySqlUtilities {
	String query;
	private static final Logger logger = Logger.getLogger(MySqlUtilities.class) ;
	
	public MySqlUtilities(){
		logger.trace("Starting instantiation of MySqlTableDependencies object");
		
		query = "";
		query += " select ";
		query += "	TABLE_NAME,COLUMN_NAME,CONSTRAINT_NAME, REFERENCED_TABLE_NAME,REFERENCED_COLUMN_NAME, CONSTRAINT_SCHEMA";
		query += " from INFORMATION_SCHEMA.KEY_COLUMN_USAGE ";
		query += " where ";
		query += "   CONSTRAINT_SCHEMA = coalesce( ? , CONSTRAINT_SCHEMA)";
		query += " and ";
		query += "   REFERENCED_TABLE_NAME = ? ";
		
		logger.trace("Instantiation of MySqlTableDependencies complete");
	}
	
	public List<TableDependencyInfo> listTableChildren(Connection connection, String schemaName, String tableName) throws SQLException{
		logger.trace("START : ListTableChildren");
		logger.debug("Finding information for " + schemaName + "." + tableName);
		try{
			logger.trace("Preparing Statement");
			PreparedStatement stmt = (PreparedStatement) connection.prepareStatement(query);
			
			logger.trace("Setting variables in prepared statement");
			stmt.setString(1, schemaName);
			stmt.setString(2, tableName);
			
			logger.trace("Executing prepared statement");
			ResultSet results = stmt.executeQuery();
			List<TableDependencyInfo> tableDependencies = new ArrayList<TableDependencyInfo>();
			
			logger.trace("Storing results into array");
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
			logger.debug("information query " + schemaName + "." + tableName + " is complete");
			logger.trace("END : ListTableChildren");
			return tableDependencies;
		}
		catch(Exception e){
			logger.error(e.getMessage(), e);
			throw e;
		}
	}
	
	public TreeNode<TableDependencyInfo> listChildHierarchy(Connection connection, String schema, String tableName) throws SQLException{
		logger.trace("START : listChildHierarchy");

		logger.debug("Computing table foreign key dependencies for " + schema + "." + tableName);
		TreeNode<TableDependencyInfo> tree = new TreeNode<TableDependencyInfo>();
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

	private TreeNode<TableDependencyInfo> listChildHierarchy(Connection connection, TreeNode<TableDependencyInfo> n) throws SQLException{
		logger.trace("START : listChildHierarchy");
		TableDependencyInfo info = n.getValue();
		
		logger.debug("Computing table foreign key dependencies for " + info.getConstraintSchema() + "." + info.getTableName());
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
}
