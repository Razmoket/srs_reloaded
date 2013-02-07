package fr.afnic.commons.beans;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import fr.afnic.commons.beans.description.IDescribedExternallyObject;
import fr.afnic.commons.beans.logs.ILogger;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.NotImplementedException;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;

/**
 * Contient les differents etats que peuvent contenir un ticket.<br/>
 * Correspond à ce qui s'appelle statusCode dans les bases de données.
 * 
 * @author ginguene
 * 
 */
public enum TicketStatus implements IDescribedExternallyObject {

    ClientApproved,
    Cancelled,
    Closed,
    DNSInstalled,
    DNSNotReady,
    DomainBlocked,
    DomainDeleted,
    DomainNameApproved,
    DomainRegistered,
    InvalidForm,
    InvoiceTwoMonthsOrMore,
    Open,
    PendingBlockDomain,
    PendingCheckover,
    PendingDelete,
    PendingDNSConfiguration,
    PendingExtraLegalSettlement,
    PendingHolderEMail,
    PendingHolderFax,
    PendingIdentifyHolder,
    PendingRegistrarApprove,
    PendingRegistrarCancel,
    PendingRegistrarInput,
    PendingRegistrarReject,
    PendingSolveProblem,
    PendingUnblockDomain,
    PendingUnfreezeDomain,
    PendingUserInput,
    PendingUserProcess;

    private static final ILogger LOGGER = AppServiceFacade.getLoggerService().getLogger(TicketStatus.class);

    /**
     * Constructeur permettant d'associé un commentaire en français à un statut.
     * 
     * @param description
     */
    private TicketStatus() {
    }

    /**
     * Retourne la description de l'état du ticket.
     * 
     * @return
     * @throws ServiceException
     */
    @Override
    public String getDescription(UserId userId, TldServiceFacade tld) {
        try {
            return AppServiceFacade.getDictionaryService().getDescription(this, userId, tld);
        } catch (ServiceException e) {
            TicketStatus.LOGGER.error("getDescription() failed", e);
            return this.toString();
        }
    }

    @Override
    public String getDescription(Locale locale, UserId userId, TldServiceFacade tld) throws ServiceException {
        return AppServiceFacade.getDictionaryService().getDescription(this, locale, userId, tld);
    }

    public boolean isFinal() {
        return this == TicketStatus.Closed || this == TicketStatus.Cancelled;
    }

    public static List<String> getStringValues() {
        ArrayList<String> ticketValues = new ArrayList<String>();
        for (TicketStatus status : TicketStatus.values()) {
            ticketValues.add(status.toString());
        }
        return ticketValues;
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
