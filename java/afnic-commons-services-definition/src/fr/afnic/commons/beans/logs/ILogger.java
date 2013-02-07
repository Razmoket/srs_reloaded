/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/beans/logs/ILogger.java#2 $
 * $Revision: #2 $
 * $Author: ginguene $
 */

package fr.afnic.commons.beans.logs;

public interface ILogger {

    public void trace(String message, Exception e);

    public void debug(String message, Exception e);

    public void info(String message, Exception e);

    public void warn(String message, Exception e);

    public void error(String message, Exception e);

    public void trace(String message);

    public void debug(String message);

    public void info(String message);

    public void warn(String message);

    public void error(String message);

    public boolean isDebugEnabled();

    public boolean isTraceEnabled();

    public boolean isInfoEnabled();

    public String getName();

}
