package driver;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import logos.SchemaTableResolver;
import utilities.DatabaseSettings;
import utilities.QuerySettings;

public class ListTables {
	
	private static final Logger logger = Logger.getLogger(ListTables.class);
	public static final String SCHEMA = QuerySettings.SCHEMA.validate();
	
	public static void main(String ... args) {
		try(Connection connection  = DatabaseSettings.getConnection()){
			SchemaTableResolver resolver = new SchemaTableResolver();
			resolver.getAllTables(connection, SCHEMA).forEach(logger::info);
		} catch (SQLException e) {
			logger.error("Error occured with connection", e);
		}
	}
}
