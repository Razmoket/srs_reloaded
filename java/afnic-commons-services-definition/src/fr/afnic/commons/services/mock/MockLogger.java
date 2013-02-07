/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.services.mock;

/**
 * Classe notifiant au service de gestion de log, la reception de chaque message d'erreur ou de warn.
 * 
 * @author ginguene
 * 
 */
public class MockLogger extends ScreenLogger {

    private MockLoggerService mockLoggerService;

    public MockLogger(Class<?> clazz, MockLoggerService mockLoggerService) {
        super(clazz);
        this.mockLoggerService = mockLoggerService;
    }

    @Override
    public void warn(String message, Exception e) {
        super.warn(message, e);
        mockLoggerService.addWarn(message);
    }

    @Override
    public void error(String message, Exception e) {
        super.error(message, e);
        mockLoggerService.addError(message);
    }

    @Override
    public void warn(String message) {
        super.warn(message);
        mockLoggerService.addWarn(message);
    }

    @Override
    public void error(String message) {
        super.error(message);
        mockLoggerService.addError(message);
    }

}