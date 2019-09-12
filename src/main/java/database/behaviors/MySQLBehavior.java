package database.behaviors;

import database.utilities.Databases.DBMS;

public abstract class MySQLBehavior implements Behavior {
	@Override
	public DBMS getVendor() {
		return DBMS.MYSQL;
	}
}
