package driver;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import dbms.BehaviorFactory;
import logos.SchemaTableResolver;
import utilities.DatabaseSettings;
import utilities.Databases;
import utilities.QuerySettings;

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
