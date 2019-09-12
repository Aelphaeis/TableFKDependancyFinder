package database.behaviors;

import java.util.List;

public interface KeyResolver extends Behavior{
	List<String> resolve(String schema, String tableName);
}
