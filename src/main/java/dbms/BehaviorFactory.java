package dbms;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import exceptions.FinderRuntimeException;
import utilities.DatabaseSettings;
import utilities.Databases;
import utilities.Databases.DBMS;

public class BehaviorFactory {
	private static final BehaviorMap<Behavior> BMAP;
	static {
		BMAP = new BehaviorMap<>();
	}
	
	public <T extends Behavior> T getBehavior(Class<T> type) {
		try (Connection connection = DatabaseSettings.getConnection()) {
			return getBehavior(Databases.DBMS.resolve(connection), type);
		} catch (SQLException e) {
			throw new FinderRuntimeException(e);
		}
	}
	
	public <T extends Behavior> T getBehavior(DBMS dbms, Class<T> type) {
		List<Behavior> behaviors = BMAP.getOrDefault(type, new ArrayList<>());
		List<Behavior> result = behaviors.stream()
				.filter(p -> dbms.equals(p.getVendor()))
				.filter(p -> type.isInstance(behaviors))
				.collect(Collectors.toList());
		
		if (result.isEmpty()) {
			String err = "resource of type[%s] and dbms[%s] not found";
			throw new FinderRuntimeException(String.format(err, type, dbms));
		} else if (result.size() > 2) {
			String err = "too many found"; // TODO better err msg
			throw new FinderRuntimeException(String.format(err));
		} else {
			return type.cast(result.get(0));
		}
	}
	
}
