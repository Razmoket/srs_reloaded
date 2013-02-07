package fr.afnic.commons.services.multitld;

import java.util.List;

import fr.afnic.commons.beans.Ticket;
import fr.afnic.commons.beans.TicketHistoryEvent;
import fr.afnic.commons.beans.TicketStatus;
import fr.afnic.commons.beans.TradeTicket;
import fr.afnic.commons.beans.billing.BillableTicketInfo;
import fr.afnic.commons.beans.billing.CommandId;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.beans.search.ticket.TicketSearchCriteria;
import fr.afnic.commons.services.ITicketService;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class MultiTldTicketService implements ITicketService {

    protected MultiTldTicketService() {
        super();
    }

    @Override
    public Ticket getTicketWithId(String id, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getTicketService().getTicketWithId(id, userId, tld);
    }

    @Override
    public List<Ticket> getTicketsWithDomain(String domain, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getTicketService().getTicketsWithDomain(domain, userId, tld);
    }

    @Override
    public List<TicketHistoryEvent> getHistoryEventsWithTicketId(String id, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getTicketService().getHistoryEventsWithTicketId(id, userId, tld);
    }

    @Override
    public Ticket getPendingTicketWithDomain(String domainName, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getTicketService().getPendingTicketWithDomain(domainName, userId, tld);
    }

    @Override
    public TradeTicket createTradeTicket(String domainName, String newHolderHandle, String login, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getTicketService().createTradeTicket(domainName, newHolderHandle, login, userId, tld);
    }

    @Override
    public void updateStatus(String ticketId, TicketStatus newStatus, String comment, String userLogin, UserId userId, TldServiceFacade tld) throws ServiceException {
        tld.getServiceProvider().getTicketService().updateStatus(ticketId, newStatus, comment, userLogin, userId, tld);
    }

    @Override
    public List<String> getEmail(String ticketId, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getTicketService().getEmail(ticketId, userId, tld);
    }

    @Override
    public List<Ticket> searchTicket(TicketSearchCriteria criteria, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getTicketService().searchTicket(criteria, userId, tld);
    }

    @Override
    public List<BillableTicketInfo> getBillableTickets(int month, int year, int resultCount, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getTicketService().getBillableTickets(month, year, resultCount, userId, tld);
    }

    @Override
    public void updateTicketBillingReference(String ticketId, CommandId commandId, UserId userId, TldServiceFacade tld) throws ServiceException {
        tld.getServiceProvider().getTicketService().updateTicketBillingReference(ticketId, commandId, userId, tld);

    }
}
