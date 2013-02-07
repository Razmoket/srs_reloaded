/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/services/ITradeService.java#6 $
 * $Revision: 
 * $Author: barriere $
 */

package fr.afnic.commons.services;

import java.util.List;

import fr.afnic.commons.beans.documents.GddDocument;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.beans.request.TradeRequest;
import fr.afnic.commons.beans.search.traderequest.TradeRequestSearchCriteria;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

/**
 * Classe permettant de gerer les accès aux demandes de Trade.
 * 
 * @author ginguene
 * 
 */
public interface ITradeService {
    /**
     * Recupere une demande de validation trade par fax a partir de son id
     * 
     * @param nicHandle
     * @return une Autorisation si il est trouve sinon null
     * @throws ServiceException
     *             si l'acces aux données n'est pas possible
     * @see fr.afnic.commons.beans.Authorization
     */
    public TradeRequest getTradeRequestWithId(int id, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Retourne toutes les demandes de validation de trade par fax
     * 
     * @return
     * @throws ServiceException
     */
    public List<TradeRequest> getTradeRequests(UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Retourne toutes les demandes de validation de trade par fax à afficher dans les outils
     * 
     * @return
     * @throws ServiceException
     */
    @Deprecated
    public List<TradeRequest> getTradeRequestsToDisplay(UserId userId, TldServiceFacade tld) throws ServiceException;

    public List<TradeRequest> searchTradeRequest(TradeRequestSearchCriteria criteria, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Retourne toutes les demandes de validation de trade par fax qui doivent etre expirée
     * 
     * @return
     * @throws ServiceException
     */
    public List<TradeRequest> getTradeRequestsToExpire(UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * /** Retourne toutes les demandes de validation de trade par fax qui ne sont pas en status final
     * 
     * @return
     * @throws ServiceException
     */
    public List<TradeRequest> getPendingTradeRequests(UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Insere une nouvelle demande de trade dans la base de données
     * 
     * @return l'id de la nouvelle demande inseree
     * @throws IllegalAccessException
     *             si l'acces aux données n'est pas possible
     * @see fr.afnic.gateway.beans.AuthorizationRequestHistory
     */
    public int createTradeRequest(TradeRequest tradeRequest, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Met a jour une demande de trade dans la base de données
     * 
     * @return true si la maj s'est bien passee
     * @throws ServiceException
     *             si l'acces aux données n'est pas possible
     * @see fr.afnic.gateway.beans.AuthorizationRequestHistory
     */
    public void updateTradeRequest(TradeRequest tradeRequest, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Retourne toutes les demandes de trades liées à un domain
     * 
     * @param domain
     * @return
     * @throws ServiceException
     */
    public List<TradeRequest> getTradeRequestsWithDomain(String domain, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Retourne la requete à laquelle on peut lier un document ou null
     * 
     * @param document
     * @return
     * @throws ServiceException
     */
    public TradeRequest getRequestToLinkWithDocument(GddDocument document, UserId userId, TldServiceFacade tld) throws ServiceException;

}
