package fr.afnic.utils.sql;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fr.afnic.commons.beans.logs.ILogger;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.utils.sql.pool.LockableConnection;

public final class ConnectionGarbageCollector extends Thread {

    private static final ILogger LOGGER = AppServiceFacade.getLoggerService().getLogger(ConnectionGarbageCollector.class);

    public List<LockableConnection> connectionsToClose = new ArrayList<LockableConnection>();

    public ConnectionGarbageCollector() {
        this.start();
    }

    @Override
    public void run() {
        while (true) {
            boolean hasConnectionToClose = !this.connectionsToClose.isEmpty();

            if (hasConnectionToClose) {
                LockableConnection connection = this.connectionsToClose.get(0);
                try {
                    connection.closeConnection();
                } catch (SQLException e) {
                    if (e.getErrorCode() == 17002) {
                        LOGGER.trace("Connection timeout: " + e.getMessage());
                    } else {
                        LOGGER.warn("garbageObsoleteConnections() failed to close a connection", e);
                    }
                }
            }

            if (hasConnectionToClose) {
                synchronized (this) {
                    this.connectionsToClose.remove(0);
                }
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void garbage(LockableConnection connection) {
        synchronized (this) {
            this.connectionsToClose.add(connection);
        }
    }

}
