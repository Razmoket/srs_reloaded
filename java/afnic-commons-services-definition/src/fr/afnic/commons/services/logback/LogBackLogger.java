/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/services/logback/LogBackLogger.java#17 $
 * $Revision: #17 $
 * $Author: ginguene $
 */

package fr.afnic.commons.services.logback;

import org.slf4j.Logger;

import fr.afnic.commons.beans.logs.ILogger;
import fr.afnic.commons.beans.logs.LogMonitor;

/**
 * Implémentation du ILogger basé sur la librairie logback.
 * 
 * @author ginguene
 * 
 */
public class LogBackLogger implements ILogger {

    private Logger logger;

    private static final LogMonitor MONITOR = new LogMonitor();

    public LogBackLogger(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void debug(String message, Exception e) {
        this.logger.debug(message, e);
    }

    @Override
    public void debug(String message) {
        this.logger.debug(message);
    }

    @Override
    public void warn(String message, Exception e) {
        this.logger.warn(message, e);

    }

    @Override
    public void warn(String message) {
        this.logger.warn(message);

    }

    @Override
    public void error(String message, Exception e) {
        this.logger.error(message, e);
        MONITOR.error(message, e);
    }

    @Override
    public void error(String message) {
        this.logger.error(message);

    }

    @Override
    public void info(String message, Exception e) {
        this.logger.info(message, e);
    }

    @Override
    public void info(String message) {
        this.logger.info(message);
    }

    @Override
    public void trace(String message, Exception e) {
        this.logger.trace(message, e);
    }

    @Override
    public void trace(String message) {
        this.logger.trace(message);
    }

    @Override
    public boolean isDebugEnabled() {
        return logger.isDebugEnabled();
    }

    @Override
    public boolean isInfoEnabled() {
        return logger.isInfoEnabled();
    }

    @Override
    public boolean isTraceEnabled() {
        return logger.isTraceEnabled();
    }

    @Override
    public String getName() {
        return logger.getName();
    }

}
