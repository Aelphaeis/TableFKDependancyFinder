package aide.database;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import aide.database.exceptions.FinderRuntimeException;
import aide.database.utilities.DatabaseSettings;
import aide.database.utilities.Databases;
import jmo.db.QueryResult;

public class PrimaryKeyResolver {
	//TODO make this generic for mysql
	private static final Logger logger = LogManager.getLogger();
	public static void main(String... args) {
		try(Connection c = DatabaseSettings.getConnection()){
			QueryResult r = Databases.query(c, query(), "ESS_RESOURCE");
			logger.info("\n{}", () -> r);
		} catch (SQLException e) {
			throw new FinderRuntimeException(e);
		}
	}
	
	public static String query() {
		StringBuilder query = new StringBuilder();
		query.append("SELECT cols.table_name, cols.column_name, cols.position, cons.status, cons.owner ");
		query.append("FROM all_constraints cons, all_cons_columns cols ");
		query.append("WHERE cols.table_name = ? ");
		query.append("AND cons.constraint_type = 'P' ");
		query.append("AND cons.constraint_name = cols.constraint_name ");
		query.append("AND cons.owner = cols.owner ");
		query.append("ORDER BY cols.table_name, cols.position");
		return query.toString();
	}
}
