/*
 * $Id: LogBackLoggerDao.java,v 1.8 2010/07/20 06:24:44 ginguene Exp $
 * $Revision: 1.8 $
 * $Author: ginguene $
 */

package fr.afnic.commons.services.logback;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import fr.afnic.commons.beans.logs.ILogger;
import fr.afnic.commons.beans.logs.Level;
import fr.afnic.commons.beans.logs.Log;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.ILoggerService;
import fr.afnic.commons.services.exception.NotImplementedException;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

/**
 * Implémentation de ILoggerDao pour logguer via la librairie logback.<br/>
 * La page de cette librairie est la suivante: http://logback.qos.ch/
 * 
 * 
 * @author ginguene
 * 
 */
public class LogBackLoggerService implements ILoggerService {

    private static final File UNIX_LOG_DIR = new File("/usr/share/tomcat5/logs/");

    /**
     * Constructeur par défaut
     */
    public LogBackLoggerService() {
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        JoranConfigurator configurator = new JoranConfigurator();
        configurator.setContext(lc);
    }

    /**
     * Constructeur prenant le nom du fichier de configuration en parametre.
     * 
     * @param file
     *            Nom du fichier de configuration.
     * 
     * @throws ServiceException
     *             Si la configuration échoue.
     */
    public LogBackLoggerService(String file) throws ServiceException {
        this.configure(file);
    }

    @Override
    public void configure(String file) throws ServiceException {
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();

        try {
            JoranConfigurator configurator = new JoranConfigurator();
            configurator.setContext(lc);
            configurator.doConfigure(file);
        } catch (JoranException e) {
            throw new ServiceException("LogBackLoggerService cannot load configuration from " + file, e);
        }
    }

    @Override
    public ILogger getLogger(Class<?> clazz) {
        return new LogBackLogger(LoggerFactory.getLogger(clazz));
    }

    @Override
    public List<Log> searchLog(Date start,
                               Date stop,
                               Level minLevel,
                               List<String> threads,
                               String logger,
                               String patternMessage) throws ServiceException {
        throw new NotImplementedException();
    }

    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss:S");

    @Override
    public void logActivity(String actionId, String lastUrl, String actualUrl, UserId userId, TldServiceFacade tld) {
        try {
            File logFolder = this.getLogFolder();

            if (logFolder.exists() && userId != null) {
                String newLine = this.format.format(new Date()) + "; " + userId.getObjectOwner(userId, tld).getDisplayName() + "; " + lastUrl + "; " + actualUrl + "; " + actionId + "\n";

                System.err.print(newLine);

                this.getActivityWriter(logFolder).append(newLine);
                this.getActivityWriter(logFolder).flush();
            }
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private BufferedWriter activityWriter;

    private BufferedWriter getActivityWriter(File logFolder) throws IOException {
        if (this.activityWriter == null) {
            String filename = logFolder.getAbsolutePath() + "/activity.csv";
            this.activityWriter = new BufferedWriter(new FileWriter(filename, true));
        }
        return this.activityWriter;

    }

    private File getLogFolder() {
        if (System.getProperty("os.name").toUpperCase().contains("WIN")) {
            return new File("./");
        } else {
            return UNIX_LOG_DIR;
        }

    }
}
