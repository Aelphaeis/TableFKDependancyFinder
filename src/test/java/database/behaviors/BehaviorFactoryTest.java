package database.behaviors;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import database.behaviors.BehaviorFactory;
import database.behaviors.BehaviorMap;

public class BehaviorFactoryTest {
	
	@Test
	public void createBehaviorMap_ctor_BehaviorMapCreated() {
		assertNotNull(new BehaviorFactory());
	}
	
	@Test
	public void createBehaviorMap_noInputs_BehaviorMap() {
		BehaviorMap result = BehaviorFactory.createBehaviorMap();
		assertNotNull(result);
	}
}
