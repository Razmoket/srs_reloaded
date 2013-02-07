/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/beans/notifications/EmailNotification.java#3 $
 * $Revision: #3 $
 * $Author: ginguene $
 */

package fr.afnic.commons.beans.notifications;

import fr.afnic.commons.beans.mail.SentEmail;

/**
 * Notification envoyée sous forme de mail par la gateway
 * 
 * @author ginguene
 * 
 */
public class EmailNotification extends Notification {

    private SentEmail sentMail = null;

    public EmailNotification(SentEmail sentMail) {
        this.sentMail = sentMail;
    }

    public SentEmail getSentMail() {
        return sentMail;
    }

}
