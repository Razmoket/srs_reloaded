/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.services.logback;

import fr.afnic.commons.beans.logs.ILogger;

public class LogFilter {

    public boolean hasToBeLog(ILogger logger) {
        return !logger.getName().startsWith("org.apache.wicket");
    }
}
