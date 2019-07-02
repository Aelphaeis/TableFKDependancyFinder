package utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

import exceptions.FinderRuntimeException;

public enum QuerySettings {
	SCHEMA("schema"),
	TABLE("table"),
	PRIMARY_KEY("primary.key");
	
	
	private static final String RESOURCE_FOLDER = "src/main/resources/";
	private static final String SETTINGS_FILE = "database.properties";

	private final String name;
	
	private QuerySettings(String propertyName) {
		this.name = propertyName;
	}
	
	public String getName() {
		return name;
	}
	
	public String getValue() {
		File f = new File(RESOURCE_FOLDER + SETTINGS_FILE);
		return getValue(f);
	}
	
	public String getValue(final File f) {
		return getValue(this, f);
	}
	
	public static String getValue(final QuerySettings setting, final File f) {
		try (InputStream in = new FileInputStream(f)) {
			Properties p = new Properties();
			p.load(in);
			
			return p.getProperty(setting.getName());
		} catch (FileNotFoundException e) {
			throw new FinderRuntimeException(e);
		} catch (Exception e) {
			String err = "Unable to parse file";
			throw new FinderRuntimeException(err, e);
		}
	}
}
