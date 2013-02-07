package fr.afnic.commons.beans;

import org.junit.Test;

import fr.afnic.commons.beans.request.TradeRequest;
import fr.afnic.commons.test.BeanTestCase;
import fr.afnic.commons.test.BeanTestCaseNameMethodRule;

public class TradeRequestTest {
	
	@Test
	public void testBean() throws Exception{
		BeanTestCase.assertSettersAndGetters(TradeRequest.class, new BeanTestCaseNameMethodRule( "comment"));
	}

	
}
