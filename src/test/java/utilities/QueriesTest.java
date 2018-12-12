package utilities;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class QueriesTest {
	@Test(timeout = 1000)
	public void queryQuery_validQuery_queryReturned() {
		assertNotNull(Queries.getQuery("schema_tables"));
	}
}
