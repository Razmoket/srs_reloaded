/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/services/mock/ScreenLoggerService.java#5 $
 * $Revision: #5 $
 * $Author: barriere $
 */

package fr.afnic.commons.services.mock;

import java.util.Date;
import java.util.List;

import fr.afnic.commons.beans.logs.ILogger;
import fr.afnic.commons.beans.logs.Level;
import fr.afnic.commons.beans.logs.Log;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.ILoggerService;
import fr.afnic.commons.services.exception.NotImplementedException;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

/**
 * Service retournant des logger simples qui écrivent les messages dans System.out et System.err
 * 
 */
public class ScreenLoggerService implements ILoggerService {

    @Override
    public void configure(String file) throws ServiceException {

    }

    @Override
    public ILogger getLogger(Class<?> clazz) {
        return new ScreenLogger(clazz);
    }

    @Override
    public List<Log> searchLog(Date start, Date stop, Level minLevel, List<String> threads, String logger, String patternMessage) throws ServiceException {
        throw new NotImplementedException();
    }

    @Override
    public void logActivity(String actionId, String lastUrl, String actualUrl, UserId userId, TldServiceFacade tld) {
        // TODO Auto-generated method stub

    }

}
