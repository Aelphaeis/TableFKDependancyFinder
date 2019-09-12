package database.behaviors.oracle;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

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
		try(Connection c = DatabaseSettings.getConnection()){
			String tab = tableName.toUpperCase();
			QueryResult r = Databases.query(c, query(), tab);
			return r.column(1, String.class);
		} catch (SQLException e) {
			throw logger.throwing(new FinderRuntimeException(e));
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
	
	@Override
	public DBMS getVendor() {
		return DBMS.ORACLE;
	}
}
