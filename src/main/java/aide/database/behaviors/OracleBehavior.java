package com.cruat.tools.aide.database.behaviors;

import com.cruat.tools.aide.database.utilities.Databases.DBMS;

public abstract class OracleBehavior implements Behavior{
	@Override
	public DBMS getVendor() {
		return DBMS.ORACLE;
	}
}
