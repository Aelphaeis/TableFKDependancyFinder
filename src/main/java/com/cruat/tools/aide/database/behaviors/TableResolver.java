package com.cruat.tools.aide.database.behaviors;

import java.sql.Connection;
import java.util.List;

public interface TableResolver {
	List<String> resolve(Connection connection, String schema);
}
