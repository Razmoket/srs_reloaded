package fr.afnic.utils.sql.pool;

public class LockableConnectionException extends Exception {

    private LockableConnection lockableConnection = null;

    public LockableConnectionException(LockableConnection lockableConnection) {
        super("LockableConnectionException");
        this.lockableConnection = lockableConnection;
    }

    public LockableConnection getLockableConnection() {
        return this.lockableConnection;
    }

}
