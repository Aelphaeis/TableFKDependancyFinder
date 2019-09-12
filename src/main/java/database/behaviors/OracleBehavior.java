package aide.database.behaviors;

import aide.database.utilities.Databases.DBMS;

public abstract class OracleBehavior implements Behavior{
	@Override
	public DBMS getVendor() {
		return DBMS.ORACLE;
	}
}
