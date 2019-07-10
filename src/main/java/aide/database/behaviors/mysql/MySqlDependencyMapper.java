package aide.database.behaviors.mysql;

import aide.database.behaviors.DependencyMapper;
import aide.database.utilities.Databases.DBMS;

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
