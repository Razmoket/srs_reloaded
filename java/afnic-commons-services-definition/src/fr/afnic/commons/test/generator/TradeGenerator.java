/*
 * $Id: TradeTestCaseGenerator.java,v 1.1 2010/08/11 10:04:40 ginguene Exp $
 * $Revision: 1.1 $
 * $Author: ginguene $
 */

package fr.afnic.commons.test.generator;

import java.util.Date;

import fr.afnic.commons.beans.ContactIdentificationStatus;
import fr.afnic.commons.beans.CorporateEntityWhoisContact;
import fr.afnic.commons.beans.TradeTicket;
import fr.afnic.commons.beans.WhoisContact;
import fr.afnic.commons.beans.contactdetails.EmailAddress;
import fr.afnic.commons.beans.domain.Domain;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.beans.request.RequestHistoryEvent;
import fr.afnic.commons.beans.request.TradeRequest;
import fr.afnic.commons.beans.request.TradeRequestStatus;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.exception.invalidformat.InvalidFormatException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.commons.test.generator.exception.GeneratorException;

/**
 * Genere des cas de test pour les trade
 * 
 * @author ginguene
 * 
 */
public class TradeGenerator {

    private static TradeGenerator instance = new TradeGenerator();

    public static TradeGenerator getInstance() {
        return TradeGenerator.instance;
    }

    /**
     * @throws InvalidFormatException
     * @throws ServiceException
     * 
     * 
     */
    public TradeRequest createTradeRequestForDomain(String domainName) throws GeneratorException {
        try {

            TradeTicket tradeTicket = closePendingTicketAndCreateNewTradeTicketForDomain(domainName);

            TradeRequest tradeRequest = new TradeRequest(UserGenerator.getRootUserId(), TldServiceFacade.Fr);
            tradeRequest.setCreateDate(new Date());
            tradeRequest.setDomainName(domainName);
            tradeRequest.setStatus(TradeRequestStatus.Running);
            tradeRequest.setTicketId(tradeTicket.getId());

            int idTradeRequest = AppServiceFacade.getTradeService().createTradeRequest(tradeRequest, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
            tradeRequest.setId(idTradeRequest);
            return tradeRequest;
        } catch (Exception e) {
            throw new GeneratorException("createTradeRequestForDomain(" + domainName + ") failed", e);
        }
    }

    private static TradeTicket closePendingTicketAndCreateNewTradeTicketForDomain(String domainName) throws GeneratorException {
        try {
            closePendingTicketsForDomain(domainName);
            TradeTicket tradeTicket = TradeGenerator.createNewTradeTicketForDomain(domainName);
            return tradeTicket;
        } catch (Exception e) {
            throw new GeneratorException("closePendingTicketAndCreateNewTradeTicketForDomain(" + domainName + ") failed", e);
        }
    }

    private static void closePendingTicketsForDomain(String domainName) throws ServiceException {
        try {
            AppServiceFacade.getDomainService().cancelTradeDomain(domainName, UserGenerator.generateRootUser().getLogin(), UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        } catch (Exception e) {
            // si aucun ticket n'est ouvert pour le domaine
        }
    }

    public static void changeActualHolderStatusInOkForDomain(String domainName) throws ServiceException {

        System.err.println("--->" + AppServiceFacade.getDomainService().getClass().getSimpleName());
        Domain domain = AppServiceFacade.getDomainService().getDomainWithName(domainName, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        WhoisContact actualHolder = domain.getHolder();
        actualHolder.setIdentificationStatus(ContactIdentificationStatus.Ok);
        AppServiceFacade.getWhoisContactService().updateContact(actualHolder, UserGenerator.generateRootUser().getLogin(), UserGenerator.getRootUserId(), TldServiceFacade.Fr);
    }

    /**
     * Cree un ticket de trade pour un domain vers le titulaire dont le handle est newHolderHandle<br/>
     * et en retourne l'identifiant du ticket créé.
     * 
     * @param domain
     * @param newHolderHandle
     * @return
     * @throws ServiceException
     */
    public String createTradeTicket(String domain, String newHolderHandle) throws ServiceException {
        try {
            AppServiceFacade.getDomainService().cancelTradeDomain(domain, UserGenerator.ROOT_LOGIN, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        } catch (ServiceException e) {
            // Si aucun ticket n'existe pour ce domaine on a une erreur
        }

        try {

            AppServiceFacade.getTicketService().createTradeTicket(domain, newHolderHandle, UserGenerator.ROOT_LOGIN, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
            TradeTicket ticket = (TradeTicket) AppServiceFacade.getTicketService().getPendingTicketWithDomain(domain, UserGenerator.getRootUserId(), TldServiceFacade.Fr);

            return ticket.getId();
        } catch (ServiceException e) {
            throw e;
        }
    }

    public void changeLastStatusChangeDate(TradeRequest request, Date newDate, UserId userId) throws ServiceException {
        AppServiceFacade.getRequestService().changeLastStatusChange(request, newDate, userId, TldServiceFacade.Fr);

        RequestHistoryEvent event = request.getHistory().get(0);
        event.setDate(newDate);
        AppServiceFacade.getRequestService().updateHistory(event, userId, TldServiceFacade.Fr);

    }

    public static TradeTicket createNewTradeTicketForDomain(String domainName) throws Exception {

        changeActualHolderStatusInOkForDomain(domainName);

        CorporateEntityWhoisContact newHolder = ContactGenerator.createCorporateEntityContact();

        newHolder.addEmailAddress(new EmailAddress("ginguene@afnic.fr"));

        AppServiceFacade.getTicketService().createTradeTicket(domainName, newHolder.getHandle(), UserGenerator.ROOT_LOGIN, UserGenerator.getRootUserId(), TldServiceFacade.Fr);

        TradeTicket tradeTicket = (TradeTicket) AppServiceFacade.getTicketService().getPendingTicketWithDomain(domainName, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        return tradeTicket;
    }
}
