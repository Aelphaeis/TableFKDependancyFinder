package aide.database.behaviors.oracle;

import java.sql.Connection;
import java.util.List;

import aide.database.behaviors.OracleBehavior;
import aide.database.behaviors.TableResolver;

public class SchemaTableResolver extends OracleBehavior implements TableResolver {

	@Override
	public List<String> resolve(Connection connection, String schema) {
		return null;
	}
	
}
