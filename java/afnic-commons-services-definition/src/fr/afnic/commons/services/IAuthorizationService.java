/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/services/IAuthorizationService.java#8 $
 * $Revision: 
 * $Author: barriere $
 */

package fr.afnic.commons.services;

import java.util.List;

import fr.afnic.commons.beans.Authorization;
import fr.afnic.commons.beans.AuthorizationSearchCriteria;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

/**
 * 
 * Classe permettant de gerer les accès aux authorizations et demandes d'authorization
 * 
 * @author ginguene
 * 
 */
public interface IAuthorizationService {

    /**
     * Recupere une authorization a partir de son id
     */
    public Authorization getAuthorizationWithId(int id, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Recupere la liste des authorizations
     */
    public List<Authorization> getAuthorizations(UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Recupere la liste des authorizations valide pour holderHandle/domainHandle/beHandle
     */
    public List<Authorization> getAuthorizations(String domain, String registrarHandle, String holderHandle, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Recupere la liste des authorizations valide pour un domaine    
     */
    public List<Authorization> getAuthorizations(String domain, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Retourne le code d'autorisation utilisable (non expiré) correspondant au triplet passé en parametre ou genere une nonFoundServiceException si
     * rien n'est trouvé
     */
    public Authorization getUsableAuthorization(String domain, String registrarCode, String holderHandle, UserId userId, TldServiceFacade tld) throws ServiceException;

    public List<Authorization> searchAuthorization(AuthorizationSearchCriteria criteria, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Rend un code d'autorisation inutilisable
     */
    public void invalidateAuthorization(int id, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Insere une nouvelle authorization dans la base de données
     */
    public int createAuthorization(Authorization authorization, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Met a jour une authorization dans la base de données
     */
    public void updateAuthorization(Authorization authorization, UserId userId, TldServiceFacade tld) throws ServiceException;

}
