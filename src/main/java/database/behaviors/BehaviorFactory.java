package database.behaviors;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import database.exceptions.FinderRuntimeException;
import database.utilities.DatabaseSettings;
import database.utilities.Databases;
import database.utilities.Databases.DBMS;
import jmo.util.Reflector;

public class BehaviorFactory {
	private static final Logger logger = LogManager.getLogger();
	private static final BehaviorMap BMAP = createBehaviorMap();
	
	public <T extends Behavior> T getBehavior(Class<T> type) {
		try (Connection connection = DatabaseSettings.getConnection()) {
			return getBehavior(Databases.DBMS.resolve(connection), type);
		} catch (SQLException e) {
			throw new FinderRuntimeException(e);
		}
	}
	
	public <T extends Behavior> T getBehavior(DBMS dbms, Class<T> type) {
		logger.traceEntry(null, dbms, type);
		Objects.requireNonNull(dbms);
		Objects.requireNonNull(type);
		
		List<T> result = BMAP.entrySet().stream()
				.filter(p -> type.isAssignableFrom(p.getKey()))
				.map(Entry::getValue)
				.flatMap(List::stream)
				.filter(p -> dbms.equals(p.getVendor()))
				.filter(type::isInstance)
				.map(type::cast)
				.collect(Collectors.toList());
		
		return logger.traceExit(validate(result, dbms, type).get(0));
	}
	
	private static <T> List<T> validate(List<T> r, DBMS dbms, Class<T> type){
		if (r.isEmpty()) {
			String err = "resource of type [%s] and dbms[%s] not found";
			err = String.format(err, type, dbms);
			throw new UnsupportedOperationException(err);
		} else if (r.size() > 2) {
			String err = "To many results for type[%s] and dbms[%s] : %s";
			err = String.format(err, type, dbms, r);
			throw new FinderRuntimeException(String.format(err));
		}
		return r;
	}
	
	protected static BehaviorMap createBehaviorMap() {
		logger.traceEntry();
		Package pkg = Behavior.class.getPackage();
		BehaviorMap bMap = new BehaviorMap();
		for (Class<?> cls : Reflector.getPackageClasses(pkg)) {
			boolean isCreateable = Reflector.isInstantiable(cls);
			if (Behavior.class.isAssignableFrom(cls) && isCreateable) {
				Behavior b = Behavior.class.cast(Reflector.initParamCtor(cls));
				Class<? extends Behavior> bCls = b.getClass();
				logger.trace("adding [{}]{}] to behavior map", bCls, b);
				bMap.computeIfAbsent(bCls, p -> new ArrayList<>()).add(b);
			}
		}
		return logger.traceExit(bMap);
	}
}
