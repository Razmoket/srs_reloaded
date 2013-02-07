package fr.afnic.commons.services;

import fr.afnic.commons.beans.profiling.users.User;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

public interface IAccountService {

    /**
     * Retourne les informations sur un utilisateurs.<br>
     * Si le login et le pwd ne correspondent pas, retourne null.
     * 
     * @param login
     * @param pwd
     * @return
     */
    public User getUser(String login, String pwd, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Retourne les informations d'un utilisateur à partir de son login
     * 
     * @param userId
     * @throws ServiceException
     */
    public User getUser(String login, UserId userId, TldServiceFacade tld) throws ServiceException;

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

}
