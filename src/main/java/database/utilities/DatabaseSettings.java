package database.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import database.exceptions.FinderRuntimeException;

public class DatabaseSettings {
	
	static {
		Reflector.loadClass("oracle.jdbc.OracleDriver");
	}
	
	private static final String DB_INFO = "database.properties";
	
	private static final String URL_KEY = "connection.url";
	private static final String USER_KEY = "connection.username";
	private static final String PASS_KEY = "connection.password";
	
	private DatabaseSettings() {
		throw new IllegalStateException("Utility Method");
	}
	
	public static Connection getConnection() {
		Properties p = Reflector.load(DB_INFO, DatabaseSettings::getProps);
		String url = p.getProperty(URL_KEY);
		String user = p.getProperty(USER_KEY);
		String pass = p.getProperty(PASS_KEY);
		
		try {
			return DriverManager.getConnection(url, user, pass);
		}
		catch (SQLException e) {
			String err = "Unable to parse file";
			throw new FinderRuntimeException(err, e);
		}
	}
	
	public static Connection getConnection(File file) {
		try (InputStream in = new FileInputStream(file)) {
			Properties p = new Properties();
			p.load(in);
			
			String url = p.getProperty(URL_KEY);
			String user = p.getProperty(USER_KEY);
			String pass = p.getProperty(PASS_KEY);
			return DriverManager.getConnection(url, user, pass);
		}
		catch (FileNotFoundException e) {
			throw new FinderRuntimeException(e);
		}
		catch (Exception e) {
			String err = "Unable to parse file";
			throw new FinderRuntimeException(err, e);
		}
	}
	
	public static Properties getProps(InputStream in) throws IOException {
		Properties p = new Properties();
		p.load(in);
		return p;
	}
}
