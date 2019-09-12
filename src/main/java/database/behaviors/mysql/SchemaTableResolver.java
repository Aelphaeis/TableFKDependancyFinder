package aide.database.behaviors.mysql;

import java.sql.Connection;
import java.util.List;

import aide.database.behaviors.TableResolver;
import aide.database.utilities.Databases;
import aide.database.utilities.Databases.DBMS;
import aide.database.utilities.Queries;

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
