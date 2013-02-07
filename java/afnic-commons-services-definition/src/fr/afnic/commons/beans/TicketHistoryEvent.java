/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/beans/TicketHistoryEvent.java#8 $
 * $Revision: #8 $
 * $Author: barriere $
 */

package fr.afnic.commons.beans;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import fr.afnic.commons.beans.logs.ILogger;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;

/**
 * Element de l'historique d'un ticket décrivant le passage d'un etat initiale à un état final.
 * 
 * @author ginguene
 * 
 */
public class TicketHistoryEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final ILogger LOGGER = AppServiceFacade.getLoggerService().getLogger(TicketHistoryEvent.class);
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");

    private Date date;
    private TicketStatus fromStatus;
    private TicketStatus toStatus;
    private String comment;
    private String user;

    /** le user appelant l'objet */
    protected final UserId userIdCaller;
    /** le tld sur lequel on appelle l'objet */
    protected final TldServiceFacade tldCaller;

    public TicketHistoryEvent(UserId userId, TldServiceFacade tld) {
        this.userIdCaller = userId;
        this.tldCaller = tld;
    }

    /**
     * retourne une chaine de caractère correspondant à la date de creation. On peut definir le format de cette date via la methode setDateFormat
     * 
     * @return
     */
    public String getDateStr() {
        if (this.getDate() == null) {
            return "";
        } else {
            return TicketHistoryEvent.dateFormat.format(this.getDate());
        }
    }

    public Date getDate() {
        if (this.date != null) {
            return (Date) this.date.clone();
        } else {
            return null;
        }
    }

    public void setDate(Date date) {
        if (date != null) {
            this.date = (Date) date.clone();
        } else {
            this.date = null;
        }
    }

    public TicketStatus getFromStatus() {
        return this.fromStatus;
    }

    public void setFromStatus(TicketStatus fromStatus) {
        this.fromStatus = fromStatus;
    }

    public void setFromStatus(String fromStatusStr) {
        this.fromStatus = TicketStatus.valueOf(fromStatusStr);
    }

    public TicketStatus getToStatus() {
        return this.toStatus;
    }

    public void setToStatus(TicketStatus toStatus) {
        this.toStatus = toStatus;
    }

    public boolean setToStatus(String toStatusStr) {
        try {
            this.toStatus = TicketStatus.valueOf(toStatusStr);
            return true;
        } catch (Exception e) {
            LOGGER.error("Unknown status:" + toStatusStr);
            return false;
        }
    }

    public String getComment() {
        return this.comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * Indique si l'evenement dispose d'un commentaire.
     * 
     * @return true si l'evenement dispose d'un commentaire.
     */
    public boolean hasComment() {
        return this.comment != null && !this.comment.isEmpty();
    }

    public String getUser() {
        return this.user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    /**
     * Retourne le texte en français decrivant le status final de l'historique du ticket.<br/>
     * Pour cela on consulte le dictionnaire des status.
     * 
     * @return {@link String}
     * @throws ServiceException
     *             Si l'operation échoue.
     */
    public String getFromStatusDescription() throws ServiceException {
        if (this.fromStatus == null) {
            return "";
        } else {
            return this.fromStatus.getDescription(this.userIdCaller, this.tldCaller);
        }
    }

    /**
     * Retourne le texte en français decrivant le status initial de l'historique du ticket. Pour cela on consulte le dictionnaire des status.
     * 
     * @return {@link String}
     * @throws ServiceException
     *             Si l'operation échoue.
     * @throws
     */
    public String getToStatusDescription() throws ServiceException {
        if (this.toStatus == null) {
            return "";
        } else {
            return this.toStatus.getDescription(this.userIdCaller, this.tldCaller);
        }
    }

    public boolean hasToStatus() {
        return this.toStatus != null;
    }
}
