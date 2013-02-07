package fr.afnic.commons.beans;

import java.util.Locale;

import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

public enum TradeOperation implements IRequestOperation {

    TradeDomain(TicketOperation.TradeDomain);

    private final TicketOperation ticketOperation;

    private TradeOperation(TicketOperation ticketOperation) {
        this.ticketOperation = ticketOperation;
    }

    @Override
    public TicketOperation getTicketOperation() {
        return this.ticketOperation;
    }

    @Override
    public String getOperationId() {
        return this.ticketOperation.getOperationId();
    }

    public static TradeOperation getOperation(String id) {
        for (TradeOperation operation : TradeOperation.values()) {
            if (operation.getOperationId().equalsIgnoreCase(id)) {
                return operation;
            }
        }
        throw new IllegalArgumentException("No TradeOperation found with id " + id);
    }

    @Override
    public String getDescription(UserId userId, TldServiceFacade tld) throws ServiceException {
        return this.ticketOperation.getDescription(userId, tld);
    }

    @Override
    public String getDescription(Locale locale, UserId userId, TldServiceFacade tld) throws ServiceException {
        return this.ticketOperation.getDescription(locale, userId, tld);
    }

    @Override
    public void setLocale(Locale locale) throws ServiceException {

    }

    @Override
    public String getDictionaryKey() {
        return this.getOperationId();
    }
}
