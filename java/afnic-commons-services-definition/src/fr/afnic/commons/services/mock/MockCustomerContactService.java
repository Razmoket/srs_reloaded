/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.services.mock;

import java.util.HashMap;
import java.util.List;

import fr.afnic.commons.beans.contact.CustomerContact;
import fr.afnic.commons.beans.contact.CustomerContactCriteria;
import fr.afnic.commons.beans.contact.CustomerContactId;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.ICustomerContactService;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class MockCustomerContactService implements ICustomerContactService {

    private static int lastId = 1;

    public HashMap<Integer, CustomerContact> map = new HashMap<Integer, CustomerContact>();

    @Override
    public CustomerContact getCustomerContact(CustomerContactId customerContactId, UserId userId, TldServiceFacade tld) {
        if (this.map.containsKey(customerContactId.getIntValue())) {
            return this.map.get(customerContactId.getIntValue());
        }
        return null;
    }

    @Override
    public CustomerContactId createCustomerContact(CustomerContact customerContact, UserId userId, TldServiceFacade tld) {

        customerContact.setContactId(new CustomerContactId(MockCustomerContactService.lastId++));
        this.map.put(customerContact.getContactId().getIntValue(), customerContact);

        // Vraiment pas propre, juste pour les tests!!!
        MockCustomerService mockCustomerService = (MockCustomerService) AppServiceFacade.getCustomerService();
        if (mockCustomerService.isInit() && customerContact.getCustomerId() != null) {
            try {
                AppServiceFacade.getCustomerService().getCustomerWithId(customerContact.getCustomerId(), userId, tld)
                                .getCustomerContacts().add(customerContact);
            } catch (ServiceException e) {
                e.printStackTrace();
            }
        }

        return customerContact.getContactId();
    }

    @Override
    public CustomerContact updateCustomerContact(CustomerContact contact, UserId userId, TldServiceFacade tld) throws ServiceException {
        contact.setUpdateUserId(userId);
        this.map.put(contact.getContactId().getIntValue(), contact);
        return contact;
    }

    @Override
    public List<CustomerContact> seachCustomerContact(CustomerContactCriteria criteria, UserId userId, TldServiceFacade tld) throws ServiceException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int add_contact_detail(CustomerContactId customerContactId, int contactDetailsType, String value, UserId userId, TldServiceFacade tld) throws ServiceException {
        // TODO Auto-generated method stub
        return 0;
    }

}
