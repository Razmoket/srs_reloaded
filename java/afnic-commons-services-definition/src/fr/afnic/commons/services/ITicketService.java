/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/services/ITicketService.java#15 $
 * $Revision: 
 * $Author: ginguene $
 */

package fr.afnic.commons.services;

import java.util.List;

import fr.afnic.commons.beans.Ticket;
import fr.afnic.commons.beans.TicketHistoryEvent;
import fr.afnic.commons.beans.TicketStatus;
import fr.afnic.commons.beans.TradeTicket;
import fr.afnic.commons.beans.billing.BillableTicketInfo;
import fr.afnic.commons.beans.billing.CommandId;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.beans.search.ticket.TicketSearchCriteria;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

/**
 * Data Access Object permettant de consulter les informations et d'effectuer des opération sur les tickets opérationnel
 * 
 * @see fr.afnic.commons.beans.Ticket 
 */
public interface ITicketService {

    public Ticket getTicketWithId(String id, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Recupere tous les tickets d'un domanaine à partir de son nom. L'ordre est celui des id des tickets
     */
    public List<Ticket> getTicketsWithDomain(String domain, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Retourne la liste des historiques d'un ticket à partir de son id pas possible     
     */
    public List<TicketHistoryEvent> getHistoryEventsWithTicketId(String id, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Retourne le ticket en cours sur le domain ou null si il n'y en a pas
     */
    public Ticket getPendingTicketWithDomain(String domainName, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Crée un ticket de trade du titulaire actuel vers le contact dont on prévise le handle.
     */
    public TradeTicket createTradeTicket(String domainName, String newHolderHandle, String login, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Change le status d'un ticket.
     */
    public void updateStatus(String ticketId, TicketStatus newStatus, String comment, String userLogin, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Récupère sous forme de texte la liste des emails envoyé pour un ticket.
     */
    public List<String> getEmail(String ticketId, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Permet de rechercher des tickets suivant différents critères
     */
    public List<Ticket> searchTicket(TicketSearchCriteria criteria, UserId userId, TldServiceFacade tld) throws ServiceException;

    public List<BillableTicketInfo> getBillableTickets(int month, int year, int resultCount, UserId userId, TldServiceFacade tld) throws ServiceException;

    public void updateTicketBillingReference(String ticketId, CommandId commandId, UserId userId, TldServiceFacade tld) throws ServiceException;
}
