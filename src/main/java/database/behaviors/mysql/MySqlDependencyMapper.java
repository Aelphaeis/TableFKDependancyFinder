package database.behaviors.mysql;

import database.behaviors.DependencyMapper;
import database.utilities.Databases.DBMS;

public class MySqlDependencyMapper implements DependencyMapper{



	@Override
	public void resolve() {
		// TODO implemented
		
	}
	@Override
	public DBMS getVendor() {
		return DBMS.MYSQL;
	}
	
}
