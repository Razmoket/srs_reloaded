package fr.afnic.commons.services.multitld;

import java.util.List;

import fr.afnic.commons.beans.customer.Customer;
import fr.afnic.commons.beans.customer.CustomerId;
import fr.afnic.commons.beans.profiling.CustomerAccount;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.beans.search.Pagination;
import fr.afnic.commons.beans.search.customer.CustomerSearchCriteria;
import fr.afnic.commons.beans.search.customer.CustomerSearchResult;
import fr.afnic.commons.services.ICustomerService;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class MultiTldCustomerService implements ICustomerService {

    public MultiTldCustomerService() {
        super();
    }

    @Override
    public Customer createCustomer(Customer customer, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getCustomerService().createCustomer(customer, userId, tld);
    }

    @Override
    public Customer updateCustomer(Customer customer, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getCustomerService().updateCustomer(customer, userId, tld);
    }

    @Override
    public Customer getCustomerWithId(CustomerId customerId, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getCustomerService().getCustomerWithId(customerId, userId, tld);
    }

    @Override
    public Customer getCustomerWithNumber(String customerNumber, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getCustomerService().getCustomerWithNumber(customerNumber, userId, tld);
    }

    @Override
    public Customer getCustomerWithCode(String login, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getCustomerService().getCustomerWithCode(login, userId, tld);
    }

    @Override
    public CustomerSearchResult searchCustomer(CustomerSearchCriteria criteria, Pagination pagination, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getCustomerService().searchCustomer(criteria, pagination, userId, tld);
    }

    @Override
    public int getCustomerCount(UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getCustomerService().getCustomerCount(userId, tld);
    }

    @Override
    public List<CustomerAccount> getAccounts(CustomerId customerId, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getCustomerService().getAccounts(customerId, userId, tld);
    }

    @Override
    public Customer getRegistry(UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getCustomerService().getRegistry(userId, tld);
    }
}
