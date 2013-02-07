/*
 * $Id: SqlContactService.java,v 1.25 2010/10/14 07:33:53 ginguene Exp $ $Revision: 1.25 $ $Author: ginguene $
 */

package fr.afnic.commons.services.sql;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import fr.afnic.commons.beans.NicHandle;
import fr.afnic.commons.beans.WhoisContact;
import fr.afnic.commons.beans.domain.DomainContactType;
import fr.afnic.commons.beans.logs.ILogger;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.beans.search.whoiscontact.WhoisContactSearchCriteria;
import fr.afnic.commons.services.IWhoisContactService;
import fr.afnic.commons.services.exception.RequestFailedException;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.proxy.ProxyWhoisContactService;
import fr.afnic.commons.services.sql.utils.QueryStatementManagement;
import fr.afnic.commons.services.sql.utils.SQLSelectQueryBuilder;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.commons.utils.Preconditions;
import fr.afnic.utils.sql.ISqlConnectionFactory;
import fr.afnic.utils.sql.SqlFacade;
import fr.afnic.utils.sql.pool.PoolSqlConnectionFactoryFacade;
import fr.afnic.utils.sql.pool.SqlDatabaseEnum;

public class SqlWhoisContactService extends ProxyWhoisContactService {

    private static final ILogger LOGGER = AppServiceFacade.getLoggerService().getLogger(SqlWhoisContactService.class);

    private ISqlConnectionFactory sqlConnectionFactory = null;

    /**
     * Constructeur avec un sqlConnectionFactory permettant d'obtenir une connection vers la base de donnees
     * 
     * @param sqlConnectionFactory
     */
    public SqlWhoisContactService(ISqlConnectionFactory sqlConnectionFactory) throws ServiceException {
        this.sqlConnectionFactory = sqlConnectionFactory;
    }

    /**
     * Constructeur avec un sqlConnectionFactory permettant d'obtenir une connection vers la base de donnees
     * 
     * @param sqlConnectionFactory
     */
    public SqlWhoisContactService(SqlDatabaseEnum database, TldServiceFacade tld) throws ServiceException {
        this.sqlConnectionFactory = PoolSqlConnectionFactoryFacade.getSqlPoolConnectionFactory(database, tld);
    }

    public SqlWhoisContactService(SqlDatabaseEnum database, TldServiceFacade tld, IWhoisContactService delegationService) throws ServiceException {
        super(delegationService);
        this.sqlConnectionFactory = PoolSqlConnectionFactoryFacade.getSqlPoolConnectionFactory(database, tld);

    }

    @Override
    public List<String> getDomainContactHandles(String domainName, DomainContactType contactType, UserId userId, TldServiceFacade tld) throws ServiceException {

        String sql = new StringBuilder().append("select ")
                                        .append("whois.nh.prefix nh_prefix,")
                                        .append("whois.nh.num nh_num,")
                                        .append("whois.nh.suffix nh_suffix")
                                        .append(" FROM ")
                                        .append("whois.domain,")
                                        .append("whois.object_contact_r,")
                                        .append("whois.contact,")
                                        .append("whois.nh ")
                                        .append(" WHERE ")
                                        .append(" whois.object_contact_r.object_id =  whois.domain.ID ")
                                        .append(" and whois.object_contact_r.contact_type=? ")
                                        .append(" AND whois.object_contact_r.CONTACT_ID = whois.contact.ID ")
                                        .append(" AND whois.nh.object_id =  whois.contact.ID ")
                                        .append(" and whois.domain.NAME= ? ").toString();

        if (SqlWhoisContactService.LOGGER.isTraceEnabled()) SqlWhoisContactService.LOGGER.trace("Execute " + sql + " with " + domainName);

        final ResultSetProcessor<List<String>> processor = new ResultSetProcessor<List<String>>() {
            @Override
            public void populateResultFromResultSet(final ResultSet resultSet) throws SQLException, RequestFailedException {
                this.result = new ArrayList<String>();
                while (resultSet.next()) {
                    this.result.add(new NicHandle(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3)).toString());
                }
            }
        };

        try {
            QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlConnectionFactory);
            queryStatementManagement.executeQuery(processor, sql, contactType, domainName);
            return processor.getResult();
        } catch (final SQLException e) {
            throw new RequestFailedException("getDomainContactHandles(" + domainName + ", " + contactType + ") failed", e);
        }

    }

    @Override
    public List<String> getNicHandlesToQualify(Date minDate, int nbResults, UserId userId, TldServiceFacade tld) throws ServiceException {

        if (nbResults <= 0) {
            return Collections.emptyList();
        }

        final String sql = "select handle from (select * from v_nichandles_to_qualify where the_date > ? ORDER BY dbms_random.value) where rownum <= ?"; //auto

        if (SqlWhoisContactService.LOGGER.isTraceEnabled()) SqlWhoisContactService.LOGGER.trace("Execute " + sql);

        final ResultSetProcessor<List<String>> processor = new ResultSetProcessor<List<String>>() {
            @Override
            public void populateResultFromResultSet(final ResultSet resultSet) throws SQLException, ServiceException {
                this.result = SqlWhoisContactService.this.getStringsFromResultSet(resultSet);
            }
        };

        try {
            QueryStatementManagement queryMgt = new QueryStatementManagement(SqlFacade.getBoaFactory());
            queryMgt.executeQuery(processor, sql, minDate, nbResults);
            return processor.getResult();
        } catch (final SQLException e) {
            throw new RequestFailedException("getContactsToIdentify() failed", e);
        }

    }

    @Override
    public List<String> getNicHandlesToSurvey(UserId userId, TldServiceFacade tld) throws ServiceException {

        final String sql = "select handle from v_nichandles_to_survey"; //sig

        if (SqlWhoisContactService.LOGGER.isTraceEnabled()) SqlWhoisContactService.LOGGER.trace("Execute " + sql);

        final ResultSetProcessor<List<String>> processor = new ResultSetProcessor<List<String>>() {
            @Override
            public void populateResultFromResultSet(final ResultSet resultSet) throws SQLException, ServiceException {
                this.result = SqlWhoisContactService.this.getStringsFromResultSet(resultSet);
            }
        };

        try {
            QueryStatementManagement queryMgt = new QueryStatementManagement(SqlFacade.getBoaFactory());
            queryMgt.executeQuery(processor, sql);
            return processor.getResult();
        } catch (final SQLException e) {
            throw new RequestFailedException("getContactsToIdentify() failed", e);
        }
    }

    private List<String> getStringsFromResultSet(final ResultSet resultSet) throws SQLException, ServiceException {
        final List<String> result = new ArrayList<String>();

        while (resultSet.next()) {
            result.add(resultSet.getString(1));
        }
        return result;
    }

    @Override
    public boolean isExistingNicHandle(String nicHandle, UserId userId, TldServiceFacade tld) throws ServiceException {
        Preconditions.checkNicHandle(nicHandle, "nicHandle");
        QueryStatementManagement queryStatementManagement = new QueryStatementManagement(SqlFacade.getBoaFactory());
        String sqlQuery = "select pkg_utils.isexistingnichandle(?) from dual";

        PreparedStatement preparedStatement = queryStatementManagement.initializeStatement(sqlQuery);
        try {
            preparedStatement.setString(1, nicHandle);
        } catch (SQLException e) {
            throw new ServiceException("isExistingNicHandle(" + nicHandle + ") failed.", e);
        }
        int nbFoundNicHandle = queryStatementManagement.executeStatement(preparedStatement);
        return nbFoundNicHandle > 0;
    }

    @Override
    public List<WhoisContact> getHoldersWithRegistrarCode(final String customerId, UserId userId, TldServiceFacade tld) throws ServiceException {
        Preconditions.checkNotNull(customerId, "customerId");
        QueryStatementManagement queryStatementManagement = new QueryStatementManagement(SqlFacade.getBoaFactory());

        String sqlQuery = "select  nom, prenom, whois.address.data, whois.address.commune, whois.address.cedex, whois.address.zip,"
                          + " ( select  whois.media.data from whois.contact_media_r, whois.media "
                          + " where  whois.contact_media_r.media_id= whois.media.id and whois.media.type= 'EMAIL'"
                          + " and  whois.contact_media_r.CONTACT_ID  = contactid) as mediadata,"
                          + " whois.nh.prefix, whois.nh.num, whois.nh.suffix ,   '" + customerId + "' as registrar_code"
                          + " FROM whois.contact_address_r, whois.address,  whois.nh, "
                          + " (SELECT /*+ parallel(domain, 5) */ distinct whois.contact.id as contactid, whois.contact.nom, whois.contact.prenom"
                          + "  FROM whois.domain, nicope.adherent, whois.object_contact_r, whois.contact"
                          + "        WHERE  whois.domain.REF_REGISTRAR = nicope.adherent.ID AND whois.object_contact_r.CONTACT_ID = whois.contact.ID AND"
                          + "                     whois.object_contact_r.CONTACT_TYPE='HOLDER' AND whois.object_contact_r.object_id =  whois.domain.ID AND nicope.adherent.CODE = ?)"

                          + " WHERE whois.contact_address_r.address_id = whois.address.id "
                          + " and whois.contact_address_r.contact_id = contactid "
                          + " and whois.nh.object_id = contactid";

        PreparedStatement preparedStatement = queryStatementManagement.initializeStatement(sqlQuery);
        try {
            preparedStatement.setString(1, customerId);

            @SuppressWarnings("rawtypes")
            List<WhoisContact> listHolders = queryStatementManagement.executeStatementList(WhoisContact.class, preparedStatement, userId, tld);

            List<WhoisContact> ret = new ArrayList<WhoisContact>();
            for (WhoisContact contact : listHolders) {
                ret.add(contact);
            }
            return ret;
        } catch (Exception e) {
            throw new ServiceException("getListHolderWithCustomerId(" + customerId + ") failed.", e);
        }

    }

    @Override
    public List<WhoisContact> searchContact(WhoisContactSearchCriteria criteria, UserId userId, TldServiceFacade tld) throws ServiceException {
        Preconditions.checkNotNull(criteria, "criteria");

        try {
            SQLSelectQueryBuilder builder = this.buildSearchQuery(criteria);

            QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlConnectionFactory);

            LOGGER.debug("searchContact with query " + builder.getQuery());

            PreparedStatement preparedStatement = queryStatementManagement.initializeStatement(builder.getQuery());
            QueryStatementManagement.initPreparedStatementParameters(preparedStatement, builder.getListParameters());

            try {
                List<WhoisContact> listHolders = queryStatementManagement.executeStatementList(WhoisContact.class, preparedStatement, userId, tld);

                //Bout de code dû à un problème des générique => WhoisContact n'est pas considéré comme étant du même type que WhoisContact dans les listes
                List<WhoisContact> ret = new ArrayList<WhoisContact>();
                for (WhoisContact contact : listHolders) {
                    ret.add(contact);
                }
                return ret;
            } catch (ServiceException e) {
                //throw new ServiceE
                throw e;
            }

        } catch (SQLException e) {
            throw new ServiceException("searchContact " + criteria.getMap() + " failed", e);
        }
    }

    private SQLSelectQueryBuilder buildSearchQuery(final WhoisContactSearchCriteria criteria) throws ServiceException {
        SQLSelectQueryBuilder builder = new SQLSelectQueryBuilder();

        String select = "distinct(whois.contact.ID) as contactId,"
                        + " whois.contact.prenom as prenom,"
                        + " whois.contact.nom as nom,"
                        + " whois.address.data as data,"
                        + " whois.address.commune as commune, "
                        + " whois.address.cedex as cedex, "
                        + " whois.address.zip as zip, "
                        + " whois.media.data as mediadata,"
                        + " whois.nh.prefix  as prefix, "
                        + " whois.nh.num as num, "
                        + " whois.nh.suffix as suffix, "
                        + " nicope.adherent.CODE as registrar_code";
        builder.appendSelect(select);

        builder.appendFrom("nicope.adherent, whois.contact,whois.nh, whois.media,whois.contact_address_r, whois.contact_media_r, whois.address");

        builder.appendWhere("whois.contact_address_r.address_id = whois.address.id"
                            + " and whois.contact_address_r.contact_id = whois.contact.ID  "
                            + " and whois.contact.ID = whois.contact_media_r.contact_id (+) "
                            + " and whois.contact_media_r.media_id= whois.media.id (+) "
                            + " and whois.media.type(+)= 'EMAIL'"
                            + " and whois.nh.object_id = whois.contact.ID"
                            + " and whois.contact.ref_registrar = adherent.id");

        builder.appendWhere(" and (whois.media.data = "
                            + "(select max(data2.data) from whois.media data2 where whois.contact_media_r.media_id= data2.id)"
                            + " or (whois.media.data is null and "
                            + "( select max(data2.data) from whois.media data2 where whois.contact_media_r.media_id= data2.id)is null))");

        if (criteria.getDomainName() != null && !StringUtils.isBlank(criteria.getDomainName())) {
            builder.appendFrom(",whois.domain,whois.object_contact_r");
            builder.appendWhere(" and whois.object_contact_r.CONTACT_ID(+) = whois.contact.ID "
                                + " and whois.object_contact_r.object_id =  whois.domain.ID(+) ");

            // vaut vrai si recherche exacte
            if (!criteria.getDomainNameLike()) {
                builder.appendWhere(" and whois.domain.name like ?");
                builder.addParameters("%" + criteria.getDomainName() + "%");
            } else {
                builder.appendWhere(" and whois.domain.name = ?");
                builder.addParameters(criteria.getDomainName());
            }
        }

        if (criteria.getWhoisContactName() != null && !StringUtils.isBlank(criteria.getWhoisContactName())) {
            //vaut vrai si recherche exacte
            if (!criteria.getWhoisContactNameLike()) {
                builder.appendWhere(" and whois.contact.nom like ?");
                builder.addParameters("%" + criteria.getWhoisContactName() + "%");
            } else {
                builder.appendWhere(" and whois.contact.nom = ?");
                builder.addParameters(criteria.getWhoisContactName());
            }
        }

        if (criteria.getWhoisContactNicHandle() != null && !StringUtils.isBlank(criteria.getWhoisContactNicHandle())) {
            NicHandle nicHandle = new NicHandle(criteria.getWhoisContactNicHandle());
            builder.appendWhere(" and whois.nh.prefix = ?");
            builder.appendWhere(" and whois.nh.num = ?");
            builder.appendWhere(" and whois.nh.suffix = ?");

            builder.addParameters(nicHandle.getPrefix());
            builder.addParameters(nicHandle.getNumAsInt());
            builder.addParameters(nicHandle.getSuffix());
        }

        if (criteria.getWhoisContactIdentifier() != null && !StringUtils.isBlank(criteria.getWhoisContactIdentifier())) {
            builder.appendWhere(" AND whois.identification2.contact_id=whois.contact.id AND whois.identification2.data = ?");
            builder.appendFrom(" , whois.identification2");
            builder.addParameters(criteria.getWhoisContactIdentifier());
        }

        return builder;
    }

}
