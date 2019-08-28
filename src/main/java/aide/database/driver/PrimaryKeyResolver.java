package aide.database.driver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import aide.database.behaviors.BehaviorFactory;
import aide.database.behaviors.KeyResolver;
import aide.database.utilities.QuerySettings;

public class PrimaryKeyResolver {
	//TODO make this generic for mysql
	private static final Logger logger = LogManager.getLogger();
	public static void main(String... args) {
		BehaviorFactory factory = new BehaviorFactory();
		KeyResolver kr = factory.getBehavior(KeyResolver.class);
		kr.resolve(QuerySettings.TABLE.validate()).forEach(logger::info);
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
