package fr.afnic.commons.services.jooq;

import java.sql.Connection;
import java.util.List;

import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.Factory;

import fr.afnic.commons.beans.contactdetails.ContactDetail;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.IContactDetailsService;
import fr.afnic.commons.services.exception.ConnectionException;
import fr.afnic.commons.services.exception.NotFoundException;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.jooq.converter.JooqConverterFacade;
import fr.afnic.commons.services.jooq.stub.grc.tables.ContactContactdetailsTable;
import fr.afnic.commons.services.jooq.stub.grc.tables.ContactDetailsTable;
import fr.afnic.commons.services.jooq.stub.grc.tables.CustomerContactdetailsTable;
import fr.afnic.commons.services.jooq.stub.grc.tables.records.ContactDetailsTableRecord;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.utils.sql.ISqlConnectionFactory;
import fr.afnic.utils.sql.pool.PoolSqlConnectionFactoryFacade;
import fr.afnic.utils.sql.pool.SqlDatabaseEnum;

public class JooqContactDetailsService implements IContactDetailsService {

    private final ISqlConnectionFactory sqlConnectionFactory;

    public JooqContactDetailsService(ISqlConnectionFactory sqlConnectionFactory) {
        this.sqlConnectionFactory = sqlConnectionFactory;
    }

    public JooqContactDetailsService(SqlDatabaseEnum database, TldServiceFacade tld) throws ServiceException {
        this.sqlConnectionFactory = PoolSqlConnectionFactoryFacade.getSqlPoolConnectionFactory(database, tld);
    }

    @Override
    public List<ContactDetail> getCustomerContactDetails(int id_contact, UserId userId, TldServiceFacade tld) throws ServiceException {
        Factory factory = this.createFactory();
        try {
            Result<ContactDetailsTableRecord> result = factory.select(ContactDetailsTable.CONTACT_DETAILS.getFields())
                                                              .from(ContactContactdetailsTable.CONTACT_CONTACTDETAILS)
                                                              .join(ContactDetailsTable.CONTACT_DETAILS)
                                                              .on(ContactContactdetailsTable.CONTACT_CONTACTDETAILS.ID_CONTACTDETAILS.equal(ContactDetailsTable.CONTACT_DETAILS.ID_CONTACTDETAILS))
                                                              .where(ContactContactdetailsTable.CONTACT_CONTACTDETAILS.ID_CONTACT.equal(id_contact)).fetchInto(ContactDetailsTable.CONTACT_DETAILS);

            if (result.size() == 0) {
                throw new NotFoundException("No ContactDetails found with id:" + id_contact);
            }
            return JooqConverterFacade.convertList(result, ContactDetail.class, userId, tld);
        } catch (Exception e) {
            throw new ServiceException("getCustomerContactDetails(" + id_contact + ") failed", e);
        } finally {
            this.closeFactory(factory);
        }

    }

    @Override
    public List<ContactDetail> getCustomerDetails(int id_customer, UserId userId, TldServiceFacade tld) throws ServiceException {
        Factory factory = this.createFactory();
        try {
            Result<ContactDetailsTableRecord> result = factory.select(ContactDetailsTable.CONTACT_DETAILS.getFields())
                                                              .from(CustomerContactdetailsTable.CUSTOMER_CONTACTDETAILS)
                                                              .join(ContactDetailsTable.CONTACT_DETAILS)
                                                              .on(CustomerContactdetailsTable.CUSTOMER_CONTACTDETAILS.ID_CONTACTDETAILS.equal(ContactDetailsTable.CONTACT_DETAILS.ID_CONTACTDETAILS))
                                                              .where(CustomerContactdetailsTable.CUSTOMER_CONTACTDETAILS.ID_CUSTOMER.equal(id_customer)).fetchInto(ContactDetailsTable.CONTACT_DETAILS);

            if (result.size() == 0) {
                throw new NotFoundException("No ContactDetails found with id:" + id_customer);
            }
            return JooqConverterFacade.convertList(result, ContactDetail.class, userId, tld);
        } catch (Exception e) {
            throw new ServiceException("getCustomerDetails(" + id_customer + ") failed", e);
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
