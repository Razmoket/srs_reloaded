/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/beans/domain/Domain.java#25 $
 * $Revision: #25 $
 * $Author: ginguene $
 */

package fr.afnic.commons.beans.agtf;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import fr.afnic.commons.beans.logs.ILogger;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;

/***
 * Representation d'un domaine.
 * 
 * @author ginguene
 * 
 */
public class AgtfHisto implements Serializable, Cloneable {

    protected UserId userId;
    protected Date dateComment;
    protected String comment;
    protected int version;
    protected int categoryWordId;

    protected static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    private static final long serialVersionUID = 1L;

    private static final ILogger LOGGER = AppServiceFacade.getLoggerService().getLogger(AgtfHisto.class);

    /** le user appelant l'objet */
    protected final UserId userIdCaller;
    /** le tld sur lequel on appelle l'objet */
    protected final TldServiceFacade tldCaller;

    public AgtfHisto(UserId userId, TldServiceFacade tld) {
        this.userIdCaller = userId;
        this.tldCaller = tld;
    }

    public UserId getUserId() {
        return this.userId;
    }

    public void setUserId(UserId userId) {
        this.userId = userId;
    }

    public Date getDateComment() {
        return this.dateComment;
    }

    public void setDateComment(Date dateComment) {
        this.dateComment = dateComment;
    }

    public String getComment() {
        return this.comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getVersion() {
        return this.version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getCategoryWordId() {
        return this.categoryWordId;
    }

    public void setCategoryWordId(int categoryWordId) {
        this.categoryWordId = categoryWordId;
    }
}
