package database.behaviors.oracle;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import database.behaviors.KeyResolver;
import database.exceptions.FinderRuntimeException;
import database.utilities.DatabaseSettings;
import database.utilities.Databases;
import database.utilities.Databases.DBMS;
import jmo.db.QueryResult;

public class PrimaryKeyResolver implements KeyResolver {

	private static final Logger logger = LogManager.getLogger();

	@Override
	public List<String> resolve(String schema, String tableName) {
		logger.traceEntry(null, schema, tableName);
		
		//This is because it is very easy to get the schema wrong in oracle
		String resolvedSchema = resolveSchema(schema);
		
		try(Connection c = DatabaseSettings.getConnection()){
			String tab = tableName.toUpperCase();
			QueryResult r = Databases.query(c, query(), tab, resolvedSchema);
			return r.column(1, String.class);
		} catch (SQLException e) {
			throw logger.throwing(new FinderRuntimeException(e));
		}
	}
	
	public static String query() {
		StringBuilder query = new StringBuilder();
		query.append("SELECT cols.table_name, cols.column_name ");
		query.append("FROM all_constraints cons, all_cons_columns cols ");
		query.append("WHERE cols.table_name = ? ");
		query.append("AND cons.constraint_type = 'P' ");
		query.append("AND cons.constraint_name = cols.constraint_name ");
		query.append("AND cons.owner = cols.owner ");
		query.append("AND cons.owner = ? ");
		query.append("ORDER BY cols.table_name, cols.position");
		return query.toString();
	}
	
	private String resolveSchema(String schema) {
		logger.traceEntry(null, schema);
		
		String arg = Objects.nonNull(schema) ? schema.toUpperCase() : schema;
		String schemaQuery = "select USERNAME from SYS.ALL_USERS";
		try (Connection c = DatabaseSettings.getConnection()) {
			QueryResult r = Databases.query(c, schemaQuery);
			if (r.column(0, String.class).contains(arg)) {
				//the schema provided by the user is correct.
				return logger.traceExit(schema);
			} else {
				String ns = "userenv"; // name space
				String p = "current_schema"; // parameter
				String q = "select sys_context(?, ?) from dual";
				QueryResult result = Databases.query(c, q, ns, p);
				String resolved = result.value(0, 0, String.class);
				logger.warn("Schema {} not found, using {}", schema, resolved);
				return logger.traceExit(resolved);
			}
		} catch (SQLException e) {
			throw logger.throwing(new FinderRuntimeException(e));
		}
	}
	
	@Override
	public DBMS getVendor() {
		return DBMS.ORACLE;
	}
}
