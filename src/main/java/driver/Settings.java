package driver;

public class Settings {
	
	public static Settings getSettings() {
		Settings s = new Settings();
		s.schema = "";
		s.table = "";
		s.primaryKey = "";
		
		s.connectionString = "";
		s.password = "";
		s.username = "";
		
		return getSettings();
	}
	
	private String schema;
	private String table;
	private String primaryKey;

	private String connectionString;
	private String username;
	private String password;
	
	public String getSchema() {
		return schema;
	}
	public void setSchema(String schema) {
		this.schema = schema;
	}
	public String getTable() {
		return table;
	}
	public void setTable(String table) {
		this.table = table;
	}
	public String getPrimaryKey() {
		return primaryKey;
	}
	public void setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
	}
	public String getConnectionString() {
		return connectionString;
	}
	public void setConnectionString(String connectionString) {
		this.connectionString = connectionString;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
