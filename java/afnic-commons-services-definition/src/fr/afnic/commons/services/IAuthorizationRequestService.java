package fr.afnic.commons.services;

import java.util.Date;
import java.util.List;

import org.joda.time.Interval;

import fr.afnic.commons.beans.documents.GddDocument;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.beans.request.AuthorizationRequest;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

public interface IAuthorizationRequestService {

    /**
     * Recupere une demande d'authorization a partir de son id
     * 
     */
    public AuthorizationRequest getAuthorizationRequestWithId(int id, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Recupere la liste des demandes d'authorizations
     * 
     */
    public List<AuthorizationRequest> getAuthorizationRequests(UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Insere une nouvelle demande d'authorization dans la base de données.
     * 
     */
    public int createAuthorizationRequest(AuthorizationRequest authorizationRequest, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Met a jour une demande d'authorization dans la base de données
     */
    public void updateAuthorizationRequest(AuthorizationRequest authorizationRequest, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Retourne la liste des authorizationRequest qui ont été généré pour un domaine
     */
    public List<AuthorizationRequest> getAuthorizationRequestsWithDomain(String domain, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Retourne la liste des authorizationRequest qui ont été généré pour un pattern de domaine (like '%domain%')
     * 
     */
    public List<AuthorizationRequest> getAuthorizationRequestsWithDomainLike(String domain, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Retourne la requete d'autorisation a laquelle doit etre lié la doc. Si aucune n'est trouvé, retourne null
     */
    public AuthorizationRequest getRequestToLinkWithDocument(GddDocument document, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Retourne la liste de toutes les requetes qui sont dans un statut non final     
     */
    public List<AuthorizationRequest> getAuthorizationRequestsInNonFinalStatus(UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Retourne toutes les requetes d'autorisation créées entre 2 dates.
     */
    public List<AuthorizationRequest> getCreatedRequestsBewteenTwoDates(Date start, Date end, UserId userId, TldServiceFacade tld) throws ServiceException;

    public Interval getSunrisePeriod(UserId userId, TldServiceFacade tld) throws ServiceException;
}
