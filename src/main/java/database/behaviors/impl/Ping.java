package database.behaviors.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Ping implements AutoCloseable {
	
	private static final Logger logger = LogManager.getLogger();
	
	private final Connection connection;
	
	public Ping(Connection c) {
		this.connection = Objects.requireNonNull(c);
	}
	
	public void ping() throws SQLException {
		try(Statement s = connection.createStatement()){
			s.executeQuery("select 1 from dual");
		}
		logger.info("successfully pinged server");
	}

	@Override
	public void close() throws SQLException{
		connection.close();
	}
	
}
