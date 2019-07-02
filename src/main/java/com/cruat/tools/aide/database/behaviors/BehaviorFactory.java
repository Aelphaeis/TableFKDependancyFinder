package com.cruat.tools.aide.database.behaviors;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.cruat.tools.aide.database.exceptions.FinderRuntimeException;
import com.cruat.tools.aide.database.utilities.DatabaseSettings;
import com.cruat.tools.aide.database.utilities.Databases;
import com.cruat.tools.aide.database.utilities.Databases.DBMS;

import jmo.util.Reflector;

public class BehaviorFactory {
	private static final BehaviorMap BMAP = createBehaviorMap();
	
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
				.filter(type::isInstance).collect(Collectors.toList());
		if (result.isEmpty()) {
			String err = "resource of type[%s] and dbms[%s] not found";
			throw new FinderRuntimeException(String.format(err, type, dbms));
		} else if (result.size() > 2) {
			String arrStr = Arrays.toString(result.toArray());
			String err = "To many results for type[%s] and dbms[%s] : %s";
			err = String.format(err, type, dbms, arrStr);
			throw new FinderRuntimeException(String.format(err));
		} else {
			return type.cast(result.get(0));
		}
	}
	
	private static BehaviorMap createBehaviorMap() {
		Package pkg = Behavior.class.getPackage();
		BehaviorMap bMap = new BehaviorMap();
		for (Class<?> cls : Reflector.getPackageClasses(pkg)) {
			boolean isCreateable = Reflector.isInstantiable(cls);
			if (Behavior.class.isAssignableFrom(cls) && isCreateable) {
				Behavior b = Behavior.class.cast(Reflector.initParamCtor(cls));
				Class<? extends Behavior> bCls = b.getClass();
				bMap.computeIfAbsent(bCls, p -> new ArrayList<>()).add(b);
			}
		}
		return bMap;
	}
}
