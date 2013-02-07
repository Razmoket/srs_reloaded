package fr.afnic.commons.services;

import fr.afnic.commons.beans.contact.identity.CorporateEntityIdentity;
import fr.afnic.commons.beans.contact.identity.IndividualIdentity;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

public interface IIdentityService {

    public IndividualIdentity getIndividualIdentity(int individualId, UserId userId, TldServiceFacade tld) throws ServiceException;

    public CorporateEntityIdentity getCorporateEntityIdentity(int corporateEntityId, UserId userId, TldServiceFacade tld) throws ServiceException;

    public int createCorporateEntityIdentity(CorporateEntityIdentity corporateIdentity, UserId userId, TldServiceFacade tld) throws ServiceException;

    public int createIndividualEntityIdentity(IndividualIdentity individualIdentity, UserId userId, TldServiceFacade tld) throws ServiceException;

}
