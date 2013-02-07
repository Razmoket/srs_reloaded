/*
 * $Id: //depot/main/java/app-injector/src/fr/afnic/application/injector/SignalDetector.java#12 $
 * $Revision: #12 $
 * $Author: ginguene $
 */

package fr.afnic.commons.utils.process;

import fr.afnic.commons.beans.logs.ILogger;
import fr.afnic.commons.services.facade.AppServiceFacade;

/**
 * Verifie la reception de signaux envoyer au process. Si un signal est detecte on stop ou reveille l'injecteur
 * 
 * @author ginguene
 * 
 */
public abstract class SignalDetector implements Runnable {

    private static final ILogger LOGGER = AppServiceFacade.getLoggerService().getLogger(SignalDetector.class);

    private static final long SLEEP_DELAY = 1000;

    private Process process = null;

    private boolean started;

    public final void start(Process process) {
        Thread thread = new Thread(this);

        this.process = process;
        thread.start();

    }

    @Override
    public final void run() {
        Thread.currentThread().setName(process.getName() + "-" + this.getClass().getSimpleName());
        LOGGER.debug("Start signal detection: " + this.toString());
        started = true;
        while (started) {
            this.sleep();
            try {
                this.detectSignals();
            } catch (ProcessException e) {
                throw new RuntimeException("detectSignal() failed", e);
            }
        }
    }

    protected final void sleep() {
        try {
            Thread.sleep(SLEEP_DELAY);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected final void stopDetection() {
        this.started = false;
    }

    @edu.umd.cs.findbugs.annotations.SuppressWarnings(value = "DM_EXIT",
            justification = "L'utilisation de System.exit() est maitrisée ici.")
    protected final void detectSignals() throws ProcessException {
        if (this.receivedStopSignal()) {
            LOGGER.debug("Detect stop signal");
            this.process.stop();
            stopDetection();
        }

        if (this.receivedImmediatStopSignal()) {
            LOGGER.debug("Detect immediat stop signal");
            stopDetection();
            System.exit(0);
        }

        if (this.receivedWakeUpSignal()) {
            LOGGER.debug("Detect wakeUp signal");
            this.process.wakeUp();
        }

    }

    protected abstract boolean receivedStopSignal();

    protected abstract boolean receivedImmediatStopSignal();

    protected abstract boolean receivedWakeUpSignal();

}
