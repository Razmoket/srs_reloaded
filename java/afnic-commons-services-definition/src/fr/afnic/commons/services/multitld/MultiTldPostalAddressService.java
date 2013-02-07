package fr.afnic.commons.services.multitld;

import fr.afnic.commons.beans.contactdetails.PostalAddress;
import fr.afnic.commons.beans.contactdetails.PostalAddressId;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.IPostalAddressService;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class MultiTldPostalAddressService implements IPostalAddressService {

    protected MultiTldPostalAddressService() {
        super();
    }

    @Override
    public PostalAddress getPostalAddress(PostalAddressId id, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getPostalAddressService().getPostalAddress(id, userId, tld);
    }

    @Override
    public void update(PostalAddress postalAddress, UserId userId, TldServiceFacade tld) throws ServiceException {
        tld.getServiceProvider().getPostalAddressService().update(postalAddress, userId, tld);
    }

    @Override
    public PostalAddressId create(PostalAddress postalAddress, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getPostalAddressService().create(postalAddress, userId, tld);
    }
}
