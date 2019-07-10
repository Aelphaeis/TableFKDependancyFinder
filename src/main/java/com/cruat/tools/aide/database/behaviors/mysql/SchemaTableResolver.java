package com.cruat.tools.aide.database.behaviors.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.cruat.tools.aide.database.behaviors.MySQLBehavior;
import com.cruat.tools.aide.database.behaviors.TableResolver;
import com.cruat.tools.aide.database.exceptions.FinderRuntimeException;
import com.cruat.tools.aide.database.utilities.Databases.DBMS;
import com.cruat.tools.aide.database.utilities.Queries;

import jmo.db.QueryResult;

/**
 * This class gets all the tables from a given schema
 * @author morain
 *
 */
public class SchemaTableResolver extends MySQLBehavior implements TableResolver {
	private static final Logger logger = LogManager.getLogger();
	private static final String TABLE_QUERY = Queries.getQuery("schema_tables");


	@Override
	public List<String> resolve(Connection conn, String schema) {
		return run(conn, TABLE_QUERY, schema).column(0).stream()
				.map(String.class::cast)
				.collect(Collectors.toList());
	}
	
	public QueryResult run(Connection c, String query, Object... arguments) {
		try(PreparedStatement stmt = c.prepareStatement(query)){
			for(int i = 0; i < arguments.length; i++) {
				stmt.setObject(i + 1, arguments[i]);
			}
			return new QueryResult(stmt.executeQuery());
		} catch (SQLException e) {
			throw new FinderRuntimeException(e);
		}
	}
	
	@Override
	public DBMS getVendor() {
		return DBMS.MYSQL;
	}

}
