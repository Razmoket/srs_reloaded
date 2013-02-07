/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/beans/profiling/users/User.java#12 $
 * $Revision: #12 $
 * $Author: barriere $
 */

package fr.afnic.commons.beans.profiling.users;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import fr.afnic.commons.beans.BusinessObject;
import fr.afnic.commons.beans.contactdetails.EmailAddress;
import fr.afnic.commons.beans.operations.OperationType;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.utils.ToStringHelper;

/**
 * Represente les utilisateurs des outils que l'on developpe, et les droit auquels ils ont acces. Cette partie est fait au plus vite et devra être
 * amelioré ultérieurement pour intégrer plus de sécurité.
 * 
 * @author ginguene
 * 
 */
public class User extends BusinessObject<UserId> implements Cloneable, Serializable {

    private static final long serialVersionUID = 1L;

    private String login;
    private String password;

    private UserId backupUserId;
    private String nicpersId;
    private String nicpersLogin;

    private String firstName;
    private String lastName;

    private UserProfile profile;
    private EmailAddress email;

    private final UserStatus status = UserStatus.Active;

    public User(UserId userId, TldServiceFacade tld) {
        super(userId, tld);
    }

    /** Portefeuille de BE */
    private String[] registrarCodePortfolio;

    public String getLogin() {
        return this.login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String pwd) {
        this.password = pwd;
    }

    public String getNicpersId() {
        return this.nicpersId;
    }

    public void setNicpersId(String id) {
        this.nicpersId = id;
    }

    /**
     * Indique si le user possede le droit passé en parametre. Un utilisateur pôssede le droit si son profile le possede ou si l'on lui a attribue.
     * 
     * @param right
     * @return
     */
    public boolean hasRight(UserRight right) {
        return this.hasProfile() && this.profile.hasRight(right);

    }

    public boolean canExecute(OperationType operationType) {
        return this.hasProfile() && this.profile.canExecute(operationType);
    }

    public UserId getBackupUserId() {
        return this.backupUserId;
    }

    public void setBackupUserId(UserId backupUserId) {
        this.backupUserId = backupUserId;
    }

    public boolean hasNotRight(UserRight right) {
        return !this.hasRight(right);
    }

    public UserProfile getProfile() {
        return this.profile;
    }

    public void setProfile(UserProfile profile) {
        this.profile = profile;
    }

    /**
     * Indique le nom du profil de l'utilisateur.
     * 
     * @return
     */
    public String getProfileName() {
        if (this.hasProfile()) {
            return this.profile.getName();
        } else {
            return "No profile";
        }
    }

    public boolean hasProfile() {
        return this.profile != null;
    }

    /**
     * Retourne la liste des droits de l'utilisateur.
     * 
     * @return
     */
    public List<UserRight> getRights() {
        return this.profile.getRights();
    }

    /**
     * Retourne une copie du User.
     * 
     * @return
     * @throws CloneNotSupportedException
     */
    public User copy() throws CloneNotSupportedException {
        return (User) super.clone();
    }

    @Override
    public String toString() {
        return new ToStringHelper().addAllObjectAttributes(this).toString();
    }

    public void setRegistrarCodePortfolioList(String registrarCodePortfolioList) {
        if (registrarCodePortfolioList != null) {
            this.registrarCodePortfolio = registrarCodePortfolioList.replaceAll(" ", "").split(",");
        }
    }

    public boolean hasNoRegistrarCodePortfolio() {
        return !this.hasRegistrarCodePortfolio();
    }

    public boolean hasRegistrarCodePortfolio() {
        return this.registrarCodePortfolio != null && this.registrarCodePortfolio.length > 0;
    }

    public String[] getRegistrarCodePortfolio() {
        String[] copy = new String[this.registrarCodePortfolio.length];
        System.arraycopy(this.registrarCodePortfolio, 0, copy, 0, this.registrarCodePortfolio.length);
        return copy;
    }

    public EmailAddress getEmail() {
        return this.email;
    }

    public void setEmail(EmailAddress email) {
        this.email = email;
    }

    public void setEmail(String email) {
        this.email = new EmailAddress(email);
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getNicpersLogin() {
        return this.nicpersLogin;
    }

    public void setNicpersLogin(String nicpersLogin) {
        this.nicpersLogin = nicpersLogin;
    }

    public String getDisplayFullName() {
        return this.getFirstName() + " " + this.getLastName();
    }

    public String getDisplayName() {
        return this.getFirstName().substring(0, 1) + ". " + this.getLastName();
    }

    public UserStatus getStatus() {
        return this.status;
    }

    public boolean isValidPassword(String passwordToTest) {
        return StringUtils.equals(this.password, passwordToTest);
    }

    @Deprecated
    /**
     * Si à terme on veut vouloir utiliser des mot de passes cryptés en base, il ne faut plus y accéder.
     * isValidPassword() peut remplacer une partie des appels.
     * 
     */
    public String getPassword() {
        return this.password;
    }

}
