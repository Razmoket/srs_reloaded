package fr.afnic.commons.services.multitld;

import fr.afnic.commons.beans.corporateentity.CorporateEntity;
import fr.afnic.commons.beans.corporateentity.CorporateEntityIdentifier;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.IPublicLegalStructureService;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class MultiTldPublicLegalStructureService implements IPublicLegalStructureService {

    protected MultiTldPublicLegalStructureService() {
        super();
    }

    @Override
    public CorporateEntity getCorporateEntity(String id, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getPublicLegalStructureService().getCorporateEntity(id, userId, tld);
    }

    @Override
    public CorporateEntity getCorporateEntity(CorporateEntityIdentifier legalStructureIdentifier, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getPublicLegalStructureService().getCorporateEntity(legalStructureIdentifier, userId, tld);
    }
}
