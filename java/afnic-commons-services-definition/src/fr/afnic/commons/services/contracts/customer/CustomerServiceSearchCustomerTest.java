package fr.afnic.commons.services.contracts.customer;

import org.junit.Test;

import fr.afnic.commons.beans.search.Pagination;
import fr.afnic.commons.beans.search.customer.CustomerSearchCriteria;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.commons.test.generator.UserGenerator;

public class CustomerServiceSearchCustomerTest {

    @Test(expected = IllegalArgumentException.class)
    public void testSearchCustomerWithNullCriteria() throws ServiceException {
        CustomerSearchCriteria criteria = null;
        AppServiceFacade.getCustomerService().searchCustomer(criteria, Pagination.createOnePagePagination(),
                                                             UserGenerator.getRootUserId(),
                                                             TldServiceFacade.Fr);
    }

}
