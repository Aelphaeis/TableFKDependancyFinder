package database.utilities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import database.exceptions.FinderRuntimeException;
import jmo.db.QueryResult;

public class Databases {
	private static final Logger logger = LogManager.getLogger();
	private static final String DMBS_REGEX = "jdbc:(.*?):.*";
	private static final Pattern DBMS_PATTERN = Pattern.compile(DMBS_REGEX);
	
	public static QueryResult query(Connection c, String query, Object...args) {
		logger.traceEntry(() -> c, () -> query, () -> Arrays.toString(args));
		try(PreparedStatement stmt = c.prepareStatement(query)){
			for(int i = 0; i < args.length; i++) {
				stmt.setObject(i + 1, args[i]);
			}
			return new QueryResult(stmt.executeQuery());
		} catch (SQLException e) {
			throw new FinderRuntimeException(e);
		}
	}
	
	/**
	 * Represents different DataBase Management Systems
	 * 
	 * @author morain
	 */
	public enum DBMS {
		MYSQL, ORACLE;

		private static final Logger logger = LogManager.getLogger();
		public static DBMS resolve(Connection connection) {
			logger.traceEntry(null, connection);
			try {
				String url = connection.getMetaData().getURL();
				Matcher matcher = DBMS_PATTERN.matcher(url);
				if (matcher.find()) {
					String result = matcher.group(1);
					for (DBMS current : DBMS.values()) {
						if (current.name().equalsIgnoreCase(result)) {
							return logger.traceExit(current);
						}
					}
				}
				String err = "Unable to resolve DBMS. URL:[" + url + "]";
				throw logger.throwing(new UnknownDBMSException(err));
			} catch (SQLException e) {
				String err = "Unable to resolve DBMS";
				throw logger.throwing(new UnknownDBMSException(err, e));
			}
		}
	}
	
	public static class UnknownDBMSException extends FinderRuntimeException {
		private static final long serialVersionUID = 1L;
		
		public UnknownDBMSException(String message, Throwable cause) {
			super(message, cause);
		}
		
		public UnknownDBMSException(String message) {
			super(message);
		}
	}
}
