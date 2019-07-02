package com.cruat.tools.aide.database.behaviors.factory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.cruat.tools.aide.database.exceptions.FinderRuntimeException;
import com.cruat.tools.aide.database.utilities.DatabaseSettings;
import com.cruat.tools.aide.database.utilities.Databases;
import com.cruat.tools.aide.database.utilities.Databases.DBMS;

public class BehaviorFactory {
	private static final BehaviorMap BMAP;
	static {
		BMAP = new BehaviorMap();
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
			// TODO better err message
			String err = "too many found"; 
			throw new FinderRuntimeException(String.format(err));
		} else {
			return type.cast(result.get(0));
		}
	}
	
	private static BehaviorMap createBehaviorMap() {
		return null;
	}
	
}