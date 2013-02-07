/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/beans/Authorization.java#15 $
 * $Revision: #15 $
 * $Author: barriere $
 */

package fr.afnic.commons.beans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.utils.DateUtils;

/**
 * Code d'autorisation permettant de demander un nom de domaine protégé
 * 
 * @author ginguene
 * 
 */
public class Authorization implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Format de date utilisé pour retournée les date au format text dans les methodes de type getDateStr() */
    private static final String DATE_FORMAT = "dd/MM/yyyy";

    /**
     * Nombre de jour durant lesquels un code d'autorisation est valide.<br/>
     * Cette variable est utilisée pour calculer la date d'expiration.
     */
    private static final int NB_DAY_VALIDITY = 15;

    /** Identificant de l'autorisation, correspond à l'id dans la table nicope.autorisation */
    private int id;

    /** Nom de domaine pour lequel on génère un code d'autorisation. */
    private String domainName;

    /** Date de création du code d'autorisation */
    private Date createDate;

    /**
     * Date de validité du code d'autorisation.<br/>
     * Après cette date le système ne permet plus d'utiliser le code
     */
    private Date expirationDate;

    /**
     * Date d'utilisation du code d'autorisation.<br/>
     * null tant que le code n'a pas été utilisé.
     */
    private Date useDate;

    private boolean actif = true;

    /**
     * Code du bureau d'enregistrement pour lequel le code est attribué.<br/>
     * Il sera le seul a pouvoir utiliser ce code.
     */
    private String registrarCode;

    private String holderHandle;

    private WhoisContact holder;
    private AuthorizationOperation operation;

    // Code d'autorisation
    private String code;

    /** le user appelant l'objet */
    protected final UserId userIdCaller;
    /** le tld sur lequel on appelle l'objet */
    protected final TldServiceFacade tldCaller;

    /**
     * Constructeur par défaut.<br>
     * initialise la date de création avec la date courante.
     * 
     */
    public Authorization(UserId userId, TldServiceFacade tld) {
        this.userIdCaller = userId;
        this.tldCaller = tld;
        this.setCreateDate(new Date());
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDomainName() {
        return this.domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    /**
     * Retourne un clone de la date de création ou<br/>
     * null si la date de création est null.
     * 
     * @return un clone de la date de création
     */
    public Date getCreateDate() {
        if (this.createDate != null) {
            return (Date) this.createDate.clone();
        } else {
            return null;
        }
    }

    /**
     * Initialise la date de création du code d'autorisation<br/>
     * avec un clone du parametre.<br/>
     * Initialise également la date de validité du code d'autorisation<br/>
     * avec createDate + Authorization.NB_DAY_VALIDITY.<br/>
     * 
     * @param createDate
     *            Date de création du code d'autorisation.
     * 
     * @see Authorization.NB_DAY_VALIDITY
     * 
     */
    public void setCreateDate(Date createDate) {
        if (createDate != null) {
            this.createDate = (Date) createDate.clone();

            Calendar calendar = new GregorianCalendar();
            calendar.setTime(this.createDate);
            calendar.add(Calendar.DATE, Authorization.NB_DAY_VALIDITY);
            this.setExpirationDate(calendar.getTime());
        } else {
            createDate = null;
        }
    }

    /**
     * Retourne le code d'autorisation affichée en séparant le code <br/>
     * en bloques de 8 caractères séparés par des <code>-</code>
     * 
     * @return le code d'autorisation sous une forme plus lisible
     */
    public String getCodeStr() {
        return this.code.substring(0, 8)
               + "-" + this.code.substring(8, 16)
               + "-" + this.code.substring(16, 24)
               + "-" + this.code.substring(24, 32);
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public AuthorizationOperation getOperation() {
        return this.operation;
    }

    public void setOperation(AuthorizationOperation operation) {
        this.operation = operation;
    }

    public String getRegistrarCode() {
        return this.registrarCode;
    }

    public void setRegistrarCode(String registrarCode) {
        this.registrarCode = registrarCode;
    }

    /**
     * Interroge le service de contacts à partir du<br/>
     * nichandle du titulaire pour rennvoyer l'objet WhoisContact<br/>
     * correspondant au titulaire.
     * 
     * @return un objet WhoisContact correspondant au titulaire
     * @throws ServiceException
     *             si le service rencontre un problème
     * 
     * @see fr.afnic.commons.beans.WhoisContact
     */
    public WhoisContact getHolder() throws ServiceException {
        if (this.holder == null) {
            this.holder = AppServiceFacade.getWhoisContactService()
                                          .getContactWithHandle(this.holderHandle, this.userIdCaller, this.tldCaller);
        }
        return this.holder;
    }

    public void setHolder(WhoisContact holder) {
        this.holder = holder;
    }

    public String getHolderHandle() {
        return this.holderHandle;
    }

    public void setHolderHandle(String holderHandle) {
        this.holderHandle = holderHandle;
    }

    /**
     * Retourne un clone date validité du code d'autorisation ou null si la date est null 
     */
    public Date getExpirationDate() {
        return DateUtils.clone(this.expirationDate);
    }

    /**
     * Initialise la date de validité avec un clone du parametre.<br/>
     * A noter que la date de validité est également automatiquement initialisée,<br/>
     * lorsque l'on initialise la date de création.
     * 
     * @param validityDate
     *            Date de validité du code d'autorisation
     */
    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = DateUtils.clone(expirationDate);
    }

    public boolean isActif() {
        return this.actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    /**
     * Retourne un clone de la date d'utilisation du code d'autorisation ou null<br/>
     * si ce dernier est null.
     * 
     * @return Un clone de la date d'utilisation du code d'autorisation
     */
    public Date getUseDate() {
        return DateUtils.clone(this.useDate);
    }

    /**
     * Initialise la date de validité avec un clone du parametre.<br/>
     * 
     * 
     * @param dateOfUse
     *            Date d'utilisation du code d'autorisation
     */
    public void setUseDate(Date useDate) {
        this.useDate = DateUtils.clone(useDate);
    }

    /**
     * Indique si le code d'autorisation a déja été utilisé.
     * 
     * @return true si le code d'autorisation a déja été utilisé.
     */
    public boolean hasBeenUsed() {
        return this.useDate != null;
    }

    /**
     * Indique si le code d'autorisation n'a pas été utilisé.
     * 
     * @return false si le code d'autorisation a déja été utilisé.
     */
    public boolean hasNotBeenUsed() {
        return !this.hasBeenUsed();
    }

    /**
     * Indique si une autorisation est toujours valide
     * 
     * @return True si le code est toujours valide
     */
    public boolean isAlwaysValid() {
        return new Date().before(this.getExpirationDate()) && this.hasNotBeenUsed();
    }

    /**
     * Indique si une autorisation n'est plus valide
     * 
     * @return False si le code est toujours valide
     */
    public boolean isNoLongerValid() {
        return !this.isAlwaysValid();
    }

    /**
     * Indique si le champs operation de l'autorisation a été initialisé.<br/>
     * 
     * 
     * @return True si le champs operation de l'autorisation a été initialisé.
     */
    public boolean hasOperation() {
        return this.operation != null;
    }

    public UserId getUserIdCaller() {
        return this.userIdCaller;
    }

}
