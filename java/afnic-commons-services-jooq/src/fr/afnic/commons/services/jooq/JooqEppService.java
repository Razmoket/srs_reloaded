package fr.afnic.commons.services.jooq;

import java.sql.Connection;
import java.util.List;

import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.Factory;

import fr.afnic.commons.beans.epp.EppMessage;
import fr.afnic.commons.beans.notifications.Notification;
import fr.afnic.commons.beans.operations.qualification.Qualification;
import fr.afnic.commons.beans.operations.qualification.QualificationStep;
import fr.afnic.commons.beans.operations.qualification.operation.DomainPortfolioOperationType;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.IEppService;
import fr.afnic.commons.services.exception.ConnectionException;
import fr.afnic.commons.services.exception.NotImplementedException;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.jooq.converter.JooqConverterFacade;
import fr.afnic.commons.services.jooq.stub.nicope.tables.EppMessageTable;
import fr.afnic.commons.services.jooq.stub.nicope.tables.records.EppMessageTableRecord;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.commons.utils.Preconditions;
import fr.afnic.utils.sql.ISqlConnectionFactory;

public class JooqEppService implements IEppService {

    private IEppService eppService;
    private final ISqlConnectionFactory sqlConnectionFactory;

    protected JooqEppService(ISqlConnectionFactory sqlConnectionFactory) {
        this.sqlConnectionFactory = sqlConnectionFactory;
    }

    protected JooqEppService(ISqlConnectionFactory sqlConnectionFactory, IEppService eppService) {
        this.eppService = eppService;
        this.sqlConnectionFactory = sqlConnectionFactory;
    }

    @Override
    public List<Notification> notifyOfDomainPortfolioOperation(DomainPortfolioOperationType operation, String holderNicHandle, UserId userId, TldServiceFacade tld) throws ServiceException {
        return this.getEppService().notifyOfDomainPortfolioOperation(operation, holderNicHandle, userId, tld);
    }

    @Override
    public List<Notification> notifyOfQualificationStep(QualificationStep step, Qualification qualification, UserId userId, TldServiceFacade tld) throws ServiceException {
        return this.getEppService().notifyOfQualificationStep(step, qualification, userId, tld);
    }

    @Override
    public List<EppMessage> getEppMessages(String contactSnapshotId, UserId userId, TldServiceFacade tld) throws ServiceException {
        Preconditions.checkNotEmpty(contactSnapshotId, "contactSnapshotId");

        Factory factory = this.createFactory();
        try {
            Result<EppMessageTableRecord> result = factory.selectFrom(EppMessageTable.EPP_MESSAGE)
                                                          .where(EppMessageTable.EPP_MESSAGE.CONTACT_SNAPSHOT_ID.equal(contactSnapshotId))
                                                          .fetch();

            return JooqConverterFacade.convertIterator(result.iterator(), EppMessage.class, userId, tld);
        } catch (Exception e) {
            throw new ServiceException("getEppMessages(" + contactSnapshotId + ") failed", e);
        } finally {
            this.closeFactory(factory);
        }
    }

    private IEppService getEppService() throws NotImplementedException {
        if (this.eppService == null) {
            throw new NotImplementedException("eppService is not set.");
        } else {
            return this.eppService;
        }
    }

    protected Factory createFactory() throws ConnectionException {
        Connection connection = this.sqlConnectionFactory.createConnection();
        return new Factory(connection, SQLDialect.ORACLE);
    }

    protected void closeFactory(Factory factory) throws ConnectionException {
        this.sqlConnectionFactory.closeConnection(factory.getConnection());
    }

}
