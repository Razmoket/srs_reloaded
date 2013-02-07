/*
 * $Id: $ $Revision: $ $Author: $
 */

package fr.afnic.commons.beans.logs;

import java.net.InetAddress;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

import javax.xml.ws.WebServiceException;

import fr.afnic.commons.beans.application.env.Environnement;
import fr.afnic.commons.beans.mail.Email;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.utils.StackTraceUtils;

/**
 * Classe appelée par les logs qui en fonction du message envoi des alertes. <br/>
 * TODO utiliser un pattern decorator serait beaucoup plus sexy (mais pas le temps).
 * 
 * @author ginguene
 * 
 */
public class LogMonitor extends AbstractLogger {

    private long lastErrorTime = -1;
    private final long timeBetweenTwoAlerts = TimeUnit.MINUTES.toMillis(10);

    @Override
    public void error(String message, Exception e) {
        try {
            this.detectExternalDepedencyCrash(e);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void detectExternalDepedencyCrash(Exception e) {
        this.detectSoapGatewayCrash(e);
        this.detectDocushareCrash(e);
        this.detectScoreEtDecisionCrash(e);
        this.detectDatabaseCrash(e);
        this.detectUnclosedConnection(e);
    }

    private void detectDocushareCrash(Exception e) {
        if (e instanceof ServiceException) {
            String firstMessage = ((ServiceException) e).getFirstCauseMessage();
            if (firstMessage != null
                && (firstMessage.contains("no such object in table")
                || firstMessage.contains("Connection refused to host:")
                )) {
                this.sendAlertMail(firstMessage, "Docushare");
            }
        }
    }

    private void detectSoapGatewayCrash(Exception e) {

        if (e instanceof ServiceException) {
            String firstMessage = ((ServiceException) e).getFirstCauseMessage();
            if (firstMessage != null) {
                if (firstMessage.contains("Not implemented: The handler does not implement the method") || firstMessage.contains("logcroak")
                    || firstMessage.contains("Not a GLOB reference") || firstMessage.contains("Unsupported endpoint address")) {
                    this.sendAlertMail(firstMessage, "la gateway");
                }
            }
        }

        if (e instanceof WebServiceException && !e.getMessage().contains("populate")) {
            this.sendAlertMail(e.getMessage(), "la gateway");
        }

    }

    private void detectDatabaseCrash(Exception e) {
        if (e instanceof ServiceException) {
            Throwable firstCause = ((ServiceException) e).getFirstCause();

            if (firstCause instanceof SQLException) {
                SQLException sqlException = (SQLException) firstCause;
                if (sqlException != null && sqlException.getMessage() != null && sqlException.getMessage().contains("interrompue")) {
                    this.sendAlertMail(e.getMessage(), "La base de données");
                }
            }
        }

    }

    private void detectScoreEtDecisionCrash(Exception e) {

        if (e instanceof ServiceException) {
            String firstMessage = ((ServiceException) e).getFirstCauseMessage();
            if (firstMessage != null && firstMessage.contains("Can't connect to ws3.scores-decisions.com:80 (connect: timeout))")) {
                this.sendAlertMail(e.getMessage(), "Score & Decision");

            }
        }

    }

    private void detectUnclosedConnection(Exception e) {
        if ("LockableConnectionException".equalsIgnoreCase(e.getMessage())) {
            this.sendAlertMail(StackTraceUtils.getStackTrace(e), "la fermeture des connections ");
        }
    }

    private void sendAlertMail(String message, String dependencyName) {
        if (this.hasToSendAlert()) {

            Email mail = new Email();
            mail.setSubject("[" + AppServiceFacade.getApplicationService().getCurrentVersion().getEnvironnement().getName()
                            + "] Message automatique d'alerte " + AppServiceFacade.getApplicationService().getCurrentVersion().getApplication().getName());

            mail.setContent("Le message suivant a été relevé dans les logs de "
                            + AppServiceFacade.getApplicationService().getCurrentVersion().getDisplayName() + ":" + "\n" + message
                            + "\n\nEn général ce type de message signifie que " + dependencyName + " rencontre des difficultés."
                            + "\n\nL'erreur a été rencontré sur la machine " + getComputerFullName());
            Environnement env = AppServiceFacade.getApplicationService().getCurrentVersion().getEnvironnement();
            if (env.equals(Environnement.Prod) || env.equals(Environnement.Sandbox)) {
                mail.setToEmailAddresses("boa@nic.fr", "max@nic.fr");
            } else {
                mail.setToEmailAddresses("boa@nic.fr");
            }
            mail.setFromEmailAddress("boa@nic.fr");

            try {
                AppServiceFacade.getEmailService().sendEmail(mail, new UserId(1), TldServiceFacade.Fr);
            } catch (ServiceException e1) {
                // Comme on est dans le service de log, on ne fait pas appel à un logger, sinon on risquerait de boucler
                e1.printStackTrace();
            }

            this.lastErrorTime = System.currentTimeMillis();
        }
    }

    private boolean hasToSendAlert() {
        return this.lastErrorTime == -1 || ((System.currentTimeMillis() - this.lastErrorTime) > this.timeBetweenTwoAlerts);

    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    public static String getComputerFullName() {
        try {
            final InetAddress addr = InetAddress.getLocalHost();
            return addr.getHostName() + "[" + addr.getHostAddress() + "]";
        } catch (final Exception e) {
            return "undefined";
        }
    }

}
