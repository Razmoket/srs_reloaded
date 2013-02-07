package fr.afnic.utils.sql.pool;

import java.sql.Connection;
import java.sql.SQLException;

import junit.framework.TestCase;

import org.junit.Ignore;
import org.junit.Test;

import fr.afnic.utils.sql.pool.LockableConnection;

public class LockableConnectionTest {

    private static final long defaultTimeout = 6000;
    private static final int defaultMaxUseCount = 10;
    private static final Connection defaultConnection = new OpenedConnection();

    @Test
    public void lock() {
        LockableConnection conn = new LockableConnection(defaultConnection, defaultMaxUseCount, defaultTimeout);
        boolean isLock = conn.lock();

        TestCase.assertFalse(conn.isObsolete());
        TestCase.assertTrue(isLock);

        isLock = conn.lock();
        TestCase.assertFalse(isLock);
    }

    @Test
    public void unlock() {
        LockableConnection conn = new LockableConnection(defaultConnection, defaultMaxUseCount, defaultTimeout);
        boolean isLock = conn.lock();
        TestCase.assertTrue(isLock);

        conn.unlock();

        isLock = conn.lock();
        TestCase.assertTrue(isLock);
    }

    @Test
    public void lockAfterTooManyUse() {
        int maxUse = 4;
        LockableConnection conn = new LockableConnection(defaultConnection, maxUse, defaultTimeout);

        boolean isLock;

        for (int i = 0; i < maxUse; i++) {
            isLock = conn.lock();
            TestCase.assertTrue(isLock);
            conn.unlock();
        }

        isLock = conn.lock();
        TestCase.assertFalse(isLock);
    }

    @Test
    public void lockAfterTooManyTimeAfterCreate() throws InterruptedException {
        long timeout = 1000;

        LockableConnection conn = new LockableConnection(defaultConnection, defaultMaxUseCount, timeout);
        Thread.sleep(timeout + 100);

        TestCase.assertTrue(conn.isObsolete());
        TestCase.assertFalse(conn.lock());
    }

    @Test
    public void lockBeforeTooManyTimeAfterCreate() throws InterruptedException {
        long timeout = 1000;

        LockableConnection conn = new LockableConnection(defaultConnection, defaultMaxUseCount, timeout);
        Thread.sleep(timeout - 100);

        TestCase.assertFalse(conn.isObsolete());
        TestCase.assertTrue(conn.lock());
    }

    @Test
    @Ignore("on n'appelle plus isClosed() dans isObsolete()")
    public void lockWithClosedConnection() throws InterruptedException, SQLException {
        LockableConnection conn = new LockableConnection(new ClosedConnection(), defaultMaxUseCount, defaultTimeout);

        TestCase.assertTrue(conn.isObsolete());
        TestCase.assertFalse(conn.lock());
    }

    @Test
    public void lockWithOpenedConnection() throws InterruptedException {
        LockableConnection conn = new LockableConnection(new OpenedConnection(), defaultMaxUseCount, defaultTimeout);
        TestCase.assertFalse(conn.isObsolete());
        TestCase.assertTrue(conn.lock());
    }

    @Test
    public void isLockedSinceTooManyTime() throws InterruptedException {
        LockableConnection conn = new LockableConnection(new OpenedConnection(), defaultMaxUseCount, defaultTimeout);

        TestCase.assertFalse(conn.isObsolete());
        TestCase.assertFalse(conn.isLockedSinceTooManyTime());
    }

    @Test
    public void unlockAfterClose() throws Exception {
        LockableConnection conn = new LockableConnection(new OpenedConnection(), defaultMaxUseCount, defaultTimeout);
        conn.lock();
        conn.close();
        TestCase.assertFalse(conn.isLocked());
    }

}
