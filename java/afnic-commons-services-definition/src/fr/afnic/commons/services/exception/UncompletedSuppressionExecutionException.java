/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/services/exception/UncompletedSuppressionExecutionException.java#3 $
 * $Revision: #3 $
 * $Author: ginguene $
 */

package fr.afnic.commons.services.exception;

import java.util.List;

import fr.afnic.commons.beans.notifications.Notification;

/**
 * Retourné lorsqu'une demande s'est executee partiellement. On peut accéder à la liste des elements qui ont réussi et à ceux qui ont échoués
 * 
 * @author ginguene
 * 
 */
public class UncompletedSuppressionExecutionException extends UncompletedExecutionException {

    private static final long serialVersionUID = 1L;

    private List<Notification> sentNotification;

    public UncompletedSuppressionExecutionException(List<Notification> sentNotification, List<String> succed, List<String> failed, String message, Exception exception) {
        super(succed, failed, message, exception);
        this.sentNotification = sentNotification;
    }

    public List<Notification> getSentNotification() {
        return sentNotification;
    }

    public UncompletedSuppressionExecutionException(List<Notification> sentNotification, List<String> succed, List<String> failed, String message) {
        super(succed, failed, message);
        this.sentNotification = sentNotification;
    }

}
