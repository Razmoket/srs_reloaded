/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/beans/request/RequestHistoryEvent.java#7 $
 * $Revision: #7 $
 * $Author: barriere $
 */

package fr.afnic.commons.beans.request;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;

/**
 * Garde l'historique des changements des requetes
 * 
 * @author ginguene
 * 
 */
public class RequestHistoryEvent implements Serializable {

    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    /** Contient le nom de la classe de request (ex: IdentificationRequest) */
    private String requestType;

    /** Contient l'identificatn de la requete. */
    private int requestId;
    private String oldValue;
    private String newValue;
    private Date date;
    private String comment;
    private String user;
    private IRequestHistoryEventField field;

    /** Identificant de l'évenement d'historique */
    private int id;

    private Request request = null;

    /**
     * Retourne un clone de la date de l'evenement
     * 
     * @return Un clone de la date de l'evenement
     */
    public Date getDate() {
        if (this.date != null) {
            return (Date) this.date.clone();
        } else {
            return null;
        }
    }

    /**
     * Chaine correspondant à la date.
     * 
     * @return Chaine correspondant à la date
     */
    public String getDateStr() {
        if (this.date != null) {
            return FORMAT.format(this.date);
        } else {
            return "";
        }
    }

    /**
     * Initialise la date avec un clone du parametre.
     * 
     * @param date
     *            Nouvelle valeur de la date.
     */
    public void setDate(Date date) {
        if (date != null) {
            this.date = (Date) date.clone();
        } else {
            this.date = null;
        }
    }

    public String getComment() {
        return this.comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUser() {
        return this.user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getOldValue() {
        return this.oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public String getNewValue() {
        return this.newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    public String getRequestType() {
        return this.requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRequestId() {
        return this.requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public Request getRequest(UserId userId, TldServiceFacade tld) throws ServiceException {
        if (this.request == null) {
            this.request = AppServiceFacade.getRequestService()
                                           .getRequestById(this.requestType,
                                                           this.requestId,
                                                           userId, tld);

        }
        return this.request;
    }

    public IRequestHistoryEventField getField() {
        return this.field;
    }

    public void setField(IRequestHistoryEventField field) {
        this.field = field;
    }

    @Override
    public String toString() {
        return "id=" + this.requestId + "; "
               + "type=" + this.requestType + "; "
               + "field=" + this.field + "; "
               + "old value=" + this.oldValue + "; "
               + "new value=" + this.newValue + "; "
               + "user=" + this.user;
    }

}
