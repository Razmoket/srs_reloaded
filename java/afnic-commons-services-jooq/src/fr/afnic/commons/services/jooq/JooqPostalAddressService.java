package fr.afnic.commons.services.jooq;

import java.math.BigInteger;
import java.sql.Connection;

import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.SimpleSelectConditionStep;
import org.jooq.impl.Factory;

import fr.afnic.commons.beans.contactdetails.PostalAddress;
import fr.afnic.commons.beans.contactdetails.PostalAddressId;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.IPostalAddressService;
import fr.afnic.commons.services.exception.ConnectionException;
import fr.afnic.commons.services.exception.NotFoundException;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.jooq.converter.JooqConverterFacade;
import fr.afnic.commons.services.jooq.stub.grc.packages.PkgPostalAddress;
import fr.afnic.commons.services.jooq.stub.grc.tables.PostalAddressTable;
import fr.afnic.commons.services.jooq.stub.grc.tables.records.PostalAddressTableRecord;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.commons.utils.Preconditions;
import fr.afnic.utils.sql.ISqlConnectionFactory;
import fr.afnic.utils.sql.pool.PoolSqlConnectionFactoryFacade;
import fr.afnic.utils.sql.pool.SqlDatabaseEnum;

public class JooqPostalAddressService implements IPostalAddressService {

    private final ISqlConnectionFactory sqlConnectionFactory;

    private static final PostalAddressTable POSTAL_ADDRESS = PostalAddressTable.POSTAL_ADDRESS;

    public JooqPostalAddressService(ISqlConnectionFactory sqlConnectionFactory) {
        this.sqlConnectionFactory = sqlConnectionFactory;
    }

    public JooqPostalAddressService(SqlDatabaseEnum database, TldServiceFacade tld) throws ServiceException {
        this.sqlConnectionFactory = PoolSqlConnectionFactoryFacade.getSqlPoolConnectionFactory(database, tld);
    }

    @Override
    public PostalAddress getPostalAddress(PostalAddressId id, UserId userId, TldServiceFacade tld) throws ServiceException {
        Factory factory = this.createFactory();
        try {
            SimpleSelectConditionStep<PostalAddressTableRecord> select = factory.selectFrom(POSTAL_ADDRESS)
                                                                                .where(POSTAL_ADDRESS.ID_POSTALADDRESS.equal(id.getIntValue()));

            Result<PostalAddressTableRecord> result = select.fetch();

            if (result.size() == 0) {
                throw new NotFoundException("No postalAddress found wiht id:" + id);
            }
            return JooqConverterFacade.convert(result.get(0), PostalAddress.class, userId, tld);
        } catch (Exception e) {
            throw new ServiceException("getPostalAddress(" + id.toString() + ") failed", e);
        } finally {
            this.closeFactory(factory);
        }
    }

    @Override
    public void update(PostalAddress postalAddress, UserId userId, TldServiceFacade tld) throws ServiceException {
        Preconditions.checkNotNull(postalAddress, "postalAddress");
        Preconditions.checkNotNull(postalAddress.getId(), "postalAddress.id");
        Preconditions.checkNotNull(userId, "userId");

        Factory factory = this.createFactory();
        try {

            PkgPostalAddress.updatePostalAddress(factory,
                                                 postalAddress.getId().getIntValue(),
                                                 postalAddress.getOrganization(),
                                                 postalAddress.getStreetLine(0),
                                                 postalAddress.getStreetLine(1),
                                                 postalAddress.getStreetLine(2),
                                                 postalAddress.getPostCode(),
                                                 postalAddress.getCity(),
                                                 postalAddress.getCityCedex(),
                                                 postalAddress.getCountryCode(),
                                                 userId.getIntValue());

        } catch (Exception e) {
            throw new ServiceException("update(" + postalAddress.toString() + ") failed", e);
        } finally {
            this.closeFactory(factory);
        }

    }

    @Override
    public PostalAddressId create(PostalAddress postalAddress, UserId userId, TldServiceFacade tld) throws ServiceException {
        Preconditions.checkNotNull(postalAddress, "postalAddress");
        Preconditions.checkNotNull(postalAddress.getCreateUserId(), "postalAddress.createUserId");

        Factory factory = this.createFactory();
        try {

            BigInteger newId = PkgPostalAddress.createPostalAddress(factory,
                                                                    postalAddress.getOrganization(),
                                                                    postalAddress.getStreetLine(0),
                                                                    postalAddress.getStreetLine(1),
                                                                    postalAddress.getStreetLine(2),
                                                                    postalAddress.getPostCode(),
                                                                    postalAddress.getCity(),
                                                                    postalAddress.getCityCedex(),
                                                                    postalAddress.getCountryCode(),
                                                                    postalAddress.getCreateUserId().getIntValue());
            return new PostalAddressId(newId.intValue());

        } catch (Exception e) {
            throw new ServiceException("create(" + postalAddress.toString() + ") failed", e);
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