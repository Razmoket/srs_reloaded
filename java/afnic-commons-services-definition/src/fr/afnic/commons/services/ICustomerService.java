/*
 * $Id: $ $Revision: $ $Author: $
 */

package fr.afnic.commons.services;

import java.util.List;

import fr.afnic.commons.beans.customer.Customer;
import fr.afnic.commons.beans.customer.CustomerId;
import fr.afnic.commons.beans.profiling.CustomerAccount;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.beans.search.Pagination;
import fr.afnic.commons.beans.search.customer.CustomerSearchCriteria;
import fr.afnic.commons.beans.search.customer.CustomerSearchResult;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

/**
 * Service permettant d'accéder à des méthodes CRUD sur les clients
 * 
 * @author ginguene
 * 
 */
public interface ICustomerService {

    public Customer createCustomer(Customer customer, UserId userId, TldServiceFacade tld) throws ServiceException;

    public Customer updateCustomer(Customer customer, UserId userId, TldServiceFacade tld) throws ServiceException;

    public Customer getCustomerWithId(final CustomerId customerId, UserId userId, TldServiceFacade tld) throws ServiceException;

    public Customer getCustomerWithNumber(final String customerNumber, UserId userId, TldServiceFacade tld) throws ServiceException;

    public Customer getCustomerWithCode(final String code, UserId userId, TldServiceFacade tld) throws ServiceException;

    public CustomerSearchResult searchCustomer(CustomerSearchCriteria criteria, Pagination pagination, UserId userId, TldServiceFacade tld) throws ServiceException;

    public int getCustomerCount(UserId userId, TldServiceFacade tld) throws ServiceException;

    public List<CustomerAccount> getAccounts(CustomerId customerId, UserId userId, TldServiceFacade tld) throws ServiceException;

    public Customer getRegistry(UserId userId, TldServiceFacade tld) throws ServiceException;

}
