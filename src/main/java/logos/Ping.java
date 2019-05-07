package logos;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

public class Ping implements AutoCloseable {
	private final Connection connection;
	
	public Ping(Connection c) {
		this.connection = Objects.requireNonNull(c);
	}
	

	public void ping() throws SQLException {
		try(Statement s = connection.createStatement()){
			s.executeQuery("select 1 from dual");
		}
	}


	@Override
	public void close() throws Exception {
		connection.close();
	}
	
}
