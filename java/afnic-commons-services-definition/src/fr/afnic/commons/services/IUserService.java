/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/services/IUserService.java#16 $
 * $Revision: 
 * $Author: chandavoine $
 */

package fr.afnic.commons.services;

import java.util.List;

import fr.afnic.commons.beans.profiling.users.User;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.beans.profiling.users.UserRight;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

/**
 * Data Access Objecte permettant de gérer les utilisateurs pour les applications basées sur l'afnic-commons
 * 
 * @author ginguene
 * 
 */
public interface IUserService {

    public List<User> getUsers(UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Retourne les informations sur un utilisateurs.<br>
     * Si le login et le pwd ne correspondent pas, retourne null.
     * 
     * @param login
     * @param pwd
     * @return
     */
    public User authenticate(String login, String pwd) throws ServiceException;

    /**
     * Retourne les informations d'un utilisateur à partir de son login
     * 
     * @param userId
     * @throws ServiceException
     * 
     */
    public User getUser(String login, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Retourne les informatiosn d'un utilisateur à partir de son id
     * 
     * @param userid
     * @return
     * @throws ServiceException
     */
    public User getUser(UserId userid, UserId userIdCaller, TldServiceFacade tld) throws ServiceException;

    /**
     * Retourne les informations d'un utilisateur à partir de son id dans nicope.<br/>
     * Cette fonction ne devrait pas etre utilisé, l'id de reference pour un user étant son login.<br/>
     * Cepandant dans la table request_history, c'est l'id de nicope qui est utiliser.<br/>
     * En attendant que ce défaut de conception soit corrigé, la conversion se fait via cette méthode.
     * 
     * @param userId
     * @throws ServiceException
     * @Deprecated
     */
    public String getUserLogin(String nicopeId, UserId userId, TldServiceFacade tld) throws ServiceException;

    public User getUserWithNicpersId(String nicopeId, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Ajoute un utilisateur dans les bases.
     * 
     * @param user
     *            User à ajouter.
     * @throws ServiceException
     *             Si l'opération échoue.
     */
    public void addUser(User user, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Supprime un utilisateur dans les bases
     * 
     * @param UserLogin
     *            Login du user à supprimer.
     * @throws ServiceException
     *             Si l'opération échoue.
     */
    public void removeUser(String userLogin, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Indique si le login est connu dans les bases.
     * 
     * @param userLogin
     * @return
     * @throws ServiceException
     *             Si l'opération échoue.
     */
    public boolean isKnownUser(String userLogin, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Indique si le login est inconnu dans les bases.
     * 
     * @param userLogin
     * @return
     * @throws ServiceException
     *             Si l'opération échoue.
     */
    public boolean isNotKnownUser(String userLogin, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Retourne tous les logins des chargés de clientèle
     * 
     * @return
     * @throws ServiceException
     */
    public List<String> getAccountManagersLogin(UserRight userRight, UserId userId, TldServiceFacade tld) throws ServiceException;

    public User createAndGetUser(User user, UserId createUserId, TldServiceFacade tld) throws ServiceException;

    public void updatePassord(UserId userId, String newPassword, UserId updater, TldServiceFacade tld) throws ServiceException;

}
