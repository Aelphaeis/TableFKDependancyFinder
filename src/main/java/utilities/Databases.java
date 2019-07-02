package utilities;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import exceptions.FinderRuntimeException;

public class Databases {
	private static final String DMBS_REGEX = "jdbc:(.*?):.*";
	private static final Pattern DBMS_PATTERN = Pattern.compile(DMBS_REGEX);
	
	/**
	 * Represents different DataBase Management Systems
	 * 
	 * @author morain
	 */
	public enum DBMS {
		MYSQL, ORACLE;
	}
	
	public static DBMS resolveDBMS(Connection connection) {
		try {
			String url = connection.getMetaData().getURL();
			Matcher matcher = DBMS_PATTERN.matcher(url);
			if (matcher.find()) {
				String result = matcher.group(1);
				for (DBMS current : DBMS.values()) {
					if (current.name().equalsIgnoreCase(result)) {
						return current;
					}
				}
			}
			String err = "Unable to resolve DBMS. URL:[%s]";
			throw new UnknownDBMSException(String.format(err, url));
		} catch (SQLException e) {
			String err = "Unable to resolve DBMS";
			throw new UnknownDBMSException(err, e);
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
