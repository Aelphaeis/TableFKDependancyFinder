package com.cruat.tools.aide.database.behaviors.oracle;

import java.sql.Connection;
import java.util.List;

import com.cruat.tools.aide.database.behaviors.OracleBehavior;
import com.cruat.tools.aide.database.behaviors.TableResolver;

public class SchemaTableResolver extends OracleBehavior implements TableResolver {

	@Override
	public List<String> resolve(Connection connection, String schema) {
		return null;
	}
	
}
