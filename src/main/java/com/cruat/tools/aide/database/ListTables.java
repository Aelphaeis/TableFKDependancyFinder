package com.cruat.tools.aide.database;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.cruat.tools.aide.database.behaviors.BehaviorFactory;
import com.cruat.tools.aide.database.behaviors.mysql.SchemaTableResolver;
import com.cruat.tools.aide.database.utilities.DatabaseSettings;
import com.cruat.tools.aide.database.utilities.QuerySettings;

public class ListTables {
	
	private static final Logger logger = LogManager.getLogger();
	public static final String SCHEMA = QuerySettings.SCHEMA.validate();
	
	public static void main(String... args) {
		BehaviorFactory factory = new BehaviorFactory();
		SchemaTableResolver r = factory.getBehavior(SchemaTableResolver.class);
		try (Connection connection = DatabaseSettings.getConnection()) {
			r.resolve(connection, SCHEMA).forEach(logger::info);
		} catch (SQLException e) {
			logger.error("Error occured with connection", e);
		}
	}
}
