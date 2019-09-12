package database.behaviors.mysql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import database.behaviors.KeyResolver;
import database.behaviors.MySQLBehavior;
import database.exceptions.FinderRuntimeException;
import database.utilities.DatabaseSettings;
import database.utilities.Databases;
import jmo.db.QueryResult;

public class PrimaryKeyResolver extends MySQLBehavior implements KeyResolver {
	
	private static final Logger logger = LogManager.getLogger();

	@Override
	public List<String> resolve(String schema, String tableName) {
		logger.traceEntry(null, schema, tableName);
		try(Connection c = DatabaseSettings.getConnection()){
			String tab = tableName.toUpperCase();
			QueryResult r = Databases.query(c, query(), tab, schema);
			return logger.traceExit(r.column(0, String.class));
		} catch (SQLException e) {
			throw logger.throwing(new FinderRuntimeException(e));
		}
	}
	
	public String query() {
		StringBuilder  query = new StringBuilder();
		query.append("SELECT k.column_name ");
		query.append("FROM information_schema.table_constraints t ");
		query.append("JOIN information_schema.key_column_usage k ");
		query.append("USING (constraint_name, table_schema, table_name) ");
		query.append("WHERE t.constraint_type='PRIMARY KEY' ");
		query.append("  AND t.table_name= ? AND t.constraint_schema = ?");
		return query.toString();
	}
	
}
