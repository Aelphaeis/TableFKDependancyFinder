package aide.database;

import java.sql.Connection;
import java.sql.SQLException;

import aide.database.exceptions.FinderRuntimeException;
import aide.database.utilities.DatabaseSettings;
import aide.database.utilities.Databases;
import jmo.db.QueryResult;

public class PrimaryKeyResolver {
	//TODO make this generic for mysql
	public static void main(String... args) {
		try(Connection c = DatabaseSettings.getConnection()){
			QueryResult r = Databases.query(c, query(), "ESS_RESOURCE");
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
