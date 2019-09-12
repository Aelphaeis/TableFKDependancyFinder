package aide.database.driver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import aide.database.behaviors.BehaviorFactory;
import aide.database.behaviors.KeyResolver;
import aide.database.utilities.QuerySettings;

public class PrimaryKeyResolver {
	private static final Logger logger = LogManager.getLogger();
	public static void main(String... args) {
		BehaviorFactory factory = new BehaviorFactory();
		KeyResolver kr = factory.getBehavior(KeyResolver.class);
		String schema = QuerySettings.SCHEMA.validate();
		String table = QuerySettings.TABLE.validate();
		kr.resolve(schema, table).forEach(logger::info);
	}
}
