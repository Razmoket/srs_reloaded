package fr.afnic.commons.services.jooq;

import java.math.BigInteger;
import java.sql.Connection;

import org.jooq.SQLDialect;
import org.jooq.impl.Factory;

import fr.afnic.commons.beans.billing.BillableTicketInfo;
import fr.afnic.commons.beans.billing.Command;
import fr.afnic.commons.beans.billing.CommandId;
import fr.afnic.commons.beans.customer.Customer;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.IBillingService;
import fr.afnic.commons.services.exception.ConnectionException;
import fr.afnic.commons.services.exception.NotImplementedException;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.jooq.stub.cegid.packages.PkgCegidUtils;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.utils.sql.ISqlConnectionFactory;
import fr.afnic.utils.sql.pool.PoolSqlConnectionFactoryFacade;
import fr.afnic.utils.sql.pool.SqlDatabaseEnum;

public class JooqBillingService implements IBillingService {

    private final ISqlConnectionFactory sqlConnectionFactory;

    public JooqBillingService(ISqlConnectionFactory sqlConnectionFactory) {
        this.sqlConnectionFactory = sqlConnectionFactory;
    }

    public JooqBillingService(SqlDatabaseEnum database, TldServiceFacade tld) throws ServiceException {
        this.sqlConnectionFactory = PoolSqlConnectionFactoryFacade.getSqlPoolConnectionFactory(database, tld);
    }

    protected Factory createFactory() throws ConnectionException {
        Connection connection = this.sqlConnectionFactory.createConnection();
        return new Factory(connection, SQLDialect.ORACLE);
    }

    protected void closeFactory(Factory factory) throws ConnectionException {
        this.sqlConnectionFactory.closeConnection(factory.getConnection());
    }

    @Override
    public void updateCustomer(Customer customer, UserId userId, TldServiceFacade tld) throws ServiceException {
        System.err.println("===============>>>>> try to update cegid");
    }

    @Override
    public CommandId createCommand(Command command, TldServiceFacade tld) throws ServiceException {
        throw new NotImplementedException();
    }

    @Override
    public void createCommand(BillableTicketInfo billableTicketInfo, UserId userId, TldServiceFacade tld) throws ServiceException {

        Factory factory = this.createFactory();
        try {

            BigInteger newId = PkgCegidUtils.createOperationMvt(factory,
                                                                billableTicketInfo.getTicketId(),
                                                                billableTicketInfo.getDomainName(),
                                                                null,
                                                                billableTicketInfo.getArticle(),
                                                                billableTicketInfo.getTld(),
                                                                billableTicketInfo.getBilledCustomer(),
                                                                billableTicketInfo.getPayersCustomer(),
                                                                new java.sql.Date(billableTicketInfo.getCommandDate().getTime()),
                                                                "");

        } catch (Exception e) {
            throw new ServiceException("getNicHandlesToSurvey() failed", e);
        } finally {
            this.closeFactory(factory);
        }
    }
}
