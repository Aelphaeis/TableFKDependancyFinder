package database.driver;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import database.behaviors.BehaviorFactory;
import database.behaviors.TableResolver;
import database.utilities.DatabaseSettings;
import database.utilities.QuerySettings;

public class ListTables {
	
	private static final Logger logger = LogManager.getLogger();
	public static final String SCHEMA = QuerySettings.SCHEMA.validate();
	
	public static void main(String... args) {
		BehaviorFactory factory = new BehaviorFactory();
		TableResolver r = factory.getBehavior(TableResolver.class);
		try (Connection connection = DatabaseSettings.getConnection()) {
			r.resolve(connection, SCHEMA).forEach(logger::info);
		} catch (SQLException e) {
			logger.error("Error occured with connection", e);
		}
	}
}
