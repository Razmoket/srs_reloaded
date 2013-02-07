package fr.afnic.commons.services;

import fr.afnic.commons.beans.contactdetails.PostalAddress;
import fr.afnic.commons.beans.contactdetails.PostalAddressId;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

public interface IPostalAddressService {

    public PostalAddress getPostalAddress(PostalAddressId id, UserId userId, TldServiceFacade tld) throws ServiceException;

    public void update(PostalAddress postalAddress, UserId userId, TldServiceFacade tld) throws ServiceException;

    public PostalAddressId create(PostalAddress postalAddress, UserId userId, TldServiceFacade tld) throws ServiceException;

}
