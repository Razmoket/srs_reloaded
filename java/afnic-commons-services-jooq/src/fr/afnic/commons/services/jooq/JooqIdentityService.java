package fr.afnic.commons.services.jooq;

import java.math.BigInteger;
import java.sql.Connection;

import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.SimpleSelectConditionStep;
import org.jooq.impl.Factory;

import fr.afnic.commons.beans.contact.identity.CorporateEntityIdentity;
import fr.afnic.commons.beans.contact.identity.IndividualIdentity;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.IIdentityService;
import fr.afnic.commons.services.exception.ConnectionException;
import fr.afnic.commons.services.exception.NotFoundException;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.jooq.converter.JooqConverterFacade;
import fr.afnic.commons.services.jooq.stub.grc.packages.PkgCorporate;
import fr.afnic.commons.services.jooq.stub.grc.packages.PkgIndividual;
import fr.afnic.commons.services.jooq.stub.grc.tables.CorporateEntityTable;
import fr.afnic.commons.services.jooq.stub.grc.tables.IndividualEntityTable;
import fr.afnic.commons.services.jooq.stub.grc.tables.records.CorporateEntityTableRecord;
import fr.afnic.commons.services.jooq.stub.grc.tables.records.IndividualEntityTableRecord;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.commons.utils.Preconditions;
import fr.afnic.utils.sql.ISqlConnectionFactory;
import fr.afnic.utils.sql.pool.PoolSqlConnectionFactoryFacade;
import fr.afnic.utils.sql.pool.SqlDatabaseEnum;

public class JooqIdentityService implements IIdentityService {

    private final ISqlConnectionFactory sqlConnectionFactory;

    private static final IndividualEntityTable INDIVIDUAL_ENTITY = IndividualEntityTable.INDIVIDUAL_ENTITY;
    private static final CorporateEntityTable CORPORATE_ENTITY = CorporateEntityTable.CORPORATE_ENTITY;

    public JooqIdentityService(ISqlConnectionFactory sqlConnectionFactory) {
        this.sqlConnectionFactory = sqlConnectionFactory;
    }

    public JooqIdentityService(SqlDatabaseEnum database, TldServiceFacade tld) throws ServiceException {
        this.sqlConnectionFactory = PoolSqlConnectionFactoryFacade.getSqlPoolConnectionFactory(database, tld);
    }

    @Override
    public IndividualIdentity getIndividualIdentity(int individualId, UserId userId, TldServiceFacade tld) throws ServiceException {
        Factory factory = this.createFactory();
        try {
            SimpleSelectConditionStep<IndividualEntityTableRecord> select = factory.selectFrom(INDIVIDUAL_ENTITY)
                                                                                   .where(INDIVIDUAL_ENTITY.ID_INDIVIDUALENTITY.equal(individualId));

            Result<IndividualEntityTableRecord> result = select.fetch();
            if (result.size() == 0) {
                throw new NotFoundException("No IndividualIdentity found with id:" + individualId);
            }
            return JooqConverterFacade.convert(result.get(0), IndividualIdentity.class, userId, tld);
        } catch (Exception e) {
            throw new ServiceException("getIndividualIdentity(" + individualId + ") failed", e);
        } finally {
            this.closeFactory(factory);
        }
    }

    @Override
    public CorporateEntityIdentity getCorporateEntityIdentity(int corporateEntityId, UserId userId, TldServiceFacade tld) throws ServiceException {
        Factory factory = this.createFactory();
        try {
            SimpleSelectConditionStep<CorporateEntityTableRecord> select = factory.selectFrom(CORPORATE_ENTITY)
                                                                                  .where(CORPORATE_ENTITY.ID_CORPORATEENTITY.equal(corporateEntityId));

            Result<CorporateEntityTableRecord> result = select.fetch();
            if (result.size() == 0) {
                throw new NotFoundException("No CorporateEntityIdentity found with id:" + corporateEntityId);
            }
            return JooqConverterFacade.convert(result.get(0), CorporateEntityIdentity.class, userId, tld);
        } catch (Exception e) {
            throw new ServiceException("getCorporateEntityIdentity(" + corporateEntityId + ") failed", e);
        } finally {
            this.closeFactory(factory);
        }
    }

    @Override
    public int createCorporateEntityIdentity(CorporateEntityIdentity corporateIdentity, UserId userId, TldServiceFacade tld) throws ServiceException {
        Preconditions.checkNotNull(corporateIdentity, "corporateIdentity");

        Factory factory = this.createFactory();
        try {
            BigInteger newId = PkgCorporate.createCorporate(factory, corporateIdentity.getSirenAsString(), corporateIdentity.getSiretAsString(), corporateIdentity.getIntracommunityVatAsString(),
                                                            corporateIdentity.getTradeMarkAsString(), corporateIdentity.getWaldecAsString(), corporateIdentity.getOrganizationName());
            corporateIdentity.setId(newId.intValue());
            return newId.intValue();

        } catch (Exception e) {
            throw new ServiceException("create(" + corporateIdentity.toString() + ") failed", e);
        } finally {
            this.closeFactory(factory);
        }

    }

    @Override
    public int createIndividualEntityIdentity(IndividualIdentity individualIdentity, UserId userId, TldServiceFacade tld) throws ServiceException {
        Preconditions.checkNotNull(individualIdentity, "individualIdentity");

        Factory factory = this.createFactory();
        try {
            BigInteger newId = PkgIndividual.createIndividual(factory, individualIdentity.getLastName(), individualIdentity.getFirstName(), new java.sql.Date(individualIdentity.getBirthDate()
                                                                                                                                                                                .getTime()),
                                                              individualIdentity.getBirthPostCode());
            individualIdentity.setId(newId.intValue());
            return newId.intValue();

        } catch (Exception e) {
            throw new ServiceException("create(" + individualIdentity.toString() + ") failed", e);
        } finally {
            this.closeFactory(factory);
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
