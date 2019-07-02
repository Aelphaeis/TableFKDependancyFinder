package com.cruat.tools.aide.database.driver;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.cruat.tools.aide.database.behaviors.BehaviorFactory;
import com.cruat.tools.aide.database.behaviors.impl.SchemaTableResolver;
import com.cruat.tools.aide.database.utilities.DatabaseSettings;
import com.cruat.tools.aide.database.utilities.Databases;
import com.cruat.tools.aide.database.utilities.QuerySettings;

public class ListTables {
	
	private static final Logger logger = Logger.getLogger(ListTables.class);
	public static final String SCHEMA = QuerySettings.SCHEMA.validate();
	
	public static void main(String ... args) {
	
	}
	
	public static void a() {
		try(Connection connection  = DatabaseSettings.getConnection()){
			logger.info(Databases.DBMS.resolve(connection));
			SchemaTableResolver resolver = new SchemaTableResolver();
			resolver.getAllTables(connection, SCHEMA).forEach(logger::info);
		} catch (SQLException e) {
			logger.error("Error occured with connection", e);
		}
	}
	public static void b() {
		BehaviorFactory factory = new BehaviorFactory();
		factory.getBehavior(SchemaTableResolver.class);
	}
}
