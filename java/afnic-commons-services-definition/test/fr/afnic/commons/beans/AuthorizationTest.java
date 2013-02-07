package fr.afnic.commons.beans;

import org.junit.Test;

import fr.afnic.commons.test.BeanTestCase;
import fr.afnic.commons.test.BeanTestCaseNameMethodRule;

public class AuthorizationTest {
	
	@Test
	public void testBean() throws Exception{
		BeanTestCase.assertSettersAndGetters(Authorization.class,new BeanTestCaseNameMethodRule( "holder", "registrar","createDate"));
	}

}
