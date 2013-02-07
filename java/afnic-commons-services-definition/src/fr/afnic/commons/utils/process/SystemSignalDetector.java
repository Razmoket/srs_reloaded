/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.utils.process;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SystemSignalDetector extends SignalDetector implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(SystemSignalDetector.class);

    public boolean hasReceivedStopSignal;

    public SystemSignalDetector() {
        Runtime.getRuntime().addShutdownHook(new ShutdownHook());
    }

    @Override
    protected boolean receivedStopSignal() {
        return hasReceivedStopSignal;
    }

    @Override
    protected boolean receivedImmediatStopSignal() {
        return false;
    }

    @Override
    protected boolean receivedWakeUpSignal() {
        return false;
    }

    class ShutdownHook extends Thread {
        @Override
        public void run() {
            super.run();
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("ShutdownHook called.");
            }
            hasReceivedStopSignal = true;
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("ShutdownHook's job is finished.");
            }
        }
    }
}
