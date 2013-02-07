/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/beans/mail/reception/EmailIdSearchTerm.java#2 $
 * $Revision: #2 $
 * $Author: barriere $
 */

package fr.afnic.commons.beans.mail.reception;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.search.SearchTerm;

import fr.afnic.commons.beans.logs.ILogger;
import fr.afnic.commons.services.facade.AppServiceFacade;

/**
 * Represente un critère de recherche des mail par mailId
 * 
 * @author ginguene
 * 
 */
public class EmailIdSearchTerm extends SearchTerm {

    private static final long serialVersionUID = 1L;

    private static final ILogger LOGGER = AppServiceFacade.getLoggerService().getLogger(EmailIdSearchTerm.class);

    private String mailId = null;

    public EmailIdSearchTerm(String mailId) {
        this.mailId = mailId;

    }

    @Override
    public boolean match(Message message) {
        try {
            return this.mailId.equals(message.getHeader("Message-ID")[0]);
        } catch (MessagingException e) {
            LOGGER.error("Error while matching message", e);
            return false;
        }
    }
}
