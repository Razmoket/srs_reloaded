package fr.afnic.commons.services.jooq;

import java.math.BigInteger;
import java.sql.Connection;
import java.util.List;

import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.SimpleSelectConditionStep;
import org.jooq.impl.Factory;

import fr.afnic.commons.beans.contact.CustomerContact;
import fr.afnic.commons.beans.contact.CustomerContactCriteria;
import fr.afnic.commons.beans.contact.CustomerContactId;
import fr.afnic.commons.beans.contactdetails.EmailAddress;
import fr.afnic.commons.beans.contactdetails.PhoneNumber;
import fr.afnic.commons.beans.contactdetails.PostalAddress;
import fr.afnic.commons.beans.contactdetails.PostalAddressId;
import fr.afnic.commons.beans.contactdetails.Url;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.beans.validatable.ObjectValue;
import fr.afnic.commons.services.ICustomerContactService;
import fr.afnic.commons.services.exception.ConnectionException;
import fr.afnic.commons.services.exception.NotFoundException;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.jooq.converter.JooqConverterFacade;
import fr.afnic.commons.services.jooq.stub.grc.packages.PkgContact;
import fr.afnic.commons.services.jooq.stub.grc.tables.CustomerContactTable;
import fr.afnic.commons.services.jooq.stub.grc.tables.records.CustomerContactTableRecord;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.commons.utils.Preconditions;
import fr.afnic.utils.sql.ISqlConnectionFactory;
import fr.afnic.utils.sql.pool.PoolSqlConnectionFactoryFacade;
import fr.afnic.utils.sql.pool.SqlDatabaseEnum;

public class JooqCustomerContactService implements ICustomerContactService {

    private final ISqlConnectionFactory sqlConnectionFactory;

    private static final CustomerContactTable CUSTOMER_CONTACT = CustomerContactTable.CUSTOMER_CONTACT;

    private static final int EMAIL = 1;
    private static final int PHONE = 2;
    private static final int FAX = 3;
    private static final int URL = 4;

    public JooqCustomerContactService(ISqlConnectionFactory sqlConnectionFactory) {
        this.sqlConnectionFactory = sqlConnectionFactory;
    }

    public JooqCustomerContactService(SqlDatabaseEnum database, TldServiceFacade tld) throws ServiceException {
        this.sqlConnectionFactory = PoolSqlConnectionFactoryFacade.getSqlPoolConnectionFactory(database, tld);
    }

    @Override
    public CustomerContact getCustomerContact(CustomerContactId id, UserId userId, TldServiceFacade tld) throws ServiceException {
        Factory factory = this.createFactory();
        try {
            SimpleSelectConditionStep<CustomerContactTableRecord> select = factory.selectFrom(CUSTOMER_CONTACT)
                                                                                  .where(CUSTOMER_CONTACT.ID_CONTACT.equal(id.getIntValue()));

            Result<CustomerContactTableRecord> result1 = select.fetch();

            if (result1.size() == 0) {
                throw new NotFoundException("No CustomerContact found wiht id:" + id);
            }

            return JooqConverterFacade.convert(result1.get(0), CustomerContact.class, userId, tld);
        } catch (Exception e) {
            throw new ServiceException("getCustomerContact(" + id.toString() + ") failed", e);
        } finally {
            this.closeFactory(factory);
        }
    }

    @Override
    public CustomerContactId createCustomerContact(CustomerContact customerContact, UserId userId, TldServiceFacade tld) throws ServiceException {
        Preconditions.checkNotNull(customerContact, "customerContact");
        Preconditions.checkNotNull(customerContact.getCreateUserId(), "customerContact.createUserId");
        Preconditions.checkNotNull(customerContact.getCustomerId(), "customerContact.customerId");

        Factory factory = this.createFactory();
        try {

            PostalAddressId postalAddressId = this.createOrGetPostalAddressId(customerContact, userId, tld);

            BigInteger newId;
            if (customerContact.isIndividual()) {
                newId = PkgContact.createContactIndividual(factory,
                                                           postalAddressId.getIntValue(),
                                                           customerContact.getCustomerId().getIntValue(),
                                                           customerContact.getIndividualIdentity().getFirstName(),
                                                           customerContact.getIndividualIdentity().getLastName(),
                                                           "",
                                                           customerContact.getCreateUserId().getIntValue());

            } else {
                newId = PkgContact.createContactCorporate(factory,
                                                          postalAddressId.getIntValue(),
                                                          customerContact.getCustomerId().getIntValue(),
                                                          customerContact.getCorporateEntityIdentity().getOrganizationName(),
                                                          "",
                                                          customerContact.getCreateUserId().getIntValue());

            }

            if (!customerContact.getEmailAddresses().isEmpty()) {
                for (EmailAddress emailAdress : customerContact.getEmailAddresses()) {
                    PkgContact.addContactDetail(factory, newId, JooqCustomerContactService.EMAIL, emailAdress.getValue());
                }
            }
            if (!customerContact.getPhoneNumbers().isEmpty()) {
                for (PhoneNumber phoneNumber : customerContact.getPhoneNumbers()) {
                    PkgContact.addContactDetail(factory, newId, JooqCustomerContactService.PHONE, phoneNumber.getValue());
                }
            }
            if (!customerContact.getFaxNumbers().isEmpty()) {
                for (PhoneNumber phoneNumber : customerContact.getFaxNumbers()) {
                    PkgContact.addContactDetail(factory, newId, JooqCustomerContactService.FAX, phoneNumber.getValue());
                }
            }
            if (!customerContact.getUrls().isEmpty()) {
                for (Url url : customerContact.getUrls()) {
                    PkgContact.addContactDetail(factory, newId, JooqCustomerContactService.URL, url.getValue());
                }
            }

            return new CustomerContactId(newId.intValue());

        } catch (Exception e) {
            throw new ServiceException("create(" + customerContact.toString() + ") failed", e);
        } finally {
            this.closeFactory(factory);
        }
    }

    public String getValue(ObjectValue obj) {
        if (obj == null) {
            return null;
        } else {
            return obj.getValue();
        }
    }

    private PostalAddressId createOrGetPostalAddressId(CustomerContact customerContact, UserId userId, TldServiceFacade tld) throws ServiceException {
        PostalAddress postalAddress = customerContact.getPostalAddress();
        if (postalAddress.getId() != null) {
            return postalAddress.getId();
        } else {
            postalAddress.setCreateUserId(customerContact.getCreateUserId());
            return AppServiceFacade.getPostalAddressService().create(postalAddress, userId, tld);
        }
    }

    @Override
    public CustomerContact updateCustomerContact(CustomerContact from, UserId userId, TldServiceFacade tld) throws ServiceException {
        Preconditions.checkNotNull(from, "customerContact");
        Preconditions.checkNotNull(from.getCreateUserId(), "customerContact.createUserId");
        Preconditions.checkNotNull(from.getCustomerId(), "customerContact.customerId");

        return null;
    }

    @Override
    public List<CustomerContact> seachCustomerContact(CustomerContactCriteria criteria, UserId userId, TldServiceFacade tld) throws ServiceException {
        Factory factory = this.createFactory();
        try {
            SimpleSelectConditionStep<CustomerContactTableRecord> select = factory.selectFrom(CUSTOMER_CONTACT)
                                                                                  .where(CUSTOMER_CONTACT.ID_CUSTOMER.equal(criteria.getCustomerId().getIntValue()));

            Result<CustomerContactTableRecord> result1 = select.fetch();

            if (result1.size() == 0) {
                throw new NotFoundException("No CustomerContact found wiht id_customer:" + criteria.getCustomerId().getIntValue());
            }

            return JooqConverterFacade.convertList(result1, CustomerContact.class, userId, tld);
        } catch (Exception e) {
            throw new ServiceException("seachCustomerContact(" + criteria.getCustomerId().getIntValue() + ") failed", e);
        } finally {
            this.closeFactory(factory);
        }
    }

    @Override
    public int add_contact_detail(CustomerContactId customerContactId, int contactDetailsType, String value, UserId userId, TldServiceFacade tld) throws ServiceException {
        Factory factory = this.createFactory();
        try {

            BigInteger newId;
            newId = PkgContact.addContactDetail(factory, customerContactId.getIntValue(), contactDetailsType, value);
            return newId.intValue();

        } catch (Exception e) {
            throw new ServiceException("add_contact_detail(" + value + ") failed", e);
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
