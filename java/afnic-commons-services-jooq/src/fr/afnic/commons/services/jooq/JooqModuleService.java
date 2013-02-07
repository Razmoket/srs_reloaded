package fr.afnic.commons.services.jooq;

import java.sql.Connection;

import org.jooq.SQLDialect;
import org.jooq.impl.Factory;

import fr.afnic.commons.services.IModuleService;
import fr.afnic.commons.services.exception.ConnectionException;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.utils.sql.ISqlConnectionFactory;
import fr.afnic.utils.sql.pool.PoolSqlConnectionFactoryFacade;
import fr.afnic.utils.sql.pool.SqlDatabaseEnum;

public class JooqModuleService implements IModuleService {

    private final ISqlConnectionFactory sqlConnectionFactory;

    // private static final PostalAddressTable POSTAL_ADDRESS = PostalAddressTable.POSTAL_ADDRESS;

    public JooqModuleService(ISqlConnectionFactory sqlConnectionFactory) {
        this.sqlConnectionFactory = sqlConnectionFactory;
    }

    public JooqModuleService(SqlDatabaseEnum database, TldServiceFacade tld) throws ServiceException {
        this.sqlConnectionFactory = PoolSqlConnectionFactoryFacade.getSqlPoolConnectionFactory(database, tld);
    }

    protected Factory createFactory() throws ConnectionException {
        Connection connection = this.sqlConnectionFactory.createConnection();
        return new Factory(connection, SQLDialect.ORACLE);
    }

    protected void closeFactory(Factory factory) throws ConnectionException {
        this.sqlConnectionFactory.closeConnection(factory.getConnection());
    }

}
