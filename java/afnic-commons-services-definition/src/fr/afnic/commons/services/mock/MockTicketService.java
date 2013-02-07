package fr.afnic.commons.services.mock;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.NullArgumentException;

import fr.afnic.commons.beans.OperationForm;
import fr.afnic.commons.beans.Ticket;
import fr.afnic.commons.beans.TicketHistoryEvent;
import fr.afnic.commons.beans.TicketOperation;
import fr.afnic.commons.beans.TicketStatus;
import fr.afnic.commons.beans.TradeTicket;
import fr.afnic.commons.beans.WhoisContact;
import fr.afnic.commons.beans.billing.BillableTicketInfo;
import fr.afnic.commons.beans.billing.CommandId;
import fr.afnic.commons.beans.domain.Domain;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.beans.search.ticket.TicketSearchCriteria;
import fr.afnic.commons.beans.validatable.OperationFormId;
import fr.afnic.commons.services.ITicketService;
import fr.afnic.commons.services.contracts.ticket.TicketServiceParametersMethodChecker;
import fr.afnic.commons.services.exception.NotFoundException;
import fr.afnic.commons.services.exception.NotImplementedException;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.exception.TooManyResultException;
import fr.afnic.commons.services.exception.invalidformat.InvalidFormatException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class MockTicketService implements ITicketService {

    private final TicketServiceParametersMethodChecker methodChecker = new TicketServiceParametersMethodChecker();

    private int ticketIdSequence = 1;
    private final NumberFormat idNumberformat;

    /** clé = id d'un ticket; valeur = ticket */
    private HashMap<String, Ticket> idMap = new HashMap<String, Ticket>();

    /**
     * Constructeur par copie.
     * 
     * @param mockTicketService
     */
    public MockTicketService(MockTicketService mockTicketService) {
        this.idMap = mockTicketService.idMap;
        this.idNumberformat = mockTicketService.idNumberformat;
        this.ticketIdSequence = mockTicketService.ticketIdSequence;
    }

    public MockTicketService() {
        this.idNumberformat = NumberFormat.getIntegerInstance();
        this.idNumberformat.setMinimumIntegerDigits(12);
        this.idNumberformat.setGroupingUsed(false);
    }

    @Override
    public Ticket getTicketWithId(String id, UserId userId, TldServiceFacade tld) throws ServiceException {
        this.methodChecker.checkGetTicketWithIdParameters(id);

        Ticket ticket = this.idMap.get(id);

        if (ticket != null) {
            try {
                return ticket.copy();
            } catch (Exception e) {
                throw new ServiceException("copy failed", e);
            }
        } else {
            throw new NotFoundException("no ticket found with id " + id, Ticket.class);
        }
    }

    @Override
    public List<Ticket> getTicketsWithDomain(String domain, UserId userId, TldServiceFacade tld) throws ServiceException {
        if (domain == null) {
            throw new ServiceException("no ticket can be found with null domain name");
        }
        List<Ticket> retour = new ArrayList<Ticket>();
        for (Ticket tick : this.idMap.values()) {
            if (domain.equals(tick.getDomainName())) {
                try {
                    retour.add(tick.copy());
                } catch (Exception e) {
                    throw new ServiceException("copy failed", e);
                }
            }
        }
        /*if (retour.isEmpty()) {
            throw new NotFoundException("no ticket found with domain name " + domain, Ticket.class);
        }*/

        return retour;
    }

    @Override
    public List<TicketHistoryEvent> getHistoryEventsWithTicketId(String id, UserId userId, TldServiceFacade tld) throws ServiceException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Ticket getPendingTicketWithDomain(String domainName, UserId userId, TldServiceFacade tld) throws ServiceException {

        this.methodChecker.checkGetPendingTicketWithDomainParameters(domainName);
        for (Ticket ticket : this.idMap.values()) {
            if (domainName.equals(ticket.getDomainName()) && ticket.hasNotFinalStatus()) {
                return ticket;
            }
        }

        throw new NotFoundException("No pending ticket found with domain " + domainName, Ticket.class);
    }

    @Override
    public TradeTicket createTradeTicket(String domainName, String newHolderHandle, String login, UserId userId, TldServiceFacade tld) throws ServiceException {
        this.methodChecker.checkCreateTradeTicketParameters(domainName, newHolderHandle, login);
        TradeTicket tradeTicket = new TradeTicket(userId, tld);
        tradeTicket.setId(this.createTicketId());
        tradeTicket.setCreateDate(new Date());
        tradeTicket.setStatus(TicketStatus.PendingHolderEMail);
        tradeTicket.setOperation(TicketOperation.TradeDomain);
        tradeTicket.setDomainName(domainName);

        Domain domain = AppServiceFacade.getDomainService().getDomainWithName(domainName, userId, tld);
        tradeTicket.setOutgoingHolderHandle(domain.getHolderHandle());
        tradeTicket.setOutgoingRegistrarCode(domain.getRegistrarCode());

        tradeTicket.setIncomingHolderHandle(newHolderHandle);

        WhoisContact newHolder = AppServiceFacade.getWhoisContactService().getContactWithHandle(newHolderHandle, userId, tld);
        tradeTicket.setIncomingRegistrarCode(newHolder.getRegistrarCode());

        if (AppServiceFacade.getOperationFormService() instanceof MockOperationFormService) {
            MockOperationFormService mockOperationFormService = (MockOperationFormService) AppServiceFacade.getOperationFormService();
            Domain domainWithName = AppServiceFacade.getDomainService().getDomainWithName(domainName, userId, tld);
            OperationForm operationForm = mockOperationFormService.createCreateDomainOperationForm(domainWithName, domainWithName.getRegistrarCode(), userId, tld);
            tradeTicket.setOperationFormId(operationForm.getOperationFormId());
        }
        this.storeTicket(tradeTicket);
        return tradeTicket;
    }

    /**
     * Crée un ticket de creation pour un domaine. Le ticket est clos, cela permet de simuler une vrai création de domaine. Est appelé depuis
     * MockDomainService.createDomain();
     * 
     * @param domainName
     * @param registrarCode
     * @return
     */
    public Ticket createCreateDomainTicket(String domainName, String registrarCode, OperationFormId formId, UserId userId, TldServiceFacade tld) {
        Ticket ticket = new Ticket(userId, tld);
        ticket.setId(this.createTicketId());
        ticket.setCreateDate(new Date());
        ticket.setOperation(TicketOperation.CreateDomain);
        ticket.setStatus(TicketStatus.Closed);
        ticket.setDomainName(domainName);
        ticket.setRegistrarCode(registrarCode);
        ticket.setOperationFormId(formId);
        this.storeTicket(ticket);
        return ticket;
    }

    /**
     * Ajoute un ticket au service mock
     * 
     * @param ticket
     */
    public void storeTicket(Ticket ticket) {
        if (ticket == null) {
            throw new NullArgumentException("ticket");
        }

        if (ticket.getId() == null) {
            throw new NullArgumentException("ticket.id");
        }

        this.idMap.put(ticket.getId(), ticket);
    }

    private String createTicketId() {
        return "NIC" + this.idNumberformat.format(this.ticketIdSequence++);

    }

    /** {@inheritDoc} **/
    @Override
    public void updateStatus(String ticketId, TicketStatus newStatus, String comment, String userLogin, UserId userId, TldServiceFacade tld) throws ServiceException {
        if (newStatus == null) {
            throw new IllegalArgumentException("newStatus cannot be null");
        }

        Ticket ticketBeforeUpdate = this.getTicketWithId(ticketId, userId, tld);

        this.idMap.get(ticketId).setStatus(newStatus);
        TicketHistoryEvent event = new TicketHistoryEvent(userId, tld);
        event.setFromStatus(ticketBeforeUpdate.getStatus());
        event.setToStatus(newStatus);
        event.setDate(new Date());
        event.setComment(comment + "\nNouvel Etat: " + newStatus.getDescription(userId, tld) + "\n");
        event.setUser(userLogin);
        this.idMap.get(ticketId).addHistoryEntry(event);

    }

    @Override
    public List<String> getEmail(String ticketId, UserId userId, TldServiceFacade tld) throws ServiceException {
        List<String> emails = new ArrayList<String>();
        emails.add("Mails concernant le ticket " + ticketId + "[...]");
        return emails;
    }

    @Override
    public List<Ticket> searchTicket(TicketSearchCriteria criteria, UserId userId, TldServiceFacade tld) throws ServiceException {
        List<Ticket> result = new ArrayList<Ticket>();
        if (criteria.getTicketId() != null) {
            try {
                result.add(this.getTicketWithId(criteria.getTicketId(), userId, tld));
            } catch (InvalidFormatException e) {
                // Format invalide de l'id du ticket
            } catch (NotFoundException e) {
                // Format invalide de l'id du ticket
            }
            return result;
        }

        // Pour les autres critères on prend tous les résultats et on filtre
        result.addAll(this.idMap.values());
        if (criteria.getRegistrarCode() != null) {
            for (Ticket ticket : this.idMap.values()) {
                if (!criteria.getRegistrarCode().equals(ticket.getRegistrarCode())) {
                    result.remove(ticket);
                }

            }
        }

        if (criteria.getDomainName() != null) {
            for (Ticket ticket : this.idMap.values()) {
                if (!criteria.getDomainName().equals(ticket.getDomainName())) {
                    result.remove(ticket);
                }
            }
        }

        if (criteria.getBeginningDate() != null) {
            for (Ticket ticket : this.idMap.values()) {
                if (criteria.getBeginningDate().after(ticket.getCreateDate())) {
                    result.remove(ticket);
                }
            }
        }

        if (criteria.getEndingDate() != null) {
            for (Ticket ticket : this.idMap.values()) {
                if (criteria.getEndingDate().before(ticket.getCreateDate())) {
                    result.remove(ticket);
                }
            }
        }

        if (criteria.getOperation() != null) {
            for (Ticket ticket : this.idMap.values()) {
                if (criteria.getOperation() != ticket.getOperation()) {
                    result.remove(ticket);
                }
            }
        }

        if (criteria.getTicketStatus() != null) {
            for (Ticket ticket : this.idMap.values()) {
                if (criteria.getTicketStatus() != ticket.getStatus()) {
                    result.remove(ticket);
                }
            }
        }

        if (result.size() > criteria.getMaxResultCount()) {
            throw new TooManyResultException(criteria.getMaxResultCount());
        }

        return result;
    }

    @Override
    public List<BillableTicketInfo> getBillableTickets(int month, int year, int resultCount, UserId userId, TldServiceFacade tld) throws ServiceException {
        throw new NotImplementedException();
    }

    @Override
    public void updateTicketBillingReference(String ticketId, CommandId commandId, UserId userId, TldServiceFacade tld) throws ServiceException {
        throw new NotImplementedException();
    }
}
