/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/services/IRequestService.java#5 $
 * $Revision: #5 $
 * $Author: barriere $
 */

package fr.afnic.commons.services;

import java.util.Date;
import java.util.List;
import java.util.Set;

import fr.afnic.commons.beans.documents.Document;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.beans.request.Request;
import fr.afnic.commons.beans.request.RequestHistoryEvent;
import fr.afnic.commons.beans.request.RequestStatus;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

/**
 * Data Access Object permettant de consulter des opérations ou de récupérer des informations sur les Requetes.<br/>
 * Ces informations/Opérations sont celles communes à tous les types de requêtes.
 * 
 * 
 * @author ginguene
 * 
 */
public interface IRequestService {

    /**
     * Retourne une request à partir de son id
     * 
     * @param type
     * @param id
     * @return
     */
    public Request getRequestById(String type, int id, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Ajoute un evenement à l'historique d'une requete
     * 
     * 
     * @param request
     * @param event
     */
    public boolean addHistoryEvent(RequestHistoryEvent event, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Récupère la liste de tous les evenements dans l'historique d'une requete. L'ordre dans la liste se fait par date d'update: l'evenement le plus
     * recent est en le premier element de la liste.
     * 
     * 
     * @param request
     * @param event
     * @return
     */
    public List<RequestHistoryEvent> getHistory(String type, int requestId, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Compare si la requete passée en parametre a changé depuis son enregistrement, si c'est le cas, on enregistre ces changements dans l'historique.
     * Les changement portent sur le commentaire et le status
     * 
     * @param request
     * @param user
     * @return
     * @throws ServiceException
     */
    public void history(Request request, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Retourne la requête laquelle est associee une documentation dont on precise le handle
     * 
     * @param handle
     * @return
     * @throws ServiceException
     */
    public Request getLinkedRequestToDocumentWithHandle(String handle, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Permet de récupérer le commentaire lié à une requete. Ce dernier étant stocké dans un clob, sa récupération peut etre longue et ne doit etre
     * fait que si nécessaire
     * 
     * @param request
     * @return
     * @throws ServiceException
     */
    public String getRequestComment(Request request, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Retourne la date la plus ancienne ou la requete est passé dans ce status
     * 
     * @param status
     * @param request
     * @return
     * @throws ServiceException
     */
    public Date getFirstTimeStatusForRequest(RequestStatus status, Request request, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Retourne la date la plus récente où la requete est passée dans ce status
     * 
     * @param status
     * @param request
     * @return
     * @throws ServiceException
     */
    public Date getLastTimeStatusForRequest(RequestStatus status, Request request, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Annule une requete en lui attribuant<br/>
     * le statut correspondant au statut de suppression.
     * 
     * @param request
     *            requete à supprimer
     * @param login
     *            login du user à l'origine de l'opération
     * @throws ServiceException
     *             Si l'opération echoue
     */
    public void cancelRequest(Request request, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Supprime une requete en lui attribuant<br/>
     * de la base de données et ses dépendance en matière de document et d'historique
     * 
     * @param request
     *            requete à supprimer
     * @param login
     *            login du user à l'origine de l'opération
     * @throws ServiceException
     *             Si l'opération echoue
     */
    public void deleteRequest(Request request, String login, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Permet de changer la date d'une élément d'historique d'une requete. Cette méthode ne doit servir qu'à des fin de tests
     * 
     * @param event
     * @param newDate
     * @throws ServiceException
     */
    @Deprecated
    public void updateHistory(RequestHistoryEvent event, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Pour les tests uniquement Permet de changer la date de dernier changement de status. utile pour simuler des bons dans le temps
     * 
     * @param request
     * @param newDate
     * @throws ServiceException
     */
    @Deprecated
    public void changeLastStatusChange(Request request, Date newDate, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Pour les tests uniquement Permet de changer la date de création d'une requete
     * 
     * @param request
     * @param newDate
     * @throws ServiceException
     */
    @Deprecated
    public void changeDateCreation(Request request, Date newDate, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Retourne la liste des changements fait pas un utilisateur dans une journée.
     * 
     * @param userLogin
     *            Login de l'operateur.
     * @return La liste des changements opérés.
     * @throws ServiceException
     *             Si l'opération échoue.
     */
    public List<RequestHistoryEvent> getTodayHistoryWithUser(String userLogin, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Lie un document a une requete.
     * 
     * @param handle
     * @param request
     * @return
     * @throws ServiceException
     */
    public void linkDocumentToRequest(Document document, Request request, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Retourne les handles de toutes les doc liées à la requete. On retroune un Set qui ne peut pas contenir de doublon.
     * 
     * @param request
     * @return
     * @throws ServiceException
     */
    public Set<String> getDocumentsHandleLinkedToRequest(Request request, UserId userId, TldServiceFacade tld) throws ServiceException;

}
