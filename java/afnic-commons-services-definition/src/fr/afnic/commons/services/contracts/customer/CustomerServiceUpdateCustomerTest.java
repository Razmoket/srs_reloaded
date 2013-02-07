package fr.afnic.commons.services.contracts.customer;

import junit.framework.TestCase;

import org.junit.Ignore;
import org.junit.Test;

import fr.afnic.commons.beans.contactdetails.PostalAddress;
import fr.afnic.commons.beans.customer.Customer;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.commons.test.generator.PostalAddressGenerator;
import fr.afnic.commons.test.generator.UserGenerator;

public class CustomerServiceUpdateCustomerTest {

    @Ignore
    @Test(expected = IllegalArgumentException.class)
    public void testUpdateCustomerWithBothNullArgument() throws ServiceException {
        AppServiceFacade.getCustomerService().updateCustomer(null, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
    }

    @Test
    public void testUpdateCustomer() throws ServiceException {

        System.err.println("start");

        Customer customer = AppServiceFacade.getCustomerService().getCustomerWithCode("TEST", new UserId(1), TldServiceFacade.Fr);
        PostalAddress newPA = PostalAddressGenerator.generateRandomPostalAddressInParis();
        customer.setPostalAddress(newPA);

        AppServiceFacade.getCustomerService().updateCustomer(customer, new UserId(1), TldServiceFacade.Fr);

        Customer customerAfter = AppServiceFacade.getCustomerService().getCustomerWithCode("TEST", new UserId(1), TldServiceFacade.Fr);

        TestCase.assertEquals(customer.getPostalAddress().getStreetStr(), customerAfter.getPostalAddress().getStreetStr());

    }
}
