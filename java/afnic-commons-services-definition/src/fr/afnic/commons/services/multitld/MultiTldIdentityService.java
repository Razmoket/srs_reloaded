package fr.afnic.commons.services.multitld;

import fr.afnic.commons.beans.contact.identity.CorporateEntityIdentity;
import fr.afnic.commons.beans.contact.identity.IndividualIdentity;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.IIdentityService;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class MultiTldIdentityService implements IIdentityService {

    protected MultiTldIdentityService() {
        super();
    }

    @Override
    public IndividualIdentity getIndividualIdentity(int individualId, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getIdentityService().getIndividualIdentity(individualId, userId, tld);
    }

    @Override
    public CorporateEntityIdentity getCorporateEntityIdentity(int corporateEntityId, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getIdentityService().getCorporateEntityIdentity(corporateEntityId, userId, tld);

    }

    @Override
    public int createCorporateEntityIdentity(CorporateEntityIdentity corporateIdentity, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getIdentityService().createCorporateEntityIdentity(corporateIdentity, userId, tld);
    }

    @Override
    public int createIndividualEntityIdentity(IndividualIdentity individualIdentity, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getIdentityService().createIndividualEntityIdentity(individualIdentity, userId, tld);
    }
}
