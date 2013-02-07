package fr.afnic.commons.beans;

import org.junit.Test;

import fr.afnic.commons.test.BeanTestCase;
import fr.afnic.commons.test.BeanTestCaseNameMethodRule;

public class TicketTest {
	
	@Test
	public void testBean() throws Exception{
		BeanTestCase.assertSettersAndGetters(Ticket.class,new BeanTestCaseNameMethodRule( "registrar","createDate" , "domain"));
	}


}
