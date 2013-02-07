package fr.afnic.commons.services.proxy;

import fr.afnic.commons.beans.contactdetails.PostalAddress;
import fr.afnic.commons.beans.contactdetails.PostalAddressId;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.IPostalAddressService;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class ProxyPostalAddressService extends ProxyService<IPostalAddressService> implements IPostalAddressService {

    protected ProxyPostalAddressService() {
        super();
    }

    protected ProxyPostalAddressService(IPostalAddressService delegationService) {
        super(delegationService);
    }

    @Override
    public PostalAddress getPostalAddress(PostalAddressId id, UserId userId, TldServiceFacade tld) throws ServiceException {
        return this.getDelegationService().getPostalAddress(id, userId, tld);
    }

    @Override
    public void update(PostalAddress postalAddress, UserId userId, TldServiceFacade tld) throws ServiceException {
        this.getDelegationService().update(postalAddress, userId, tld);
    }

    @Override
    public PostalAddressId create(PostalAddress postalAddress, UserId userId, TldServiceFacade tld) throws ServiceException {
        return this.getDelegationService().create(postalAddress, userId, tld);
    }
}
