package fr.afnic.commons.services;

import java.util.List;

import fr.afnic.commons.beans.contactdetails.ContactDetail;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

public interface IContactDetailsService {
    public List<ContactDetail> getCustomerContactDetails(int id_contact, UserId userId, TldServiceFacade tld) throws ServiceException;

    public List<ContactDetail> getCustomerDetails(int id_customer, UserId userId, TldServiceFacade tld) throws ServiceException;
}
