/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.services;

import java.util.List;

import fr.afnic.commons.beans.contact.CustomerContact;
import fr.afnic.commons.beans.contact.CustomerContactCriteria;
import fr.afnic.commons.beans.contact.CustomerContactId;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

/**
 * Permet de récupérer les contacts liés à un customer.
 * 
 * @author ginguene
 * 
 */
public interface ICustomerContactService {

    public CustomerContact getCustomerContact(CustomerContactId customerContactId, UserId userId, TldServiceFacade tld) throws ServiceException;

    public CustomerContactId createCustomerContact(CustomerContact customerContact, UserId userId, TldServiceFacade tld) throws ServiceException;

    public CustomerContact updateCustomerContact(CustomerContact customerContact, UserId userId, TldServiceFacade tld) throws ServiceException;

    public List<CustomerContact> seachCustomerContact(CustomerContactCriteria criteria, UserId userId, TldServiceFacade tld) throws ServiceException;

    public int add_contact_detail(CustomerContactId customerContactId, int contactDetailsType, String value, UserId userId, TldServiceFacade tld) throws ServiceException;
}
