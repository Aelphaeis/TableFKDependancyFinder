package dbms;

import java.util.HashMap;
import java.util.List;

class BehaviorMap<T extends Behavior> extends HashMap<Class<T>, List<T>> {
	private static final long serialVersionUID = 1L;
}
