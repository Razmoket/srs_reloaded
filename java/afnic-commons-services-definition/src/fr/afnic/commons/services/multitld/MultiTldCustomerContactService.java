package fr.afnic.commons.services.multitld;

import java.util.List;

import fr.afnic.commons.beans.contact.CustomerContact;
import fr.afnic.commons.beans.contact.CustomerContactCriteria;
import fr.afnic.commons.beans.contact.CustomerContactId;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.ICustomerContactService;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class MultiTldCustomerContactService implements ICustomerContactService {

    public MultiTldCustomerContactService() {
        super();
    }

    @Override
    public CustomerContact getCustomerContact(CustomerContactId customerContactId, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getCustomerContactService().getCustomerContact(customerContactId, userId, tld);
    }

    @Override
    public CustomerContactId createCustomerContact(CustomerContact customerContact, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getCustomerContactService().createCustomerContact(customerContact, userId, tld);
    }

    @Override
    public CustomerContact updateCustomerContact(CustomerContact customerContact, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getCustomerContactService().updateCustomerContact(customerContact, userId, tld);
    }

    @Override
    public List<CustomerContact> seachCustomerContact(CustomerContactCriteria criteria, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getCustomerContactService().seachCustomerContact(criteria, userId, tld);
    }

    @Override
    public int add_contact_detail(CustomerContactId customerContactId, int contactDetailsType, String value, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getCustomerContactService().add_contact_detail(customerContactId, contactDetailsType, value, userId, tld);
    }
}
