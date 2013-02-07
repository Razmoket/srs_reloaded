package fr.afnic.commons.beans;

import java.util.Locale;

import fr.afnic.commons.beans.description.IDescribedExternallyObject;
import fr.afnic.commons.beans.logs.ILogger;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.NotImplementedException;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;

/**
 * Représente une opération sur un nom de domaine. Classe à clarifier.
 * 
 * @author ginguene
 * 
 */
public enum TicketOperation implements IDescribedExternallyObject {

    CreateDomain("C"),
    DeleteDomain,
    TransferDomain,
    TradeDomain,
    RecoverDomain("P"),
    UpdateDomainContactInfo,
    UpdateDomainConfiguration,
    BulkTransferDomain,
    BlockDomain,
    UnblockDomain,
    IdentifyHolder,
    CheckHolder,
    CheckExtraLegalSettlement,
    LegacyUpdateDomain,
    LegacyRenameDomain,
    RestoreDomain,
    UpdateDomainContext,
    Undefined,
    UpdateContactInformation,
    FreezeDomain,
    UnfreezeDomain;

    private static final ILogger LOGGER = AppServiceFacade.getLoggerService().getLogger(TicketOperation.class);

    private final String operationId;

    private TicketOperation() {
        this(null);
    }

    private TicketOperation(String operationId) {
        this.operationId = operationId;
    }

    public String getOperationId() {
        if (this.operationId == null) {
            throw new IllegalArgumentException("No operationId defined for " + this);
        } else {
            return this.operationId;
        }
    }

    /**
     * Renvoit une description de l'etat en toute lettre
     * 
     * @return description de l'etat
     * @throws ServiceException
     */
    @Override
    public String getDescription(UserId userId, TldServiceFacade tld) {
        try {
            return AppServiceFacade.getDictionaryService().getDescription(this, userId, tld);
        } catch (ServiceException e) {
            TicketOperation.LOGGER.error("getDescription() failed", e);
            return this.toString();
        }
    }

    @Override
    public String getDescription(Locale locale, UserId userId, TldServiceFacade tld) throws ServiceException {
        try {
            return AppServiceFacade.getDictionaryService().getDescription(this, locale, userId, tld);
        } catch (ServiceException e) {
            TicketOperation.LOGGER.error("getDescription('" + locale + "') failed", e);
            return this.toString();
        }
    }

    @Override
    public String getDictionaryKey() {
        return this.toString();
    }

    @Override
    public void setLocale(Locale locale) throws ServiceException {
        throw new NotImplementedException();
    }

}
