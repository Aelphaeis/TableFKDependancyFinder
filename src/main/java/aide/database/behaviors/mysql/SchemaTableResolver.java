package aide.database.behaviors.mysql;

import java.sql.Connection;
import java.util.List;
import java.util.stream.Collectors;

import aide.database.behaviors.MySQLBehavior;
import aide.database.behaviors.TableResolver;
import aide.database.utilities.Databases;
import aide.database.utilities.Queries;
import aide.database.utilities.Databases.DBMS;

/**
 * This class gets all the tables from a given schema
 * @author morain
 *
 */
public class SchemaTableResolver extends MySQLBehavior implements TableResolver {
	private static final String TABLE_QUERY = Queries.getQuery("schema_tables");

	@Override
	public List<String> resolve(Connection conn, String schema) {
		return Databases.query(conn, TABLE_QUERY, schema).column(0).stream()
				.map(String.class::cast).collect(Collectors.toList());
	}
	
	@Override
	public DBMS getVendor() {
		return DBMS.MYSQL;
	}

}
