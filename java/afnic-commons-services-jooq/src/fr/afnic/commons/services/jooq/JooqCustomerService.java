package fr.afnic.commons.services.jooq;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.jooq.Condition;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.SelectLimitStep;
import org.jooq.SimpleSelectConditionStep;
import org.jooq.impl.Factory;

import fr.afnic.commons.beans.contact.identity.CorporateEntityIdentity;
import fr.afnic.commons.beans.contact.identity.IndividualIdentity;
import fr.afnic.commons.beans.contactdetails.PostalAddress;
import fr.afnic.commons.beans.customer.Customer;
import fr.afnic.commons.beans.customer.CustomerId;
import fr.afnic.commons.beans.profiling.CustomerAccount;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.beans.search.Pagination;
import fr.afnic.commons.beans.search.customer.CustomerSearchCriteria;
import fr.afnic.commons.beans.search.customer.CustomerSearchResult;
import fr.afnic.commons.services.ICustomerService;
import fr.afnic.commons.services.exception.ConnectionException;
import fr.afnic.commons.services.exception.NotFoundException;
import fr.afnic.commons.services.exception.NotImplementedException;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.jooq.converter.JooqConverterFacade;
import fr.afnic.commons.services.jooq.stub.grc.packages.PkgCustomer;
import fr.afnic.commons.services.jooq.stub.grc.tables.CorporateEntityTable;
import fr.afnic.commons.services.jooq.stub.grc.tables.CustomerTable;
import fr.afnic.commons.services.jooq.stub.grc.tables.IndividualEntityTable;
import fr.afnic.commons.services.jooq.stub.grc.tables.records.CustomerTableRecord;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.commons.utils.Preconditions;
import fr.afnic.utils.sql.ISqlConnectionFactory;
import fr.afnic.utils.sql.pool.PoolSqlConnectionFactoryFacade;
import fr.afnic.utils.sql.pool.SqlDatabaseEnum;

public class JooqCustomerService implements ICustomerService {

    private final ISqlConnectionFactory sqlConnectionFactory;

    private static final CustomerTable CUSTOMER = CustomerTable.CUSTOMER;

    public JooqCustomerService(ISqlConnectionFactory sqlConnectionFactory) {
        this.sqlConnectionFactory = sqlConnectionFactory;
    }

    public JooqCustomerService(SqlDatabaseEnum database, TldServiceFacade tld) throws ServiceException {
        this.sqlConnectionFactory = PoolSqlConnectionFactoryFacade.getSqlPoolConnectionFactory(database, tld);
    }

    @Override
    public Customer createCustomer(Customer customer, UserId userId, TldServiceFacade tld) throws ServiceException {
        Preconditions.checkNotNull(customer, "customer");
        Preconditions.checkNotNull(customer.getCreateUserId(), "contract.createUserId");
        Preconditions.checkNotNull(customer.getStatus(), "contract.status");

        if (customer.getIdentity() instanceof IndividualIdentity) {
            return this.createIndividualCustomer(customer, userId, tld);
        } else {
            return this.createCorporateCustomer(customer, userId, tld);
        }
    }

    private Customer createCorporateCustomer(Customer customer, UserId userId, TldServiceFacade tld) throws ServiceException {
        Factory factory = this.createFactory();
        try {
            CorporateEntityIdentity corporate = (CorporateEntityIdentity) customer.getIdentity();
            PostalAddress postal = customer.getPostalAddress();
            String[] arrayStreet = postal.getStreetInArray(3);
            BigDecimal newId = PkgCustomer.createCustomerCorporate(factory, JooqConverterFacade.convert(customer.getStatus(), Integer.class, userId, tld),
                                                                   customer.getRemark(),
                                                                   customer.getAccountManagerId().getIntValue(), customer.isTest() ? 1 : 0, customer.getCreateUserId().getIntValue(),
                                                                   corporate.getSirenAsString(),
                                                                   corporate.getSirenAsString(),
                                                                   corporate.getIntracommunityVatAsString(), corporate.getTradeMarkAsString(), corporate.getWaldecAsString(),
                                                                   corporate.getOrganizationName(),
                                                                   customer.getAccountLogin(), customer.getAccount().getPassword(), postal.getOrganization(), arrayStreet[0], arrayStreet[1],
                                                                   arrayStreet[2],
                                                                   postal.getPostCode(), postal.getCity(), postal.getCityCedex(), postal.getCountryCode());
            return this.getCustomerWithId(new CustomerId(newId.intValue()), userId, tld);
        } catch (Exception e) {
            throw new ServiceException("create(" + customer.toString() + ") failed", e);
        } finally {
            this.closeFactory(factory);
        }
    }

    private Customer createIndividualCustomer(Customer customer, UserId userId, TldServiceFacade tld) throws ServiceException {

        Factory factory = this.createFactory();
        try {
            PostalAddress postal = customer.getPostalAddress();
            String[] arrayStreet = postal.getStreetInArray(3);
            IndividualIdentity individual = (IndividualIdentity) customer.getIdentity();

            BigDecimal newId = PkgCustomer.createCustomerIndividual(factory, JooqConverterFacade.convert(customer.getStatus(), Integer.class, userId, tld),
                                                                    customer.getRemark(),
                                                                    customer.getAccountManagerId().getIntValue(), customer.isTest() ? 1 : 0, customer.getCreateUserId().getIntValue(),
                                                                    individual.getLastName(),
                                                                    individual.getFirstName(), new java.sql.Date(individual.getBirthDate().getTime()), individual.getBirthCity(),
                                                                    customer.getAccountLogin(), customer.getAccount().getPassword(), postal.getOrganization(), arrayStreet[0], arrayStreet[1],
                                                                    arrayStreet[2],
                                                                    postal.getPostCode(), postal.getCity(), postal.getCityCedex(), postal.getCountryCode());
            return this.getCustomerWithId(new CustomerId(newId.intValue()), userId, tld);
        } catch (Exception e) {
            throw new ServiceException("create(" + customer.toString() + ") failed", e);
        } finally {
            this.closeFactory(factory);
        }
    }

    @Override
    public Customer updateCustomer(Customer customer, UserId userId, TldServiceFacade tld) throws ServiceException {
        Factory factory = this.createFactory();
        try {
            Integer idIndividual = null;
            Integer idCorporate = null;
            if (customer.getIdentity() instanceof IndividualIdentity) {
                idIndividual = customer.getIdentity().getId();
            } else {
                idCorporate = customer.getIdentity().getId();
            }

            PkgCustomer.updateCustomer(factory, customer.getCustomerId().getIntValue(), JooqConverterFacade.convert(customer.getStatus(), Integer.class, userId, tld),
                                       customer.getRemark(), customer.getAccountManagerId().getIntValue(), customer.isTest() ? 1 : 0, customer.getPostalAddress().getId()
                                                                                                                                              .getIntValue(),
                                       userId.getIntValue());

        } catch (Exception e) {
            throw new ServiceException("create(" + customer.toString() + ") failed", e);
        } finally {
            this.closeFactory(factory);
        }

        tld.getServiceProvider().getBillingService().updateCustomer(customer, userId, tld);
        return customer;

    }

    @Override
    public Customer getCustomerWithId(CustomerId customerId, UserId userId, TldServiceFacade tld) throws ServiceException {
        return this.getCustomer(CUSTOMER.ID_CUSTOMER.equal(customerId.getIntValue()), "id:" + customerId, userId, tld);
    }

    @Override
    public Customer getCustomerWithNumber(String customerNumber, UserId userId, TldServiceFacade tld) throws ServiceException {
        return this.getCustomer(CUSTOMER.CODE.equal(customerNumber), "customerNumber:" + customerNumber, userId, tld);
    }

    @Override
    public Customer getCustomerWithCode(String code, UserId userId, TldServiceFacade tld) throws ServiceException {
        return this.getCustomer(CUSTOMER.CODE.equal(code), "code:" + code, userId, tld);
    }

    private Customer getCustomer(Condition condition, String logComment, UserId userId, TldServiceFacade tld) throws ServiceException {
        Factory factory = this.createFactory();
        try {
            SimpleSelectConditionStep<CustomerTableRecord> select = factory.selectFrom(CUSTOMER)
                                                                           .where(condition);

            Result<CustomerTableRecord> result = select.fetch();

            if (result.size() == 0) {
                throw new NotFoundException("No Customer found whith " + logComment);
            }

            return JooqConverterFacade.convert(result.get(0), Customer.class, userId, tld);

        } catch (Exception e) {
            throw new ServiceException("getCustomer(" + logComment + ") failed", e);
        } finally {
            this.closeFactory(factory);
        }
    }

    @Override
    public CustomerSearchResult searchCustomer(CustomerSearchCriteria criteria, Pagination pagination, UserId userId, TldServiceFacade tld) throws ServiceException {

        Preconditions.checkNotNull(criteria, "criteria");

        Factory factory = this.createFactory();
        try {

            List<Condition> conditions = new ArrayList<Condition>();

            if (StringUtils.isNotBlank(criteria.getCode())) {
                conditions.add(CUSTOMER.CODE.equalIgnoreCase(criteria.getCode()));
            }

            if (StringUtils.isNotBlank(criteria.getCustomerNumber())) {
                conditions.add(CUSTOMER.CONTRACT_LEGAL_ID.equalIgnoreCase(criteria.getCustomerNumber()));
            }

            if (StringUtils.isNotBlank(criteria.getName())) {
                conditions.add(CorporateEntityTable.CORPORATE_ENTITY.ORGANIZATION_NAME.upper().contains(criteria.getName().toUpperCase())
                                                                                      .or(IndividualEntityTable.INDIVIDUAL_ENTITY.FIRST_NAME.upper().contains(criteria.getName().toUpperCase()))
                                                                                      .or(IndividualEntityTable.INDIVIDUAL_ENTITY.LAST_NAME.upper().contains(criteria.getName().toUpperCase())));
            }

            SelectLimitStep select = factory.select()
                                            .from(CUSTOMER)
                                            .leftOuterJoin(IndividualEntityTable.INDIVIDUAL_ENTITY).on(CUSTOMER.ID_INDIVIDUALENTITY.equal(IndividualEntityTable.INDIVIDUAL_ENTITY.ID_INDIVIDUALENTITY))
                                            .leftOuterJoin(CorporateEntityTable.CORPORATE_ENTITY).on(CUSTOMER.ID_CORPORATEENTITY.equal(CorporateEntityTable.CORPORATE_ENTITY.ID_CORPORATEENTITY))
                                            .where(conditions)
                                            .orderBy(CUSTOMER.ID_CUSTOMER);

            Result<CustomerTableRecord> records = select.fetchInto(CUSTOMER);

            int minIndex = (pagination.getPageNumber() - 1) * pagination.getMaxResultCount();
            int maxIndex = minIndex + pagination.getMaxResultCount();

            List<Customer> customers = new ArrayList<Customer>();
            for (int i = 0; i < records.size(); i++) {
                if (i >= minIndex && i < maxIndex) {
                    customers.add(JooqConverterFacade.convert(records.get(i), Customer.class, userId, tld));
                }
            }

            CustomerSearchResult result = new CustomerSearchResult();
            result.setPagination(pagination);
            result.setPageResults(customers);
            result.setCriteria(criteria);
            result.setTotalResultCount(records.size());

            return result;

        } catch (Exception e) {
            throw new ServiceException("searchCustomer(" + criteria + ") failed", e);
        } finally {
            this.closeFactory(factory);
        }
    }

    @Override
    public int getCustomerCount(UserId userId, TldServiceFacade tld) throws ServiceException {
        throw new NotImplementedException();
    }

    @Override
    public List<CustomerAccount> getAccounts(CustomerId customerId, UserId userId, TldServiceFacade tld) throws ServiceException {
        throw new NotImplementedException();
    }

    protected Factory createFactory() throws ConnectionException {
        Connection connection = this.sqlConnectionFactory.createConnection();
        return new Factory(connection, SQLDialect.ORACLE);
    }

    protected void closeFactory(Factory factory) throws ConnectionException {
        this.sqlConnectionFactory.closeConnection(factory.getConnection());
    }

    @Override
    public Customer getRegistry(UserId userId, TldServiceFacade tld) {
        // Factory factory = this.createFactory();
        try {

            /* List<Condition> conditions = new ArrayList<Condition>();

            

             SelectLimitStep select = factory.select()
                                             .from(CUSTOMER)
                                             .join(ContractTable.CONTRACT)
                                             .on(ContractTable.CONTRACT.ID_CLIENT.equal(CUSTOMER.INDIVIDUAL_ENTITY.ID_INDIVIDUALENTITY))
                                             .where(conditions)
                                             .orderBy(CUSTOMER.ID_CUSTOMER);

             Result<CustomerTableRecord> records = select.fetchInto(CUSTOMER);

             int minIndex = (pagination.getPageNumber() - 1) * pagination.getMaxResultCount();
             int maxIndex = minIndex + pagination.getMaxResultCount();

             List<Customer> customers = new ArrayList<Customer>();
             for (int i = 0; i < records.size(); i++) {
                 if (i >= minIndex && i < maxIndex) {
                     customers.add(JooqConverterFacade.convert(records.get(i), Customer.class, userId, tld));
                 }
             }

             CustomerSearchResult result = new CustomerSearchResult();
             result.setPagination(pagination);
             result.setPageResults(customers);
             result.setCriteria(criteria);
             result.setTotalResultCount(records.size());

             return result;*/

        } catch (Exception e) {
            //throw new ServiceException("searchCustomer(" + criteria + ") failed", e);
        } finally {
            // this.closeFactory(factory);
        }

        return null;

    }

}
