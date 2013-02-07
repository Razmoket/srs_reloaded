/*
 * $Id: $ $Revision: $ $Author: $
 */

package fr.afnic.commons.services.mock;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import fr.afnic.commons.beans.contact.identity.IndividualIdentity;
import fr.afnic.commons.beans.contactdetails.EmailAddress;
import fr.afnic.commons.beans.contactdetails.PhoneNumber;
import fr.afnic.commons.beans.contactdetails.PostalAddress;
import fr.afnic.commons.beans.corporateentity.CorporateEntity;
import fr.afnic.commons.beans.corporateentity.IntracommunityVat;
import fr.afnic.commons.beans.customer.Customer;
import fr.afnic.commons.beans.customer.CustomerId;
import fr.afnic.commons.beans.customer.PaymentMethod;
import fr.afnic.commons.beans.logs.ILogger;
import fr.afnic.commons.beans.profiling.CustomerAccount;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.beans.search.Pagination;
import fr.afnic.commons.beans.search.customer.CustomerSearchCriteria;
import fr.afnic.commons.beans.search.customer.CustomerSearchResult;
import fr.afnic.commons.services.ICustomerService;
import fr.afnic.commons.services.exception.CustomerNotFoundException;
import fr.afnic.commons.services.exception.FaultCode;
import fr.afnic.commons.services.exception.NotImplementedException;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.commons.test.generator.PostalAddressGenerator;
import fr.afnic.commons.test.generator.exception.GeneratorException;
import fr.afnic.commons.utils.Preconditions;
import fr.afnic.utils.StringUtils;

public class MockCustomerService implements ICustomerService {

    /** Definition du Logger de la classe MockCustomerService */
    private static final ILogger LOGGER = AppServiceFacade.getLoggerService().getLogger(MockCustomerService.class);

    private static int lastCustomerId = 123;

    public HashMap<String, Customer> conventionMap = new HashMap<String, Customer>();
    public HashMap<CustomerId, Customer> customerIdMap = new HashMap<CustomerId, Customer>();

    private boolean isInit = false;

    public void init(UserId userId, TldServiceFacade tld) {
        if (this.conventionMap.size() == 0) {
            try {
                this.createCustomer(this.createCompanyCustomer(userId, tld), userId, tld);
                this.createCustomer(this.createIndividualCustomer(userId, tld), userId, tld);
            } catch (ServiceException e) {
                e.printStackTrace();
            }
        }
        this.isInit = true;
    }

    @Override
    public Customer createCustomer(Customer customer, UserId userId, TldServiceFacade tld) throws ServiceException {

        customer.setCustomerId(new CustomerId(MockCustomerService.lastCustomerId++));
        customer.setCreateDate(new Date());
        customer.setCustomerNumber("cr" + MockCustomerService.lastCustomerId);

        this.conventionMap.put(customer.getCustomerNumber(), customer);
        this.customerIdMap.put(customer.getCustomerId(), customer);

        return customer;
    }

    @Override
    public Customer updateCustomer(Customer customer, UserId userId, TldServiceFacade tld) throws ServiceException {
        Preconditions.checkNotNull(customer, "customer");
        Preconditions.checkNotNull(userId, "userId");
        throw new NotImplementedException();
    }

    @Override
    public Customer getCustomerWithId(CustomerId customerId, UserId userId, TldServiceFacade tld) throws ServiceException {
        Preconditions.checkNotNull(customerId, "customerId");
        this.init(userId, tld);

        if (this.customerIdMap.containsKey(customerId)) {
            return this.customerIdMap.get(customerId);
        } else {
            throw new CustomerNotFoundException(customerId.getValue());
        }
    }

    @Override
    public int getCustomerCount(UserId userId, TldServiceFacade tld) throws ServiceException {
        throw new NotImplementedException();
    }

    @Override
    public Customer getCustomerWithNumber(String customerNumber, UserId userId, TldServiceFacade tld) throws ServiceException {
        this.init(userId, tld);

        if (this.conventionMap.containsKey(customerNumber)) {
            return this.conventionMap.get(customerNumber);
        } else {
            throw new CustomerNotFoundException(customerNumber);
        }
    }

    private Customer createIndividualCustomer(UserId userId, TldServiceFacade tld) throws GeneratorException {

        Customer customer = Customer.createIndividualCustomer(userId, tld);

        IndividualIdentity identity = (IndividualIdentity) customer.getIdentity();
        identity.setFirstName("Jean");
        identity.setLastName(StringUtils.generateWord(10));

        this.populateCustomer(customer);
        return customer;

    }

    private Customer createCompanyCustomer(UserId userId, TldServiceFacade tld) throws ServiceException {
        Customer companyCustomer = Customer.createCorporateEntityCustomer(userId, tld);

        CorporateEntity company = CorporateEntity.createCompany(userId, tld);
        company.setOrgnaizationName("SARL toto");
        company.setIntracommunityVat(new IntracommunityVat("FR00000001"));

        throw new RuntimeException("Not implemented");
        /*
        List<CustomerContact> contacts = new ArrayList<CustomerContact>();
        CustomerContact noc = new CustomerContact();
        noc.setContactType(CustomerContactRole.NOC);
        noc.setPhoneNumbers(new PhoneNumber("0102030405"));
        noc.setEmailAddresses(new EmailAddress("noc@nic.fr"));
        EppAccount nocAccount = new EppAccount();
        nocAccount.setLogin("LDAP NOC");
        noc.setEppAccount(nocAccount);

        contacts.add(ServiceFacade.getCustomerContactService().createCustomerContact(noc).getObjectOwner());

         CustomerContact admin = new CustomerContact();
         admin.setContactType(CustomerContactRole.ADMIN);
         admin.setPhoneNumbers(new PhoneNumber("0102030405"));
         contacts.add(ServiceFacade.getCustomerContactService().createCustomerContact(admin).getObjectOwner());
         EppAccount adminAccount = new EppAccount();
         adminAccount.setLogin("LDAP ADMIN");
         admin.setEppAccount(adminAccount);

          CustomerContact business = new CustomerContact();
          business.setContactType(CustomerContactRole.BUSINESS);
          business.setPhoneNumbers(new PhoneNumber("business@nic.fr"));
          EppAccount businessAccount = new EppAccount();
          businessAccount.setLogin("LDAP business");
          business.setEppAccount(businessAccount);
          business.setActivityRegion(new AllRegionsOfACountry(Country.FR));
          contacts.add(ServiceFacade.getCustomerContactService().createCustomerContact(business).getObjectOwner());

          companyCustomer.setCustomerContacts(contacts);
          this.populateCustomer(companyCustomer);
          return companyCustomer;*/
    }

    private void populateCustomer(Customer customer) throws GeneratorException {

        customer.setPhoneNumbers(new PhoneNumber("0102030405"));
        customer.setEmailAddresses(new EmailAddress("toto@nic.fr"));

        PostalAddress postalAddress = PostalAddressGenerator.generateRandomPostalAddressInParis();
        customer.setPostalAddress(postalAddress);

        customer.setAccountManagerId(new UserId(1));
        customer.setPaymentMethod(PaymentMethod.CreditCard);

        customer.setComment("contact généré par un mock");

        throw new RuntimeException("Not implemented");
        /*CustomerContact admin = new CustomerContact();
        admin.setContactType(CustomerContactRole.ADMIN);
        customer.addContact(admin);
        EppAccount account = new EppAccount();
        account.setLogin("epp-login");
        account.setPassword("pwd-epp");
        customer.setAccount(account);

        customer.getContracts().add(new CustomerContract(CustomerContractType.AFNIC_MEMBER));
        customer.getContracts().add(new CustomerContract(CustomerContractType.PARL_PARTNER));

        customer.setOnRedList(customer instanceof IndividualCustomer);*/
    }

    @Override
    public CustomerSearchResult searchCustomer(CustomerSearchCriteria criteria, Pagination pagination, UserId userId, TldServiceFacade tld) throws ServiceException {

        Preconditions.checkNotNull(criteria, "criteria");
        // Connait toujours 1000 résultats
        // sauf
        // si le nom du be est 'none' => aucun retour
        // si le nom du be est 'resource' => retourne une exeption
        //

        CustomerSearchResult result = new CustomerSearchResult();
        result.setTotalResultCount(1000);
        int pageCount = result.getTotalResultCount() / pagination.getMaxResultCount();

        result.setPageCount(pageCount);
        result.setCriteria(criteria);
        result.setPagination(pagination);

        if ("none".equalsIgnoreCase(criteria.getName())) {
            result.setPageResults(new ArrayList<Customer>());
            result.setTotalResultCount(0);
            result.setPageCount(0);
            return result;
        }

        if ("fail".equalsIgnoreCase(criteria.getName())) {
            throw new ServiceException(FaultCode.Ressource, "Database is crashed");
        }

        List<Customer> customers = this.createDatabaseCustomers(criteria, userId, tld);
        if (MockCustomerService.LOGGER.isDebugEnabled()) {
            MockCustomerService.LOGGER.debug("customers.size:" + customers.size());
        }

        List<Customer> results = new ArrayList<Customer>();

        int startIndex = ((pagination.getPageNumber() - 1) * pagination.getMaxResultCount());

        int endIndex = startIndex + pagination.getMaxResultCount();
        if (endIndex > customers.size()) {
            endIndex = customers.size();
        }

        if (MockCustomerService.LOGGER.isDebugEnabled()) {
            MockCustomerService.LOGGER.debug("startIndex:" + startIndex);
            MockCustomerService.LOGGER.debug("endIndex:" + endIndex);
        }
        for (int i = startIndex; i < endIndex; i++) {
            results.add(customers.get(i));
        }

        // Problème générique List<Customer> != List<Customer>

        result.setPageResults(results);

        return result;

    }

    public List<Customer> createDatabaseCustomers(CustomerSearchCriteria criteria, UserId userId, TldServiceFacade tld) throws ServiceException {
        List<Customer> customers = new ArrayList<Customer>();

        int maxResult;
        if (criteria.isExactMatch()) {
            maxResult = 1;
        } else {
            maxResult = 1000;
        }

        for (int i = 0; i < maxResult; i++) {
            Customer customer = this.createCompanyCustomer(userId, tld);

            String add = "";
            if (criteria.isNotExactMatch()) {
                add += i;
            }

            throw new RuntimeException("Not implemented");
            /*EppAccount account = new EppAccount();
            customer.setAccount(account);
            account.setLogin("login");
            account.setPassword("password");

            if (criteria.getLogin() != null) {
                account.setLogin(criteria.getLogin() + add);
                account.setPassword("pwd-" + criteria.getLogin() + add);
            }

            String customerNumber = "";
            if (criteria.getCustomerNumber() != null) {
                customerNumber = criteria.getCustomerNumber();
                customer.setCustomerNumber(customerNumber);
            }

            if (criteria.getBusinessContactCountryCode() != null) {
                CustomerContact business = customer.getContacts(CustomerContactRole.BUSINESS).get(0);

                Country country = ServiceFacade.getDictionaryService().getCountry(criteria.getBusinessContactCountryCode());

                if (criteria.getBusinessContactRegionCode() == null && country != null) {
                    business.setActivityRegion(new AllRegionsOfACountry(country));
                }
            }

            if (criteria.getBusinessContactRegionCode() != null) {
                CustomerContact business = customer.getContacts(CustomerContactRole.BUSINESS).get(0);
                Region region = ServiceFacade.getDictionaryService().getRegion(criteria.getBusinessContactRegionCode());
                business.setActivityRegion(region);
            }
            customers.add(customer);*/
        }
        return customers;
    }

    public boolean isInit() {
        return this.isInit;
    }

    @Override
    public List<CustomerAccount> getAccounts(CustomerId customerId, UserId userId, TldServiceFacade tld) throws ServiceException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Customer getCustomerWithCode(String code, UserId userId, TldServiceFacade tld) throws ServiceException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Customer getRegistry(UserId userId, TldServiceFacade tld) throws ServiceException {
        // TODO Auto-generated method stub
        return null;
    }
}
