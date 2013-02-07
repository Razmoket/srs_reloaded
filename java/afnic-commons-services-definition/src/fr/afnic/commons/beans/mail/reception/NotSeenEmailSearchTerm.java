/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/beans/mail/reception/NotSeenEmailSearchTerm.java#2 $
 * $Revision: #2 $
 * $Author: barriere $
 */

package fr.afnic.commons.beans.mail.reception;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Flags.Flag;
import javax.mail.search.SearchTerm;

import fr.afnic.commons.beans.logs.ILogger;
import fr.afnic.commons.services.facade.AppServiceFacade;

/**
 * Critère de recherche correspondant aux mails non lus.
 * 
 * @author ginguene
 * 
 */
public class NotSeenEmailSearchTerm extends SearchTerm {

    private static final long serialVersionUID = 1L;

    protected static final ILogger LOGGER = AppServiceFacade.getLoggerService().getLogger(NotSeenEmailSearchTerm.class);

    @Override
    public boolean match(Message message) {
        try {
            return !message.isSet(Flag.SEEN);
        } catch (MessagingException e) {
            LOGGER.error("Error while matching message", e);
            return false;
        }
    }
}
