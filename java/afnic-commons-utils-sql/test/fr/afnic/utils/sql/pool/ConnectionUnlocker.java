package fr.afnic.utils.sql.pool;

import fr.afnic.utils.sql.pool.LockableConnection;

public class ConnectionUnlocker extends Thread {

    private final LockableConnection connection;
    private final long timeBeforeClosing;

    ConnectionUnlocker(LockableConnection connection, long timeBeforeClosing) {
        this.connection = connection;
        this.timeBeforeClosing = timeBeforeClosing;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(this.timeBeforeClosing);
            this.connection.close();
        } catch (Exception e) {
            throw new RuntimeException("run() failed", e);
        }
    }

}
