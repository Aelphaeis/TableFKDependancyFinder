package aide.database.behaviors;

import java.sql.Connection;
import java.util.List;

public interface TableResolver extends Behavior {
	List<String> resolve(Connection connection, String schema);
}
