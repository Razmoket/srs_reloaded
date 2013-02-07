package fr.afnic.commons.beans.contact.identity;

import java.io.Serializable;

import fr.afnic.commons.beans.corporateentity.CorporateEntity;
import fr.afnic.commons.beans.logs.ILogger;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.beans.validatable.IValidatable;
import fr.afnic.commons.beans.validatable.InvalidDataException;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class CorporateWithPublicDataEntityIdentity extends CorporateEntityIdentity implements IValidatable, Serializable {

    private static final ILogger LOGGER = AppServiceFacade.getLoggerService().getLogger(CorporateWithPublicDataEntityIdentity.class);

    private static final long serialVersionUID = 1L;

    private CorporateEntity corporateEntity;

    public CorporateWithPublicDataEntityIdentity(CorporateEntityIdentity identity, UserId userId, TldServiceFacade tld) {
        super();
        this.setOrganizationName(identity.getName());

        this.setSiren(identity.getSiren());
        this.setSiret(identity.getSiret());
        this.setTradeMark(identity.getTradeMark());
        this.setIntracommunityVat(identity.getIntracommunityVat());
        this.setWaldec(identity.getWaldec());
        this.setDescription(identity.getDescription());
        try {
            if (identity.getSiren() != null) {
                this.corporateEntity = AppServiceFacade.getPublicLegalStructureService().getCorporateEntity(identity.getSiren(), userId, tld);
            }
        } catch (ServiceException e) {
            LOGGER.error("impossible de charger la legalStructure : " + e, e);
        }
    }

    @Override
    public void validate() throws InvalidDataException {
        super.validate();
    }

    public String getWhoisOrganizationName() {
        return this.getName();
    }

    public void setWhoisOrganizationName(String organizationName) {
        this.setOrganizationName(organizationName);
    }

    public String getLegalStatusPublic() {
        if (this.corporateEntity == null)
            return null;
        return this.corporateEntity.getName();
    }

    public boolean isPublicStatus() {
        if (this.corporateEntity == null)
            return false;
        return this.corporateEntity.isActive();
    }

    public CorporateEntity getLegalStruct() {
        return this.corporateEntity;
    }

    public void setLegalStruct(CorporateEntity legalStruct) {
        this.corporateEntity = legalStruct;
    }
}
