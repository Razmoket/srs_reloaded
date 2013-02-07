package fr.afnic.commons.services.multitld;

import java.util.List;

import fr.afnic.commons.beans.contactdetails.ContactDetail;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.IContactDetailsService;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class MultiTldContactDetailsService implements IContactDetailsService {

    public MultiTldContactDetailsService() {
        super();
    }

    @Override
    public List<ContactDetail> getCustomerContactDetails(int id_contact, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getContactDetailsService().getCustomerContactDetails(id_contact, userId, tld);
    }

    @Override
    public List<ContactDetail> getCustomerDetails(int id_customer, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getContactDetailsService().getCustomerDetails(id_customer, userId, tld);
    }

}
