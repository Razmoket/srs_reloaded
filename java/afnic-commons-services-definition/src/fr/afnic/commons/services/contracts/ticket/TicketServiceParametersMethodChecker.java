package fr.afnic.commons.services.contracts.ticket;

import fr.afnic.commons.beans.ContactIdentificationStatus;
import fr.afnic.commons.beans.domain.Domain;
import fr.afnic.commons.checkers.CheckerFacade;
import fr.afnic.commons.services.exception.DomainNotFoundException;
import fr.afnic.commons.services.exception.IllegalArgumentException;
import fr.afnic.commons.services.exception.NotFoundException;
import fr.afnic.commons.services.exception.NullArgumentException;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.exception.invalidformat.InvalidFormatException;
import fr.afnic.commons.services.exception.invalidformat.InvalidTicketIdException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.facade.exception.ServiceFacadeException;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.commons.test.generator.UserGenerator;

/**
 * Classe chargée de valider les parametres d'une méthode du TicketService.<br/>
 * Il est conseillé d'appeler les méthodes de cette classe en début de chaque méthode<br/>
 * d'implémentation de ITicketService.
 * 
 * 
 * @author ginguene
 */
public class TicketServiceParametersMethodChecker {

    /**
     * Vérification des parametres de la méthode getTicketWithId().
     * 
     * @param id
     * @throws InvalidTicketIdException
     */
    public void checkGetTicketWithIdParameters(String ticketId) throws InvalidFormatException {
        CheckerFacade.checkTicketId(ticketId);
    }

    /***
     * Vérification des parametres de la méthode getTicketsWithRegistrarName().
     * 
     * @param registrar
     * @throws IllegalArgumentException
     */
    public void checkGetTicketsWithRegistrarNameParameters(String registrar) throws IllegalArgumentException {

    }

    /**
     * Vérification des parametres de la méthode getTicketsWithRegistrarCode().
     * 
     * @param registrarCode
     * @throws IllegalArgumentException
     */
    public void checkGetTicketsWithRegistrarCodeParameters(String registrarCode) throws IllegalArgumentException {

    }

    /**
     * Vérification des parametres de la méthode getTicketsWithDomain().
     * 
     * @param domain
     * @throws IllegalArgumentException
     */
    public void checkGetTicketsWithDomainParameters(String domain) throws IllegalArgumentException {

    }

    /**
     * Vérification des parametres de la méthode getHistoryEventsWithTicketId().
     * 
     * @param id
     * @throws IllegalArgumentException
     */
    public void checkGetHistoryEventsWithTicketIdParameters(String id) throws IllegalArgumentException {

    }

    /**
     * Vérification des parametres de la méthode getTicketsLinkedToLegalStructureId().
     * 
     * @param id
     * @throws IllegalArgumentException
     */
    public void checkGetTicketsLinkedToLegalStructureIdParameters(String id) throws IllegalArgumentException {

    }

    /**
     * Vérification des parametres de la méthode getPendingTicketWithDomain().
     * 
     * @param domainName
     * @throws IllegalArgumentException
     */
    public void checkGetPendingTicketWithDomainParameters(String domainName) throws ServiceException {
        this.checkDomaineNameParameter(domainName);
    }

    /**
     * Vérification des parametres de la méthode createTradeTicket().
     * 
     * @param domainName
     * @param newHolderHandle
     * @param login
     * @throws ServiceFacadeException
     * @throws ServiceException
     */
    public void checkCreateTradeTicketParameters(String domainName, String newHolderHandle, String login) throws ServiceException {
        Domain domain = this.checkDomaineNameParameter(domainName);
        try {
            if (domain.getHolder().getIdentificationStatus() != ContactIdentificationStatus.Ok) {
                throw new IllegalArgumentException("the identification status of the actual domain holder must be OK:" + domain.getHolder().getIdentificationStatus());
            }
        } catch (NotFoundException e) {
            throw new IllegalArgumentException("'" + domainName + "' has no holder, so it cannot be traded");
        }
    }

    /**
     * Vérifie qu'un parametre de type nom de domain possède un bon format, est valide et est connu dans les bases.
     * 
     * @param domainName
     * @return
     * @throws ServiceException
     */
    private Domain checkDomaineNameParameter(String domainName) throws ServiceException {
        if (domainName == null) {
            throw new NullArgumentException("domainName");
        }
        CheckerFacade.checkDomainName(domainName, UserGenerator.getRootUserId(), TldServiceFacade.Fr);

        try {
            return AppServiceFacade.getDomainService().getDomainWithName(domainName, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        } catch (NotFoundException e) {
            throw new DomainNotFoundException(domainName);
        }
    }
}
