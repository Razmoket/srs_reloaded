/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.beans.history;

import java.io.Serializable;
import java.util.Date;

import fr.afnic.utils.DateUtils;

/**
 * Représente un evenement d'historique.
 * 
 * @author ginguene
 * 
 */
public class HistoryEvent implements Serializable {

    private static final long serialVersionUID = 2411506184512269355L;

    private Date date;
    private String comment;
    private String userLogin;

    public Date getDate() {
        return DateUtils.clone(this.date);
    }

    public void setDate(Date date) {
        this.date = DateUtils.clone(date);
    }

    public String getComment() {
        return this.comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUserLogin() {
        return this.userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

}
