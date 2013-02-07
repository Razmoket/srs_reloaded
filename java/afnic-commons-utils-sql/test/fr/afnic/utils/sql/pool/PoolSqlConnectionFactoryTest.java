package fr.afnic.utils.sql.pool;

import java.sql.Connection;

import junit.framework.TestCase;

import org.junit.Test;

import fr.afnic.commons.beans.Authentification;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.utils.sql.SqlFacade;
import fr.afnic.utils.sql.pool.LockableConnection;
import fr.afnic.utils.sql.pool.PoolSqlConnectionConfiguration;
import fr.afnic.utils.sql.pool.PoolSqlConnectionFactory;

public class PoolSqlConnectionFactoryTest {

    @Test
    public void createConnection() throws Exception {
        Authentification auth = SqlFacade.getBoaAuthenfication();
        PoolSqlConnectionConfiguration conf = SqlFacade.getConfiguration();

        PoolSqlConnectionFactory factory = new PoolSqlConnectionFactory(auth, conf);

        LockableConnection connection = (LockableConnection) factory.createConnection();
        TestCase.assertTrue(connection.isLocked());
    }

    @Test
    public void createConnectionAfterClosing() throws Exception {
        Authentification auth = SqlFacade.getBoaAuthenfication();
        PoolSqlConnectionConfiguration conf = SqlFacade.getConfiguration();

        PoolSqlConnectionFactory factory = new PoolSqlConnectionFactory(auth, conf);

        Connection connection1 = factory.createConnection();
        factory.closeConnection(connection1);

        Connection connection2 = factory.createConnection();
        TestCase.assertTrue(connection1 == connection2);
    }

    @Test
    public void createConnectionTwice() throws Exception {
        Authentification auth = SqlFacade.getBoaAuthenfication();
        PoolSqlConnectionConfiguration conf = SqlFacade.getConfiguration();

        PoolSqlConnectionFactory factory = new PoolSqlConnectionFactory(auth, conf);

        LockableConnection connection1 = (LockableConnection) factory.createConnection();
        LockableConnection connection2 = (LockableConnection) factory.createConnection();
        TestCase.assertTrue(connection1 != connection2);
    }

    @Test
    public void createConnectionWithFullPool() throws Exception {
        Authentification auth = SqlFacade.getBoaAuthenfication();
        PoolSqlConnectionConfiguration conf = SqlFacade.getConfiguration();
        conf.setPoolSize(1);

        PoolSqlConnectionFactory factory = new PoolSqlConnectionFactory(auth, conf);

        long expectedLockTime = 1000;
        LockableConnection connection1 = (LockableConnection) factory.createConnection();

        new ConnectionUnlocker(connection1, expectedLockTime).start();

        long start = System.currentTimeMillis();
        factory.createConnection();
        long stop = System.currentTimeMillis();

        long actualLockTime = stop - start;
        TestCase.assertTrue("actualLockTime: " + actualLockTime + "; expectedLockTime:" + expectedLockTime,
                            actualLockTime >= expectedLockTime);

    }

    @Test
    public void createConnectionWitAllPoolObsolere() throws Exception {
        Authentification auth = SqlFacade.getBoaAuthenfication();
        PoolSqlConnectionConfiguration conf = SqlFacade.getConfiguration();
        conf.setPoolSize(1);
        conf.setMaxUsePerConnection(1);

        PoolSqlConnectionFactory factory = new PoolSqlConnectionFactory(auth, conf);
        LockableConnection connection = (LockableConnection) factory.createConnection();
        connection.close();
        TestCase.assertTrue(connection.isObsolete());

        connection = (LockableConnection) factory.createConnection();
        connection.close();

        factory.createConnection();

    }

    @Test
    public void createConnectionWithFullPoolAndTimeout() throws Exception {
        Authentification auth = SqlFacade.getBoaAuthenfication();
        PoolSqlConnectionConfiguration conf = SqlFacade.getConfiguration();
        conf.setPoolSize(1);
        long timeout = 500;
        conf.setTimeout(500);

        PoolSqlConnectionFactory factory = new PoolSqlConnectionFactory(auth, conf);

        LockableConnection connection1 = (LockableConnection) factory.createConnection();
        new ConnectionUnlocker(connection1, timeout * 10).start();

        long start = System.currentTimeMillis();
        try {

            factory.createConnection();

            TestCase.fail("Should thrown an exception");
        } catch (ServiceException e) {
            TestCase.assertEquals("No unlocked connection after " + 500 + " ms", e.getMessage());

            long waitTime = System.currentTimeMillis() - start;
            TestCase.assertTrue(waitTime >= timeout);
        }

    }
}
