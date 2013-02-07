/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.services.mock;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import junit.framework.TestCase;
import fr.afnic.commons.beans.logs.ILogger;
import fr.afnic.commons.beans.logs.Level;
import fr.afnic.commons.beans.logs.Log;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.ILoggerService;
import fr.afnic.commons.services.exception.NotImplementedException;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.logback.LogBackLoggerService;
import fr.afnic.commons.services.tld.TldServiceFacade;

/**
 * Service Mock de log qui permet de voir si de message d'erreur ou de warning ont été remontés.
 * 
 * @author ginguene
 * 
 */
public class MockLoggerService implements ILoggerService {

    private final List<String> errors = new ArrayList<String>();
    private final List<String> warns = new ArrayList<String>();

    @Override
    public void configure(String file) throws ServiceException {
    }

    @Override
    public ILogger getLogger(Class<?> clazz) {
        return new MockLogger(clazz, this);
    }

    @Override
    public List<Log> searchLog(Date start, Date stop, Level minLevel, List<String> threads, String logger, String patternMessage) throws ServiceException {
        throw new NotImplementedException();
    }

    public void addWarn(String message) {
        this.warns.add(message);
    }

    public void addError(String message) {
        this.errors.add(message);
    }

    /**
     * Vérifie qu'aucun log d'erreur ou de warn n'ont été remontés.
     */
    public void assertHasNoErrorsOrWarns() {
        if (!this.errors.isEmpty()) {
            TestCase.fail("Logs contains errors: " + this.errors.toString());
        }
        if (!this.warns.isEmpty()) {
            TestCase.fail("Logs contains warns: " + this.warns.toString());
        }
    }

    /**
     * Vide le contenu des liste de warning et erreurs reçus
     */
    public void clear() {
        this.errors.clear();
        this.warns.clear();
    }

    @Override
    public void logActivity(String actionId, String lastUrl, String actualUrl, UserId userId, TldServiceFacade tld) {

        new LogBackLoggerService().logActivity(actionId, lastUrl, actualUrl, userId, tld);

    }

}
