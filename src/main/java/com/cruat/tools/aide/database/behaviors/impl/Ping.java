package com.cruat.tools.aide.database.behaviors;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

import org.apache.log4j.Logger;

public class Ping implements AutoCloseable {
	
	private static final Logger logger = Logger.getLogger(Ping.class);
	
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
	public void close() throws Exception {
		connection.close();
	}
	
}
