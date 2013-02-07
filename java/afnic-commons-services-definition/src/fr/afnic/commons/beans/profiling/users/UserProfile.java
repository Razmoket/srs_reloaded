/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/beans/profiling/users/UserProfile.java#10 $
 * $Revision: #10 $
 * $Author: barriere $
 */

package fr.afnic.commons.beans.profiling.users;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;

import fr.afnic.commons.beans.description.IDescribedInternalObject;
import fr.afnic.commons.beans.operations.OperationType;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.facade.exception.ServiceFacadeException;
import fr.afnic.commons.services.tld.TldServiceFacade;

/**
 * Classe mère de tous les profiles utilisateurs. Un profil est un ensemble de droit.<br/>
 * Tout utilisateur membre d'un profil, en possède tous les droits.<br/>
 * 
 * Doit changer dans Boa, les profils seront en bases et non plus dans des objets statics
 * 
 * @Todo Améliorer la gestion de hiérarchie entre profils qui a été faite en 5 min. Ce sujet mérite plus de reflexion.
 * 
 * 
 */
public class UserProfile implements Cloneable, Serializable, IDescribedInternalObject {

    private static final long serialVersionUID = 1L;

    private List<UserRight> rights = null;
    private final List<OperationType> types = null;

    private String name;

    private String description;

    private int id = -1;

    public UserProfile(String name) {
        this.name = name;
    }

    public UserProfile(int id, String name) {
        this.name = name;
        this.id = id;
    }

    public UserProfile() {

    }

    public int getId() {
        return this.id;
    }

    public String getIdAsString() {
        return Integer.toString(this.id);
    }

    public List<UserRight> getRights() {
        return this.rights;
    }

    public void setRights(List<UserRight> rights) {
        this.rights = rights;
    }

    /**
     * Ajoute un droit au profil * @param right
     */
    public void addRight(UserRight right) {
        if (!this.hasRight()) {
            this.rights = new ArrayList<UserRight>();
        }
        this.rights.add(right);
    }

    /**
     * Indique si le profil inclue le droit passé en parametre.
     * 
     * @param right
     * @return
     */
    public boolean hasRight(UserRight right) {
        return (this.rights != null && this.rights.contains(right));
    }

    public boolean canExecute(OperationType type) {
        return (this.types != null && this.types.contains(type));
    }

    /**
     * Indique si il y a des droits liées au profil.
     * 
     * @return
     */
    public boolean hasRight() {
        return this.rights != null && !this.rights.isEmpty();
    }

    public String getName() {
        return this.name;
    }

    /**
     * Retourne une copie du Profile.
     * 
     * @return
     * @throws CloneNotSupportedException
     */
    public UserProfile copy() throws CloneNotSupportedException {
        return (UserProfile) super.clone();
    }

    @Override
    public String getDescription() throws ServiceException {

        return this.description;
    }

    @Override
    public String getDescription(Locale locale) throws ServiceException {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public void setLocale(Locale locale) throws ServiceException {

    }

    @Override
    public String getDictionaryKey() {
        return this.name;
    }

    public List<User> getUsers(UserId userId, TldServiceFacade tld) throws ServiceException, ServiceFacadeException {
        //Peu optimisé mais peu appelé donc fait au plus rapide
        List<User> users = AppServiceFacade.getUserService().getUsers(userId, tld);

        List<User> ret = new ArrayList<User>();

        for (User user : users) {
            if (StringUtils.equals(user.getProfileName(), this.getName())) {
                ret.add(user);
            }
        }

        return ret;

    }
}
