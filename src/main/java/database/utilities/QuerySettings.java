package database.utilities;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

import database.exceptions.FinderRuntimeException;

public enum QuerySettings {
	SCHEMA("schema"),
	TABLE("table"),
	PRIMARY_KEY("primary.key");
	
	private static final String FINDER_ERR = "property [%s] required a value.";
	private static final String SETTINGS_FILE = "query.properties";

	private final String name;
	
	private QuerySettings(String propertyName) {
		this.name = propertyName;
	}
	
	public String getName() {
		return name;
	}
	
	public String getValue() {
		return Reflector.load(SETTINGS_FILE, this::readSetting);
	}
	
	public String validate() {
		String value = Objects.requireNonNull(getValue());
		if(value.trim().isEmpty()) {
			String msg = String.format(FINDER_ERR, getName());
			throw new FinderRuntimeException(msg);
		}
		else {
			return value;
		}
	}
	
	private String readSetting(InputStream is) throws IOException {
		Properties p = new Properties();
		p.load(is);
		return p.getProperty(getName());
	}
}
