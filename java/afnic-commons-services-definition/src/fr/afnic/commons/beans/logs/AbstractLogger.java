/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.beans.logs;

/**
 * 
 * Implémentation de ILogger qui définit toutes les méthodes de log comme ne faisant rien. <br/>
 * Cela permet dans une sous-classe interréssée par uniquement<br/>
 * par quelques méthodes de ne redéfinir que celles là.<br/>
 * 
 * @author ginguene
 * 
 */
public abstract class AbstractLogger implements ILogger {

    @Override
    public void trace(String message, Exception e) {

    }

    @Override
    public void debug(String message, Exception e) {

    }

    @Override
    public void info(String message, Exception e) {

    }

    @Override
    public void warn(String message, Exception e) {

    }

    @Override
    public void error(String message, Exception e) {

    }

    @Override
    public void trace(String message) {

    }

    @Override
    public void debug(String message) {

    }

    @Override
    public void info(String message) {

    }

    @Override
    public void warn(String message) {

    }

    @Override
    public void error(String message) {

    }

    @Override
    public boolean isDebugEnabled() {
        return true;
    }

    @Override
    public boolean isTraceEnabled() {
        return true;
    }

    @Override
    public boolean isInfoEnabled() {
        return true;
    }

}
