package fr.afnic.commons.services.contracts.customercontact;

import junit.framework.TestCase;

import org.junit.Test;

import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.commons.test.generator.UserGenerator;

public class CustomerContactServiceUpdateTest {

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateCustomerContactWithBothNullCustomerContact() throws ServiceException {
        AppServiceFacade.getCustomerContactService().updateCustomerContact(null, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        TestCase.fail("No exception thrown");
    }
}
