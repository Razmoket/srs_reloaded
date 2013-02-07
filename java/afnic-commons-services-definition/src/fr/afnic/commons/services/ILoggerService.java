/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/services/ILoggerService.java#5 $
 * $Revision: #5 $
 * $Author: barriere $
 */

package fr.afnic.commons.services;

import java.util.Date;
import java.util.List;

import fr.afnic.commons.beans.logs.ILogger;
import fr.afnic.commons.beans.logs.Level;
import fr.afnic.commons.beans.logs.Log;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

/**
 * Data Access Object permettant de configuer les logs, de récupérer un logger et d'effectuer des recherches dans les logs.
 * 
 * @author ginguene
 * 
 */
public interface ILoggerService {

    /**
     * Ibnitialise le systeme de log depuis un fichier
     * 
     * @param file
     * @throws ServiceException
     */
    public void configure(String file) throws ServiceException;

    /**
     * Retourne un logger pour une classe donnée
     * 
     * @param clazz
     * @return
     */
    public ILogger getLogger(Class<?> clazz);

    /**
     * Permet de récupérer des traces dans le gestionnaire de logs. Si l'on souhaite ne pas utiliser un critère de recherche, il suffit de le laisser
     * à null
     * 
     * @param start
     * @param stop
     * @param minLevel
     * @param threads
     * @param logger
     * @param patternMessage
     * @return
     * @throws ServiceException
     */
    public List<Log> searchLog(Date start, Date stop, Level minLevel, List<String> threads, String logger, String patternMessage) throws ServiceException;

    public void logActivity(String actionId, String lastUrl, String actualUrl, UserId userId, TldServiceFacade tld);

}
