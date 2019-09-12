package database.behaviors.mysql;

import java.sql.Connection;
import java.util.List;

import database.behaviors.TableResolver;
import database.utilities.Databases;
import database.utilities.Queries;
import database.utilities.Databases.DBMS;

/**
 * This class gets all the tables from a given schema
 * @author morain
 *
 */
public class SchemaTableResolver implements TableResolver {
	private static final String QUERY = Queries.getQuery("schema_tables");

	@Override
	public List<String> resolve(Connection c, String schema) {
		return Databases.query(c, QUERY, schema).column(0, String.class);
	}
	
	@Override
	public DBMS getVendor() {
		return DBMS.MYSQL;
	}
}
