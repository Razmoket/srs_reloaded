/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.beans.mail.template;

import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

public abstract class EmailTemplateSender {

    public abstract void send(UserId userId, TldServiceFacade tld) throws ServiceException;

}
