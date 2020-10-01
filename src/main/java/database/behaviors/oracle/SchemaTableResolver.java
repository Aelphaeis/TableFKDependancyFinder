package database.behaviors.oracle;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import database.behaviors.TableResolver;
import database.exceptions.FinderRuntimeException;
import database.utilities.Databases;
import database.utilities.Queries;
import database.utilities.Databases.DBMS;

public class SchemaTableResolver implements TableResolver {
	private static final String QUERY_NAME ="oracle_schema_tables";
	private static final String QUERY = Queries.getQuery(QUERY_NAME);
	private static final Logger logger = LogManager.getLogger();
	
	@Override
	public List<String> resolve(Connection c, String schema) {
		logger.traceEntry(null, c, schema);
		String username = resolveUsername(c);
		return Databases.query(c, QUERY, username).column(0, String.class);
		
	}
	
	private static String resolveUsername(Connection c) {
		try {
			return logger.traceExit(c.getMetaData().getUserName());
		} catch (SQLException e) {
			String err = "Cannot resolve username";
			throw logger.throwing(new FinderRuntimeException(err));
		}
	}

	@Override
	public DBMS getVendor() {
		return DBMS.ORACLE;
	}
}
