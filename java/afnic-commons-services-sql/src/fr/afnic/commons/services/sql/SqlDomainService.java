/*
 * $Id: SqlDomainService.java,v 1.25 2010/09/17 15:54:25 ginguene Exp $
 * $Revision: 1.25 $
 * $Author: ginguene $
 */

package fr.afnic.commons.services.sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.afnic.commons.beans.DSServer;
import fr.afnic.commons.beans.DnsServer;
import fr.afnic.commons.beans.NicHandle;
import fr.afnic.commons.beans.dnssec.AlgoHash;
import fr.afnic.commons.beans.dnssec.DigestHash;
import fr.afnic.commons.beans.domain.Domain;
import fr.afnic.commons.beans.domain.DomainNameDetail;
import fr.afnic.commons.beans.domain.DomainStatus;
import fr.afnic.commons.beans.ip.IpAddress;
import fr.afnic.commons.beans.ip.Ipv4Address;
import fr.afnic.commons.beans.ip.Ipv6Address;
import fr.afnic.commons.beans.logs.ILogger;
import fr.afnic.commons.beans.operations.qualification.operation.DomainPortfolioOperationType;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.beans.request.reserveddomains.FundamentalTerm;
import fr.afnic.commons.beans.request.reserveddomains.NormalDomain;
import fr.afnic.commons.beans.request.reserveddomains.ReservedDomainNameMotivation;
import fr.afnic.commons.beans.request.reserveddomains.SpecialDomain;
import fr.afnic.commons.beans.search.domain.DomainSearchCriteria;
import fr.afnic.commons.checkers.SpecialDomainChecker;
import fr.afnic.commons.services.IDomainService;
import fr.afnic.commons.services.exception.RequestFailedException;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.proxy.ProxyDomainService;
import fr.afnic.commons.services.sql.converter.SqlConverterFacade;
import fr.afnic.commons.services.sql.utils.QueryStatementManagement;
import fr.afnic.commons.services.sql.utils.SQLSelectQueryBuilder;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.utils.sql.ISqlConnectionFactory;
import fr.afnic.utils.sql.pool.PoolSqlConnectionFactoryFacade;
import fr.afnic.utils.sql.pool.SqlDatabaseEnum;

public class SqlDomainService extends ProxyDomainService {

    private static final ILogger LOGGER = AppServiceFacade.getLoggerService().getLogger(SqlDomainService.class);

    public static final Comparator<Domain> DOMAIN_COMPARATOR = new Comparator<Domain>() {

        @Override
        public int compare(Domain o1, Domain o2) {
            if (o1 == null || o1.getName() == null) {
                return 1;
            }

            if (o2 == null || o2.getName() == null) {
                return -1;
            }
            return o1.getName().compareTo(o2.getName());
        }
    };
    private QueryStatementManagement queryStatementManagement = null;

    private static final String DOMAIN_SELECTION = " whois.domain.STATUS,"
                                                   + " whois.domain.NAME,"
                                                   + " nicope.adherent.CODE,"
                                                   + " nicope.adherent.NOM, "
                                                   + " whois.nh.prefix, "
                                                   + " whois.nh.num, "
                                                   + " whois.nh.suffix,"
                                                   + " whois.contact.nom,"
                                                   + " whois.contact.prenom,"
                                                   + " whois.ephemeride.the_date,"
                                                   + " boa.nomdedomaine.NOM_I18N,"
                                                   + " boa.nomdedomaine.NOM_BUNDLE";

    private static final String REQ_SEARCH_DOMAIN_WHERE = " whois.domain.REF_REGISTRAR = nicope.adherent.ID"
                                                          + " AND boa.nomdedomaine.NOM = whois.domain.NAME"
                                                          + " AND whois.object_contact_r.CONTACT_ID = whois.contact.ID"
                                                          + " AND whois.object_contact_r.CONTACT_TYPE='HOLDER'"
                                                          + " AND whois.object_contact_r.object_id =  whois.domain.ID"
                                                          + " AND whois.nh.object_id =  whois.contact.ID"
                                                          + " AND whois.domain.ID=WHOIS.ephemeride.domain_id"
                                                          + " AND WHOIS.ephemeride.type='ANNIV'";

    /**
     * Constructeur avec un sqlConnectionFactory permettant d'obtenir une connection vers la base de donnees
     * 
     * @param sqlConnectionFactory
     */
    public SqlDomainService(ISqlConnectionFactory sqlConnectionFactory) {
        super();
        this.queryStatementManagement = new QueryStatementManagement(sqlConnectionFactory);
    }

    /**
     * Constructeur avec un sqlConnectionFactory permettant d'obtenir une connection vers la base de donnees
     * 
     * @param sqlConnectionFactory
     */
    public SqlDomainService(SqlDatabaseEnum database, TldServiceFacade tld) throws ServiceException {
        super();
        this.queryStatementManagement = new QueryStatementManagement(PoolSqlConnectionFactoryFacade.getSqlPoolConnectionFactory(database, tld));
    }

    public SqlDomainService(SqlDatabaseEnum database, TldServiceFacade tld, IDomainService domainService) throws ServiceException {
        super(domainService);
        this.queryStatementManagement = new QueryStatementManagement(PoolSqlConnectionFactoryFacade.getSqlPoolConnectionFactory(database, tld));
    }

    @Override
    public ReservedDomainNameMotivation getReservedDomainNameMotivation(String domainName, UserId userId, TldServiceFacade tld) throws ServiceException {

        SpecialDomain specialDomain = SpecialDomainChecker.getReservedDomainNameMotivation(domainName);
        if (specialDomain != null) {
            return specialDomain;
        }

        String sql = new StringBuilder().append(" SELECT /*+ NOPARALLEL */")
                                        .append(" agtf.DOMAINE_NON_ATTRIBUABLE.LIB_CATEG ")
                                        .append(" FROM ")
                                        .append(" agtf.DOMAINE_NON_ATTRIBUABLE ")
                                        .append(" WHERE ")
                                        .append(" (ndd =? and  id_categ='COMMUNE_RSV') ")
                                        .append(" or ( ndd =? and  id_categ!='COMMUNE_RSV')")
                                        .toString();

        String canonicalDomainName = this.getCanonicalDomainName(domainName);

        if (SqlDomainService.LOGGER.isTraceEnabled())
            SqlDomainService.LOGGER.trace("Execute " + sql + " with domain=" + domainName + "(" + canonicalDomainName + ")");

        ResultSetProcessor<String> processor = new ResultSetProcessor<String>() {
            @Override
            public void populateResultFromResultSet(ResultSet resultSet) throws SQLException, ServiceException {
                if (resultSet.next()) {
                    this.result = resultSet.getString(1);
                }
            }
        };

        try {
            this.queryStatementManagement.executeQuery(processor, sql, canonicalDomainName, this.getDomainNameWithoutExtension(domainName));
            if (processor.getResult() != null) {
                return new FundamentalTerm(processor.getResult());
            } else {
                return new NormalDomain();
            }
        } catch (Exception e) {
            throw new ServiceException("getReservedDomainNameMotivation('" + domainName + "') failed", e);
        }

    }

    /**
     * Traduction de methode getCanonicalDomainname du checker car dans la base agtf, les noms de domaines sont stockés sous formes canonique
     * 
     * @param domainName
     * @return
     */
    private String getCanonicalDomainName(String domainName) {
        return domainName.trim()
                         .replaceAll("-", "")
                         .replaceAll("\\..*", "")
                         .toLowerCase();
    }

    private String getDomainNameWithoutExtension(String domainName) {
        return domainName.trim()
                         .replaceAll("\\..*", "")
                         .toLowerCase();
    }

    @Override
    public List<Domain> getDomainsWithNameContaining(String domainNameChunk, UserId userId, TldServiceFacade tld) throws ServiceException {

        if (domainNameChunk == null) {
            throw new IllegalArgumentException("domainNameChunk cannot be null");
        }

        domainNameChunk = domainNameChunk.toLowerCase();

        String sql = " SELECT "
                     + SqlDomainService.DOMAIN_SELECTION
                     + " FROM " +
                     " WHOIS.ephemeride,"
                     + " whois.domain,"
                     + " nicope.adherent,"
                     + " boa.nomdedomaine,"
                     + " whois.object_contact_r,"
                     + " whois.contact,"
                     + " whois.nh"
                     + " WHERE "
                     + " whois.domain.REF_REGISTRAR = nicope.adherent.ID "
                     + " AND whois.domain.NAME like ? "
                     + " AND boa.nomdedomaine.NOM = whois.domain.NAME"
                     + " AND whois.object_contact_r.CONTACT_ID = whois.contact.ID"
                     + " AND whois.object_contact_r.CONTACT_TYPE='HOLDER'"
                     + " AND whois.object_contact_r.object_id =  whois.domain.ID"
                     + " AND whois.nh.object_id =  whois.contact.ID "
                     + " AND whois.domain.ID=WHOIS.ephemeride.domain_id"
                     + " AND WHOIS.ephemeride.type='ANNIV'";

        if (SqlDomainService.LOGGER.isTraceEnabled())
            SqlDomainService.LOGGER.trace("Execute " + sql + " with domainName=" + domainNameChunk);

        try {
            return this.getDomainsFromSql(sql, userId, tld, "%" + domainNameChunk + "%");
        } catch (SQLException e) {
            throw new RequestFailedException("Error in getDomainsWithNameContaining(" + domainNameChunk + ")", e);
        }
    }

    @Override
    public List<Domain> getDomainsWithRegistrarCode(String code, UserId userId, TldServiceFacade tld) throws ServiceException {

        String sql = " SELECT " + SqlDomainService.DOMAIN_SELECTION + " FROM " + " WHOIS.ephemeride," + " whois.domain," + " nicope.adherent," + " boa.nomdedomaine,"
                     + " whois.object_contact_r," + " whois.contact," + " whois.nh" + " WHERE " + " whois.domain.REF_REGISTRAR = nicope.adherent.ID "
                     + " AND nicope.adherent.CODE = ?" + " AND boa.nomdedomaine.NOM = whois.domain.NAME"
                     + " AND whois.object_contact_r.CONTACT_ID = whois.contact.ID" + " AND whois.object_contact_r.CONTACT_TYPE='HOLDER'"
                     + " AND whois.object_contact_r.object_id =  whois.domain.ID" + " AND whois.nh.object_id =  whois.contact.ID "
                     + " AND whois.domain.ID=WHOIS.ephemeride.domain_id"
                     + " AND WHOIS.ephemeride.type='ANNIV'";

        if (SqlDomainService.LOGGER.isTraceEnabled())
            SqlDomainService.LOGGER.debug("Execute " + sql + "with code=" + code);

        try {
            return this.getDomainsFromSql(sql, userId, tld, code);
        } catch (SQLException e) {
            throw new RequestFailedException("getDomainsWithRegistrarCode(" + code + ") failed", e);
        }

    }

    @Override
    public List<DnsServer> getDnsServersWithDomain(String domain, UserId userId, TldServiceFacade tld) throws ServiceException {
        final String sql = " SELECT " + " whois.nserver.nom," + " whois.nserver_ip.ip," + " whois.nserver_ip.type" + " FROM " + " whois.nserver_ip, "
                           + " whois.nserver, " + "whois.nsliste_nserver_r, " + "whois.domain " + " WHERE " + " whois.nserver_ip.nserver_id = whois.nserver.id "
                           + " AND whois.nsliste_nserver_r.nserver_id = whois.nserver.id "
                           + " AND whois.nsliste_nserver_r.nsliste_id = whois.domain.nsliste_id " + " AND whois.domain.name = ? "
                           + " order by whois.nserver.nom";

        if (SqlDomainService.LOGGER.isTraceEnabled())
            SqlDomainService.LOGGER.trace("Execute " + sql + " with domain " + domain);

        try {
            return this.getDnsServersFromSql(sql, domain);
        } catch (SQLException e) {
            throw new RequestFailedException("Error in getDnsServersWithDomain(" + domain + ")", e);
        }
    }

    @Override
    public List<String> getDomainNamesWithHolderHandle(String nicHandleStr, UserId userId, TldServiceFacade tld) throws ServiceException {

        final String sql = "SELECT " + " whois.domain.NAME " + " FROM " + " whois.contact, " + " whois.nh, " + " whois.object_contact_r, "
                           + " whois.domain " + " WHERE " + " whois.nh.object_id =  whois.contact.ID "
                           + " AND whois.object_contact_r.object_id =  whois.domain.ID" + " AND whois.object_contact_r.CONTACT_ID = whois.contact.ID"
                           + " AND whois.object_contact_r.CONTACT_TYPE='HOLDER'" + " AND whois.nh.prefix = ? " + " AND whois.nh.num = ? "
                           + " AND whois.nh.suffix = ? ";

        if (SqlDomainService.LOGGER.isTraceEnabled())
            SqlDomainService.LOGGER.trace("Execute " + sql);

        NicHandle nicHandle = new NicHandle(nicHandleStr);

        try {
            return this.getDomainNamesFromSql(sql, nicHandle.getPrefix(), nicHandle.getNum(), nicHandle.getSuffix());
        } catch (SQLException e) {
            throw new RequestFailedException("getDomainNamesWithHolderHandle(" + nicHandleStr + ") failed", e);
        }
    }

    public String restrictionForSoapOperation(DomainPortfolioOperationType operation) {
        DomainStatus[] forbiddenStatus = DomainPortfolioOperationType.listStatusForbiddenForOperation(operation);
        String sql = "";
        for (int i = 0; i < forbiddenStatus.length; i++) {
            switch (forbiddenStatus[i]) {
            case Frozen:
                sql += " AND whois.domain.status <> 'FROZEN' ";
                break;

            case Active:
                sql += " AND whois.domain.status <> 'ACTIVE' ";
                break;

            case Blocked:
                sql += " AND whois.domain.status <> 'BLOCKED' ";
                break;

            case PARL:
                sql += " AND whois.domain.status <> 'PARL' ";
                break;

            }
        }
        return sql;
    }

    public String restrictionForNotSoapOperation(DomainPortfolioOperationType operation) {
        DomainStatus[] allowedStatus = DomainPortfolioOperationType.listStatusAllowedForOperation(operation);
        String sql = "";
        for (int i = 0; i < allowedStatus.length; i++) {
            switch (allowedStatus[i]) {
            case Frozen:
                sql += " AND whois.domain.status <> 'FROZEN' ";
                break;

            case Active:
                sql += " AND whois.domain.status <> 'ACTIVE' ";
                break;

            case Blocked:
                sql += " AND whois.domain.status <> 'BLOCKED' ";
                break;

            case PARL:
                sql += " AND whois.domain.status <> 'PARL' ";
                break;

            }
        }
        return sql;
    }

    @Override
    public List<String> getDomainNamesWithHolderHandleNotDeletedForSoapOperation(String nicHandleStr, DomainPortfolioOperationType operation, UserId userId, TldServiceFacade tld)
                                                                                                                                                                                  throws ServiceException {

        String sql = "SELECT " + " whois.domain.NAME " + " FROM " + " whois.contact, " + " whois.nh, " + " whois.object_contact_r, "
                     + " whois.domain " + " WHERE " + " whois.nh.object_id =  whois.contact.ID "
                     + " AND whois.object_contact_r.object_id =  whois.domain.ID" + " AND whois.object_contact_r.CONTACT_ID = whois.contact.ID"
                     + " AND whois.object_contact_r.CONTACT_TYPE='HOLDER'" + " AND whois.nh.prefix = ? " + " AND whois.nh.num = ? "
                     + " AND whois.nh.suffix = ? " + " AND whois.domain.status <> 'DELETED' ";

        sql += this.restrictionForSoapOperation(operation);

        if (SqlDomainService.LOGGER.isTraceEnabled())
            SqlDomainService.LOGGER.trace("Execute " + sql);

        NicHandle nicHandle = new NicHandle(nicHandleStr);

        try {
            return this.getDomainNamesFromSql(sql, nicHandle.getPrefix(), nicHandle.getNum(), nicHandle.getSuffix());
        } catch (SQLException e) {
            throw new RequestFailedException("getDomainNamesWithHolderHandleNotDeleted(" + nicHandleStr + ") failed", e);
        }
    }

    @Override
    public List<Domain> getDomainNamesWithHolderHandleNotDeletedForSoapOperationForbidden(String nicHandleStr, DomainPortfolioOperationType operation, UserId userId, TldServiceFacade tld)
                                                                                                                                                                                           throws ServiceException {

        String sql = "SELECT " + " whois.domain.NAME, whois.domain.status " + " FROM " + " whois.contact, " + " whois.nh, " + " whois.object_contact_r, "
                     + " whois.domain " + " WHERE " + " whois.nh.object_id =  whois.contact.ID "
                     + " AND whois.object_contact_r.object_id =  whois.domain.ID" + " AND whois.object_contact_r.CONTACT_ID = whois.contact.ID"
                     + " AND whois.object_contact_r.CONTACT_TYPE='HOLDER'" + " AND whois.nh.prefix = ? " + " AND whois.nh.num = ? "
                     + " AND whois.nh.suffix = ? " + " AND whois.domain.status <> 'DELETED' ";

        sql += this.restrictionForNotSoapOperation(operation);

        if (SqlDomainService.LOGGER.isTraceEnabled())
            SqlDomainService.LOGGER.trace("Execute " + sql);

        NicHandle nicHandle = new NicHandle(nicHandleStr);

        try {
            return this.getDomainNamesAndStatusFromSql(sql, userId, tld, nicHandle.getPrefix(), nicHandle.getNum(), nicHandle.getSuffix());
        } catch (SQLException e) {
            throw new RequestFailedException("getDomainNamesWithHolderHandleNotDeleted(" + nicHandleStr + ") failed", e);
        }
    }

    @Override
    public List<String> getDomainNamesWithHolderHandleNotDeleted(String nicHandleStr, UserId userId, TldServiceFacade tld) throws ServiceException {

        final String sql = "SELECT " + " whois.domain.NAME " + " FROM " + " whois.contact, " + " whois.nh, " + " whois.object_contact_r, "
                           + " whois.domain " + " WHERE " + " whois.nh.object_id =  whois.contact.ID "
                           + " AND whois.object_contact_r.object_id =  whois.domain.ID" + " AND whois.object_contact_r.CONTACT_ID = whois.contact.ID"
                           + " AND whois.object_contact_r.CONTACT_TYPE='HOLDER'" + " AND whois.nh.prefix = ? " + " AND whois.nh.num = ? "
                           + " AND whois.nh.suffix = ? " + " AND whois.domain.status <> 'DELETED' ";

        if (SqlDomainService.LOGGER.isTraceEnabled())
            SqlDomainService.LOGGER.trace("Execute " + sql);

        NicHandle nicHandle = new NicHandle(nicHandleStr);

        try {
            return this.getDomainNamesFromSql(sql, nicHandle.getPrefix(), nicHandle.getNum(), nicHandle.getSuffix());
        } catch (SQLException e) {
            throw new RequestFailedException("getDomainNamesWithHolderHandleNotDeleted(" + nicHandleStr + ") failed", e);
        }
    }

    private List<Domain> getDomainsFromSql(String sql, final UserId userId, final TldServiceFacade tld, Object... params) throws SQLException, ServiceException {
        ResultSetProcessor<List<Domain>> processor = new ResultSetProcessor<List<Domain>>() {
            @Override
            public void populateResultFromResultSet(ResultSet resultSet) throws SQLException, ServiceException {
                this.result = SqlDomainService.this.getDomainsFromResultSet(resultSet, userId, tld);
            }
        };

        this.queryStatementManagement.executeQuery(processor, sql, params);
        return processor.getResult();
    }

    private List<Domain> getDomainsFromSql(String sql, final UserId userId, final TldServiceFacade tld, List<Object> params) throws SQLException, ServiceException {
        ResultSetProcessor<List<Domain>> processor = new ResultSetProcessor<List<Domain>>() {
            @Override
            public void populateResultFromResultSet(ResultSet resultSet) throws SQLException, ServiceException {
                this.result = SqlDomainService.this.getDomainsFromResultSet(resultSet, userId, tld);
            }
        };

        this.queryStatementManagement.executeQuery(processor, sql, params);
        return processor.getResult();
    }

    private List<String> getDomainNamesFromSql(String sql, Object... params) throws SQLException, ServiceException {
        ResultSetProcessor<List<String>> processor = new ResultSetProcessor<List<String>>() {
            @Override
            public void populateResultFromResultSet(ResultSet resultSet) throws SQLException, RequestFailedException {
                this.result = SqlDomainService.this.getDomainNamesFromResultSet(resultSet);

            }
        };

        this.queryStatementManagement.executeQuery(processor, sql, params);
        return processor.getResult();
    }

    private List<Domain> getDomainNamesAndStatusFromSql(String sql, final UserId userId, final TldServiceFacade tld, Object... params) throws SQLException, ServiceException {
        ResultSetProcessor<List<Domain>> processor = new ResultSetProcessor<List<Domain>>() {
            @Override
            public void populateResultFromResultSet(ResultSet resultSet) throws SQLException, RequestFailedException {
                this.result = SqlDomainService.this.getDomainNamesAndStatusFromResultSet(resultSet, userId, tld);

            }
        };

        this.queryStatementManagement.executeQuery(processor, sql, params);
        return processor.getResult();
    }

    private List<String> getDomainNamesFromSql(String sql, List<Object> params) throws SQLException, ServiceException {
        ResultSetProcessor<List<String>> processor = new ResultSetProcessor<List<String>>() {
            @Override
            public void populateResultFromResultSet(ResultSet resultSet) throws SQLException, RequestFailedException {
                this.result = SqlDomainService.this.getDomainNamesFromResultSet(resultSet);

            }
        };
        this.queryStatementManagement.executeQuery(processor, sql, params);
        return processor.getResult();
    }

    private List<DnsServer> getDnsServersFromSql(String sql, Object... params) throws SQLException, ServiceException {
        ResultSetProcessor<List<DnsServer>> processor = new ResultSetProcessor<List<DnsServer>>() {
            @Override
            public void populateResultFromResultSet(ResultSet resultSet) throws SQLException, ServiceException {
                this.result = SqlDomainService.this.getDnsServersFromResultSet(resultSet);

            }
        };

        this.queryStatementManagement.executeQuery(processor, sql, params);
        return processor.getResult();
    }

    private List<Domain> getDomainsFromResultSet(ResultSet resultSet, UserId userId, TldServiceFacade tld) throws SQLException {
        List<Domain> result = new ArrayList<Domain>();
        while (resultSet.next()) {
            Domain domain = this.getDomainFromResultSetAfterCallingNext(resultSet, userId, tld);
            result.add(domain);
        }
        return result;
    }

    private List<DnsServer> getDnsServersFromResultSet(ResultSet resultSet) throws SQLException {
        List<DnsServer> result = new ArrayList<DnsServer>();

        DnsServer server = null;
        IpAddress address = null;
        while (resultSet.next()) {
            int ind = 1;

            String name = resultSet.getString(ind++);
            String ip = resultSet.getString(ind++);
            String type = resultSet.getString(ind++);

            address = null;
            if ("v4".equals(type)) {
                address = new Ipv4Address(ip);
            } else {
                address = new Ipv6Address(ip);
            }

            if (server != null && server.getName().equals(name)) {
                // meme server que le precedent mais nouvelle ip
                server.addAddress(address);
            } else {
                server = new DnsServer();
                server.setName(name);
                server.addAddress(address);
                result.add(server);
            }
        }

        return result;
    }

    private List<String> getDomainNamesFromResultSet(ResultSet resultSet) throws SQLException {
        List<String> result = new ArrayList<String>();
        while (resultSet.next()) {
            result.add(resultSet.getString(1));
        }
        return result;
    }

    private List<Domain> getDomainNamesAndStatusFromResultSet(ResultSet resultSet, UserId userId, TldServiceFacade tld) throws SQLException {
        List<Domain> result = new ArrayList<Domain>();
        while (resultSet.next()) {
            Domain domain = new Domain(userId, tld);
            domain.setName(resultSet.getString(1));
            domain.setStatus(this.convertStrToDomainStatus(resultSet.getString(2)));
            result.add(domain);
        }
        return result;
    }

    private Domain getDomainFromResultSetAfterCallingNext(ResultSet resultSet, UserId userId, TldServiceFacade tld) throws SQLException {

        Domain result = new Domain(userId, tld);
        int ind = 1;

        result.setStatus(this.convertStrToDomainStatus(resultSet.getString(ind++)));

        result.setName(resultSet.getString(ind++));

        result.setRegistrarCode(resultSet.getString(ind++));
        result.setRegistrarName(resultSet.getString(ind++));

        NicHandle holderNichandle = new NicHandle(resultSet.getString(ind++), resultSet.getString(ind++), resultSet.getString(ind++));

        result.setHolderHandle(holderNichandle.toString());

        String lastName = resultSet.getString(ind++);
        String firstName = resultSet.getString(ind++);

        String fullName = null;
        if (firstName != null) {
            fullName = firstName + " " + lastName;
        } else {
            fullName = lastName;
        }

        result.setHolderName(fullName);

        result.setAnniversaryDate(resultSet.getDate(ind++));

        DomainNameDetail detail = new DomainNameDetail();
        detail.setLdh(result.getName());
        detail.setUtf8(resultSet.getString(ind++));
        detail.setAsciiEquivalent(resultSet.getString(ind++));

        if (detail.getUtf8() == null) {
            detail.setUtf8(result.getName());
        }

        if (detail.getAsciiEquivalent() == null) {
            detail.setAsciiEquivalent(result.getName());
        }

        result.setNameDetail(detail);

        return result;
    }

    private DomainStatus convertStrToDomainStatus(String strStatus) {
        if ("ACTIVE".equals(strStatus))
            return DomainStatus.Active;
        if ("BLOCKED".equals(strStatus))
            return DomainStatus.Blocked;
        if ("FROZEN".equals(strStatus))
            return DomainStatus.Frozen;
        if ("PENDING_LEGAL".equals(strStatus))
            return DomainStatus.PendingLegal;
        if ("DELETED".equals(strStatus))
            return DomainStatus.Deleted;
        if ("REGISTERED".equals(strStatus))
            return DomainStatus.Registred;
        if ("UNREGISTRABLE".equals(strStatus))
            return DomainStatus.Unregistrable;
        if ("PARL".equals(strStatus))
            return DomainStatus.PARL;
        if ("NOT_OPEN".equals(strStatus))
            return DomainStatus.Registred;

        SqlDomainService.LOGGER.warn("Unknown domain status: " + strStatus);
        return null;
    }

    /**
     * 
     * @param criteria
     * @param lookForFreeDomain un array afin de faire un passage par référence et non par copie du boolean. Le tableau passé doit toujours être de dimension 1
     * @return
     */
    private SQLSelectQueryBuilder buildMainSearchDomainQuery(final DomainSearchCriteria criteria, boolean[] lookForFreeDomain) {
        SQLSelectQueryBuilder builder = new SQLSelectQueryBuilder();
        String domainName = criteria.getDomainName();
        String registrar = criteria.getRegistrarNameOrCode();
        String holderHandle = criteria.getHolderHandle();
        String holderName = criteria.getHolderName();
        String serverName = criteria.getServerName();
        Date anniversaryDateDebut = criteria.getAnniversaryDateDebut();
        Date anniversaryDateFin = criteria.getAnniversaryDateFin();
        String legalStructureIdentifier = criteria.getSiret();
        boolean notLike = criteria.getDomainNameLike();
        boolean holderNotLike = criteria.getHolderNameLike();
        boolean registrarNotLike = criteria.getRegistrarNameLike();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM");

        builder.appendSelect(SqlDomainService.DOMAIN_SELECTION);
        builder.appendFrom("WHOIS.ephemeride, whois.domain, nicope.adherent,  whois.object_contact_r, whois.contact, whois.nh,"
                           + " boa.nomdedomaine");

        builder.appendWhere(REQ_SEARCH_DOMAIN_WHERE);

        if ((domainName != null) && (!"".equals(domainName))) {
            if (notLike) {
                builder.appendWhere(" AND (nomdedomaine.nom = ? ");
                builder.addParameters(domainName);
                builder.appendWhere(" or nomdedomaine.nom_bundle = ? ");
                builder.addParameters(domainName);
                builder.appendWhere(" or nomdedomaine.nom_i18n = ?) ");
                builder.addParameters(domainName);
            } else {
                builder.appendFirstSelect(" /*+ dynamic_sampling(domain,10) parallel(domain, 10) */ ");
                builder.appendWhere(" AND (nomdedomaine.nom like ?");
                builder.addParameters("%" + domainName + "%");
                builder.appendWhere(" or nomdedomaine.nom_bundle like ? ");
                builder.addParameters("%" + domainName + "%");
                builder.appendWhere(" or nomdedomaine.nom_i18n like ? )");
                builder.addParameters("%" + domainName + "%");
            }
        }

        if ((registrar != null) && (!"".equals(registrar))) {
            builder.appendFirstSelect(" /*+ dynamic_sampling(adherent,10) parallel(adherent, 10) */ ");
            if (registrarNotLike) {
                builder.appendWhere(" AND (upper(nicope.adherent.NOM) = upper(?) OR upper(nicope.adherent.CODE) = upper(?))");
                builder.addParameters(registrar);
                builder.addParameters(registrar);
            } else {
                builder.appendWhere(" AND (upper(nicope.adherent.NOM) like upper(?) OR upper(nicope.adherent.CODE) like upper(?))");
                builder.addParameters(registrar + "%");
                builder.addParameters(registrar + "%");
            }
            lookForFreeDomain[0] = false;
        }

        if ((legalStructureIdentifier != null) && (!"".equals(legalStructureIdentifier))) {
            builder.appendWhere(" AND whois.identification2.contact_id=whois.contact.id AND whois.identification2.data = ?");
            builder.appendFrom(" , whois.identification2");
            builder.addParameters(legalStructureIdentifier);
            lookForFreeDomain[0] = false;
        }

        if ((holderHandle != null) && (!"".equals(holderHandle))) {
            builder.appendWhere(" AND whois.nh.PREFIX||TO_CHAR(whois.nh.NUM)||'-'||whois.nh.SUFFIX = ?");
            builder.addParameters(holderHandle);
            lookForFreeDomain[0] = false;
        }

        if ((holderName != null) && (!"".equals(holderName))) {
            if (holderNotLike) {
                builder.appendWhere(" AND (whois.contact.skey = ? OR lower(whois.contact.prenom) = ? OR lower(whois.contact.nom) = ?)");
                builder.appendFirstSelect(" /*+ dynamic_sampling(contact,10) parallel(contact, 10) */ ");
                builder.addParameters(holderName.toLowerCase());
                builder.addParameters(holderName.toLowerCase());
                builder.addParameters(holderName.toLowerCase());
            } else {
                builder.appendWhere(" AND (whois.contact.skey like ?  OR lower(whois.contact.prenom) like ? OR lower(whois.contact.nom) like ?)");
                builder.appendFirstSelect(" /*+ dynamic_sampling(contact,10) parallel(contact, 10) */ ");
                builder.addParameters(holderName.toLowerCase() + "%"); //commence par
                builder.addParameters(holderName.toLowerCase() + "%");
                builder.addParameters(holderName.toLowerCase() + "%");
            }
            lookForFreeDomain[0] = false;
        }

        if ((serverName != null) && (!"".equals(serverName))) {
            builder.appendWhere(" AND whois.nsliste_nserver_r.nsliste_id = whois.domain.nsliste_id AND whois.nsliste_nserver_r.nserver_id = whois.nserver.id");
            builder.appendWhere(" AND whois.nserver_ip.nserver_id = whois.nserver.id AND (whois.nserver.nom = ? OR whois.nserver_ip.ip = ?)");
            builder.appendFrom(" , whois.nsliste_nserver_r, whois.nserver, whois.nserver_ip");
            builder.addParameters(serverName);
            builder.addParameters(serverName);
            lookForFreeDomain[0] = false;
        }

        if ((anniversaryDateDebut != null)) {
            if ((anniversaryDateFin != null)) {
                String[] anniversaryDateDebutSplit = sdf.format(anniversaryDateDebut).split("/");
                String[] anniversaryDateFinSplit = sdf.format(anniversaryDateFin).split("/");
                builder.appendWhere(" AND to_char(whois.ephemeride.the_date, 'mm') >= ?");
                builder.appendWhere(" AND to_char(whois.ephemeride.the_date, 'DD') >= ?");
                builder.appendWhere(" AND to_char(whois.ephemeride.the_date, 'mm') <= ?");
                builder.appendWhere(" AND to_char(whois.ephemeride.the_date, 'DD') <= ?");
                builder.addParameters(anniversaryDateDebutSplit[1]);
                builder.addParameters(anniversaryDateDebutSplit[0]);
                builder.addParameters(anniversaryDateFinSplit[1]);
                builder.addParameters(anniversaryDateFinSplit[0]);
            } else {
                String[] anniversaryDateDebutSplit = sdf.format(anniversaryDateDebut).split("/");
                builder.appendWhere(" AND to_char(whois.ephemeride.the_date, 'mm') = ?");
                builder.appendWhere(" AND to_char(whois.ephemeride.the_date, 'DD') = ?");
                builder.addParameters(anniversaryDateDebutSplit[1]);
                builder.addParameters(anniversaryDateDebutSplit[0]);
            }
            lookForFreeDomain[0] = false;
        }

        return builder;
    }

    public SQLSelectQueryBuilder buildFreeDomainSearchDomainQuery(String domainName, boolean like, UserId userId, TldServiceFacade tld) {
        SQLSelectQueryBuilder builder = new SQLSelectQueryBuilder();
        String nameToFind = domainName;
        try {
            DomainNameDetail detail = AppServiceFacade.getQualityService().normalizeDomainName(nameToFind, userId, tld);
            nameToFind = detail.getLdh();
        } catch (ServiceException e) {
            LOGGER.error("impossible de normaliser le nom de domaine : " + e, e);
        }
        builder.appendSelect("nom ");
        builder.appendFrom("boa.nomdedomaine");
        if (like) {
            builder.appendWhere("nom = ?");
            builder.addParameters(nameToFind);
        } else {
            builder.appendWhere("nom like ?");
            builder.addParameters("%" + nameToFind + "%");
        }
        return builder;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Domain> searchDomain(final DomainSearchCriteria criteria, UserId userId, TldServiceFacade tld) throws ServiceException {
        String domainName = criteria.getDomainName();
        boolean notLike = criteria.getDomainNameLike();
        List<Domain> listDomain = null;
        List<String> listDomainFree = null;
        boolean[] lookForFreeDomain = new boolean[] { true };

        SQLSelectQueryBuilder builder = this.buildMainSearchDomainQuery(criteria, lookForFreeDomain);

        try {
            listDomain = this.getDomainsFromSql(builder.getQuery(), userId, tld, builder.getListParameters());
        } catch (SQLException e) {
            throw new RequestFailedException("searchDomain(" + criteria + ") failed, query : " + builder.getQuery(), e);
        }

        if (lookForFreeDomain[0]) {
            builder = this.buildFreeDomainSearchDomainQuery(domainName, notLike, userId, tld);
        }

        try {
            if (lookForFreeDomain[0]) {
                listDomainFree = this.getDomainNamesFromSql(builder.getQuery(), builder.getListParameters());
            }
        } catch (SQLException e) {
            throw new RequestFailedException("searchDomain(" + criteria + ") failed, query : " + builder.getQuery(), e);
        }

        if ((listDomainFree != null) && (listDomainFree.size() != 0)) {
            Map<String, Domain> domainNames = new HashMap<String, Domain>();
            for (Domain domain : listDomain) {
                domainNames.put(domain.getName(), domain);
            }
            if ((listDomainFree != null) && (listDomainFree.size() != 0)) {
                for (String domainFree : listDomainFree) {
                    if (!domainNames.containsKey(domainFree)) {
                        Domain newDomain = new Domain(userId, tld);
                        newDomain.setName(domainFree);
                        newDomain.setStatus(DomainStatus.Free);
                        domainNames.put(domainFree, newDomain);
                        listDomain.add(newDomain);
                    }
                }
            }
        }

        Collections.sort(listDomain, DOMAIN_COMPARATOR);
        return listDomain;
    }

    @Override
    public List<DSServer> getDSServerWithDomain(String domain, UserId userId, TldServiceFacade tld) throws ServiceException {
        final String sql = " SELECT " + " whois.dnssec_ds.key_tag," + " whois.dnssec_ds.algorithm," + " whois.dnssec_ds.dgst_method," + " whois.dnssec_ds.dgst_value" + " FROM " + " whois.dnssec_ds, "
                           + " whois.dsliste_ds_r, " + "whois.domain " + " WHERE " + " whois.dnssec_ds.id = whois.dsliste_ds_r.ds_id "
                           + " AND whois.dsliste_ds_r.dsliste_id = whois.domain.dsliste_id "
                           + " AND whois.domain.name = ? "
                           + " order by whois.dnssec_ds.id";

        if (SqlDomainService.LOGGER.isTraceEnabled())
            SqlDomainService.LOGGER.trace("Execute " + sql + " with domain " + domain);

        try {
            return this.getDSServersFromSql(sql, domain, userId, tld);
        } catch (SQLException e) {
            throw new RequestFailedException("Error in getDnsServersWithDomain(" + domain + ")", e);
        }
    }

    private List<DSServer> getDSServersFromSql(String sql, String params, final UserId userId, final TldServiceFacade tld) throws SQLException, ServiceException {
        ResultSetProcessor<List<DSServer>> processor = new ResultSetProcessor<List<DSServer>>() {
            @Override
            public void populateResultFromResultSet(ResultSet resultSet) throws SQLException, ServiceException {
                this.result = SqlDomainService.this.getDSServersFromResultSet(resultSet, userId, tld);

            }
        };

        this.queryStatementManagement.executeQuery(processor, sql, params);
        return processor.getResult();
    }

    protected List<DSServer> getDSServersFromResultSet(ResultSet resultSet, UserId userId, TldServiceFacade tld) throws SQLException, ServiceException {
        List<DSServer> result = new ArrayList<DSServer>();

        DSServer server = null;
        while (resultSet.next()) {

            int ind = 1;
            server = new DSServer();
            server.setKeyTag(resultSet.getInt(ind++));
            server.setAlgoHash(SqlConverterFacade.convert(resultSet.getInt(ind++), AlgoHash.class, userId, tld));
            server.setDigestHash(SqlConverterFacade.convert(resultSet.getInt(ind++), DigestHash.class, userId, tld));
            server.setKeyDigest(resultSet.getString(ind++));
            result.add(server);
        }

        return result;
    }
}
