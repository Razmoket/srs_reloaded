package fr.afnic.commons.services.jooq;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jooq.Condition;
import org.jooq.SQLDialect;
import org.jooq.SelectConditionStep;
import org.jooq.SimpleSelectConditionStep;
import org.jooq.impl.Factory;

import fr.afnic.commons.beans.Ticket;
import fr.afnic.commons.beans.domain.Domain;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.beans.request.AuthorizationRequest;
import fr.afnic.commons.services.IDomainService;
import fr.afnic.commons.services.exception.ConnectionException;
import fr.afnic.commons.services.exception.DomainNotFoundException;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.jooq.converter.JooqConverterFacade;
import fr.afnic.commons.services.jooq.stub.boa.tables.VDomainView;
import fr.afnic.commons.services.jooq.stub.boa.tables.records.VDomainViewRecord;
import fr.afnic.commons.services.proxy.ProxyDomainService;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.commons.utils.Preconditions;
import fr.afnic.utils.sql.ISqlConnectionFactory;
import fr.afnic.utils.sql.pool.PoolSqlConnectionFactoryFacade;
import fr.afnic.utils.sql.pool.SqlDatabaseEnum;

public class JooqDomainService extends ProxyDomainService {

    private final ISqlConnectionFactory sqlConnectionFactory;

    private static VDomainView V_DOMAIN = VDomainView.V_DOMAIN;

    public JooqDomainService(SqlDatabaseEnum database, TldServiceFacade tld, IDomainService domainService) throws ServiceException {
        super(domainService);
        this.sqlConnectionFactory = PoolSqlConnectionFactoryFacade.getSqlPoolConnectionFactory(database, tld);
    }

    public JooqDomainService(SqlDatabaseEnum database, TldServiceFacade tld) throws ServiceException {
        this.sqlConnectionFactory = PoolSqlConnectionFactoryFacade.getSqlPoolConnectionFactory(database, tld);
    }

    @Override
    public Domain getDomainWithName(String domainName, UserId userId, TldServiceFacade tld) throws ServiceException {
        String ldhName = AppServiceFacade.getQualityService().normalizeDomainName(domainName, userId, tld).getLdh();
        List<Domain> domains = this.getDomains(1, userId, tld, V_DOMAIN.DOMAIN_NAME.equal(ldhName));
        if (domains.isEmpty()) {

            try {
                // recherche dans les tickets et les authorisations
                List<Ticket> ticketsWithDomain = AppServiceFacade.getTicketService().getTicketsWithDomain(ldhName, userId, tld);
                if (ticketsWithDomain != null && !ticketsWithDomain.isEmpty()) {
                    return Domain.createFreeDomain(ldhName, userId, tld);
                }

                List<AuthorizationRequest> authorizationRequestsWithDomain = AppServiceFacade.getAuthorizationRequestService().getAuthorizationRequestsWithDomain(ldhName, userId, tld);
                if (!authorizationRequestsWithDomain.isEmpty()) {
                    return Domain.createFreeDomain(ldhName, userId, tld);
                }
            } catch (Exception e) {
                throw new ServiceException("getDomainWithName(" + domainName + ") failed", e);
            }

            throw new DomainNotFoundException(domainName);
        } else {
            return domains.get(0);
        }
    }

    @Override
    public List<Domain> getDomainsWithNameContaining(String domainNameChunk, UserId userId, TldServiceFacade tld) throws ServiceException {
        return this.getDomains(-1, userId, tld, V_DOMAIN.DOMAIN_NAME.like("%" + domainNameChunk + "%"));
    }

    @Override
    public List<Domain> getDomainsWithHolderHandle(String nicHandleStr, UserId userId, TldServiceFacade tld) throws ServiceException {
        return this.getDomains(userId, tld, V_DOMAIN.HOLDER_HANDLE.equal(nicHandleStr));
    }

    @Override
    public int getDomainsWithHolderHandleCount(String nicHandleStr, UserId userId, TldServiceFacade tld) throws ServiceException {
        return this.getDomainsCount(V_DOMAIN.HOLDER_HANDLE.equal(nicHandleStr));

    }

    @Override
    public List<Domain> getDomainsWithRegistrarCode(String code, UserId userId, TldServiceFacade tld) throws ServiceException {
        return this.getDomains(userId, tld, V_DOMAIN.CUSTOMER_CODE.equal(code));
    }

    @Override
    public List<String> getDomainNamesWithHolderHandle(String nicHandleStr, UserId userId, TldServiceFacade tld) throws ServiceException {
        List<Domain> domains = this.getDomains(userId, tld, V_DOMAIN.HOLDER_HANDLE.equal(nicHandleStr));

        List<String> names = new ArrayList<String>();
        for (Domain domain : domains) {
            names.add(domain.getName());
        }

        return names;
    }

    private List<Domain> getDomains(UserId userId, TldServiceFacade tld, Condition... conditions) throws ServiceException {
        return this.getDomains(-1, userId, tld, conditions);
    }

    private int getDomainsCount(Condition... conditions) throws ServiceException {
        Preconditions.checkNotEmpty(conditions, "conditions");

        Factory factory = this.createFactory();
        try {

            SelectConditionStep select = factory.select(Factory.count())
                                                .from(V_DOMAIN)
                                                .where(conditions[0]);

            if (conditions.length > 1) {
                for (int i = 1; i < conditions.length; i++) {
                    select = select.and(conditions[i]);
                }
            }

            org.jooq.Result<?> r = select.fetch();
            return r.getValueAsBigInteger(0, 0).intValue();

        } catch (Exception e) {
            throw new ServiceException("getDomains(" + Arrays.toString(conditions) + ") failed", e);
        } finally {
            this.closeFactory(factory);
        }
    }

    /**
     * Si maxResult <= 0 , pas de clause limit()
     */
    private List<Domain> getDomains(int maxResult, UserId userId, TldServiceFacade tld, Condition... conditions) throws ServiceException {
        Preconditions.checkNotEmpty(conditions, "conditions");

        Factory factory = this.createFactory();
        try {
            SimpleSelectConditionStep<VDomainViewRecord> select = factory.selectFrom(V_DOMAIN)
                                                                         .where(conditions);

            org.jooq.Result<VDomainViewRecord> result = null;
            if (maxResult > 0) {
                result = select.limit(maxResult).fetch();
            } else {
                result = select.fetch();
            }

            return JooqConverterFacade.convertIterator(result.iterator(), Domain.class, userId, tld);
        } catch (Exception e) {
            throw new ServiceException("getDomains(" + Arrays.toString(conditions) + ") failed", e);
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
