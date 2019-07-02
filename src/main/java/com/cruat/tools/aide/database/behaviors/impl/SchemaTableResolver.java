package com.cruat.tools.aide.database.behaviors;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.cruat.tools.aide.database.exceptions.FinderRuntimeException;
import com.cruat.tools.aide.database.utilities.Queries;
import com.cruat.tools.aide.database.utilities.Databases.DBMS;

/**
 * This class gets all the tables from a given schema
 * @author morain
 *
 */
public class SchemaTableResolver implements com.cruat.tools.aide.database.behaviors.factory.Behavior {
	private static final Logger logger = LogManager.getLogger(SchemaTableResolver.class);
	private static final String TABLE_QUERY = Queries.getQuery("schema_tables");

	public List<String> getAllTables(Connection conn, String schema){
		long start = System.currentTimeMillis();
		logger.trace("Preparing Statement");
		try (PreparedStatement stmt = conn.prepareStatement(TABLE_QUERY)){
			
			logger.trace("Setting variables for prepared statement");
			stmt.setString(1, schema);
			
			logger.trace("executing prepared statement");
			return executeQuery(stmt);
		} catch (SQLException e) {
			String err = "Unable to get table list";
			logger.error(err, e);
			throw new FinderRuntimeException(err, e);
		}
		finally {
			long end = System.currentTimeMillis();
			String msg = "table retrieval finished in %s ms";
			logger.trace(String.format(msg, end - start));
		}
	}
	
	private static List<String> executeQuery(PreparedStatement stmt)
			throws SQLException {
		List<String> tables = new ArrayList<>();
		try (ResultSet results = stmt.executeQuery()) {
			while (results.next()) {
				tables.add(results.getString("TABLE_NAME"));
			}
		}
		return tables;
	}

	@Override
	public DBMS getVendor() {
		return DBMS.MYSQL;
	}

}
