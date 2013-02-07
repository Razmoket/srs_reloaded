package fr.afnic.commons.services.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;

import fr.afnic.commons.beans.OperationForm;
import fr.afnic.commons.beans.logs.ILogger;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.beans.search.form.FormSearchCriteria;
import fr.afnic.commons.beans.validatable.OperationFormId;
import fr.afnic.commons.services.IOperationFormService;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.proxy.ProxyOperationFormService;
import fr.afnic.commons.services.sql.dbutils.QueryManager;
import fr.afnic.commons.services.sql.dbutils.StringHandler;
import fr.afnic.commons.services.sql.utils.QueryStatementManagement;
import fr.afnic.commons.services.sql.utils.SQLSelectQueryBuilder;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.commons.utils.Preconditions;
import fr.afnic.utils.closer.CloseException;
import fr.afnic.utils.closer.Closer;
import fr.afnic.utils.sql.ISqlConnectionFactory;
import fr.afnic.utils.sql.pool.PoolSqlConnectionFactoryFacade;
import fr.afnic.utils.sql.pool.SqlDatabaseEnum;

public class SqlOperationFormService extends ProxyOperationFormService {

    /** Definition du Logger de la classe SqlOperationFormService */
    private static final ILogger LOGGER = AppServiceFacade.getLoggerService().getLogger(SqlOperationFormService.class);

    private static final String OPERATION_FORM_SEARCH_SELECTION = new StringBuffer().append(" foprinc.num as formId,")
                                                                                    .append(" nomdedomaine.nom as domainName,")
                                                                                    .append(" ticketdescoper.gw_operation_name as ticketType").toString();

    private static final String OPERATION_FORM_SEARCH_WHERE = new StringBuffer().append(" ticketinfo.idtick = ticket.ID")
                                                                                .append(" and ticket.IDOPE = ticketdescoper.id")
                                                                                .append(" and foprinc.num = ticketinfo.numfo")
                                                                                .append(" and foprinc.idnomdom = nomdedomaine.id").toString();

    private ISqlConnectionFactory sqlConnectionFactory = null;

    private final QueryManager queryManager;

    public SqlOperationFormService(SqlDatabaseEnum database, TldServiceFacade tld, IOperationFormService delegationService) throws ServiceException {
        super(delegationService);
        this.sqlConnectionFactory = PoolSqlConnectionFactoryFacade.getSqlPoolConnectionFactory(database, tld);
        this.queryManager = new QueryManager(this.sqlConnectionFactory);
    }

    public SqlOperationFormService(final ISqlConnectionFactory sqlConnectionFactory, IOperationFormService delegationService) {
        super(delegationService);
        this.sqlConnectionFactory = sqlConnectionFactory;
        this.queryManager = new QueryManager(sqlConnectionFactory);
    }

    public SqlOperationFormService(final ISqlConnectionFactory sqlConnectionFactory) {
        this.sqlConnectionFactory = sqlConnectionFactory;
        this.queryManager = new QueryManager(sqlConnectionFactory);
    }

    @Override
    public String getLastTransmissionCommentForDomainName(final String pNdd, UserId userId, TldServiceFacade tld) throws ServiceException {
        final String sqlQuery = "SELECT fop.reference, tick.id FROM foprinc FOP, ticketinfo TIC, ticket tick, nomdedomaine nom "
                                + "WHERE fop.num = tic.numfo AND tic.idtick = tick.id AND tick.idnomdom = nom.id AND nom.nom = ? and tick.ope = 'Transmission' order by tick.id";
        String retour = "";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = this.sqlConnectionFactory.createConnection();
            preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1, pNdd);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                retour = resultSet.getString(1);
            }
            resultSet.close();
            preparedStatement.close();
        } catch (final SQLException e) {
            throw new ServiceException("getLastTransmissionCommentForDomainName(" + pNdd + ")  failed", e);
        } finally {
            try {
                Closer.close(preparedStatement, resultSet);
            } catch (final CloseException e) {
                throw new ServiceException("getLastTransmissionCommentForDomainName(" + pNdd + ") failed", e);
            }
            this.sqlConnectionFactory.closeConnection(connection);
        }

        return retour;
    }

    @Override
    public String getOperationFormInitialContent(final OperationFormId id, UserId userId, TldServiceFacade tld) throws ServiceException {
        String retour = null;
        final String sql = "select donneformonlinebd(?)  from dual";

        if (SqlOperationFormService.LOGGER.isDebugEnabled()) {
            SqlOperationFormService.LOGGER.debug("Executing " + sql);
        }

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = this.sqlConnectionFactory.createConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id.getIntValue());
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                retour = resultSet.getString(1);
            }
            resultSet.close();
            preparedStatement.close();
        } catch (final SQLException e) {
            throw new ServiceException("getOperationFormInitialContent(" + id + ")  failed", e);
        } finally {
            try {
                Closer.close(preparedStatement, resultSet);
            } catch (final CloseException e) {
                throw new ServiceException("populateCountryCodenMap() failed", e);
            }
            this.sqlConnectionFactory.closeConnection(connection);
        }

        return retour;
    }

    @Override
    public void archiveOperationForm(final OperationFormId operationFormId, UserId userId, TldServiceFacade tld) throws ServiceException {
        Preconditions.checkParameter(operationFormId, "operationFormId");
        AppServiceFacade.getOperationFormService().getOperationFormWithId(operationFormId, userId, tld);

        final String sql = "update nicope.foprinc set status='Ar' where num=?";

        final QueryRunner runner = new QueryRunner();

        try {
            runner.update(this.sqlConnectionFactory.createConnection(), sql, operationFormId.getValue());
        } catch (final SQLException e) {
            throw new ServiceException("archiveOperationForm(" + operationFormId + ") failed", e);
        }
    }

    @Override
    public boolean isArchived(final OperationFormId operationFormId, UserId userId, TldServiceFacade tld) throws ServiceException {
        final String sql = "select nicope.foprinc.status from nicope.foprinc where nicope.foprinc.num=?";

        try {
            final String status = this.queryManager.executeQuery(sql, new StringHandler(), operationFormId.getValue());

            return "Ar".equals(status);
        } catch (final SQLException e) {
            throw new ServiceException("isArchived('" + operationFormId + "') failed query:" + sql, e);
        }
    }

    /**
     * 
     * @param criteria
     * @param lookForFreeDomain un array afin de faire un passage par référence et non par copie du boolean. Le tableau passé doit toujours être de dimension 1
     * @return
     */
    private SQLSelectQueryBuilder buildMainSearchOperationFormQuery(final FormSearchCriteria criteria) {
        SQLSelectQueryBuilder builder = new SQLSelectQueryBuilder();
        String formId = criteria.getFormId();
        String domainName = criteria.getDomainName();
        String ticketId = criteria.getTicketId();
        String holderHandle = criteria.getHolderHandle();
        String adminHandle = criteria.getAdminHandle();
        String registrar = criteria.getRegistrar();
        boolean whoisDomainAdded = false;

        builder.appendSelect(SqlOperationFormService.OPERATION_FORM_SEARCH_SELECTION);
        builder.appendFrom("nicope.ticketinfo, nicope.ticket, nicope.ticketdescoper, nicope.foprinc, boa.nomdedomaine");

        builder.appendWhere(OPERATION_FORM_SEARCH_WHERE);

        if ((formId != null) && (!"".equals(formId))) {
            builder.appendWhere(" and foprinc.num = ? ");
            builder.addParameters(formId);
        }

        if ((domainName != null) && (!"".equals(domainName))) {
            builder.appendWhere(" AND (nomdedomaine.nom = ? ");
            builder.addParameters(domainName);
            builder.appendWhere(" or nomdedomaine.nom_i18n = ?) ");
            builder.addParameters(domainName);
        }

        if ((ticketId != null) && (!"".equals(ticketId))) {
            builder.appendWhere(" and ticket.ID = ? ");
            builder.addParameters(ticketId);
        }

        if ((holderHandle != null) && (!"".equals(holderHandle))) {
            if (!whoisDomainAdded) {
                builder.appendFrom(", whois.domain");
                builder.appendWhere(" AND nomdedomaine.NOM = whois.domain.NAME");
                whoisDomainAdded = true;
            }
            builder.appendFrom(", whois.object_contact_r, whois.contact, whois.nh");
            builder.appendWhere(" AND whois.object_contact_r.CONTACT_ID = whois.contact.ID");
            builder.appendWhere(" AND whois.object_contact_r.CONTACT_TYPE='HOLDER'");
            builder.appendWhere(" AND whois.object_contact_r.object_id =  whois.domain.ID");
            builder.appendWhere(" AND whois.nh.object_id =  whois.contact.ID");
            builder.appendWhere(" AND whois.nh.PREFIX||TO_CHAR(whois.nh.NUM)||'-'||whois.nh.SUFFIX = ?");
            builder.addParameters(holderHandle);
        }

        if ((adminHandle != null) && (!"".equals(adminHandle))) {
            if (!whoisDomainAdded) {
                builder.appendFrom(", whois.domain");
                builder.appendWhere(" AND nomdedomaine.NOM = whois.domain.NAME");
                whoisDomainAdded = true;
            }
            builder.appendFrom(", whois.object_contact_r ocr, whois.contact con, whois.nh nh2");
            builder.appendWhere(" AND ocr.CONTACT_ID = con.ID");
            builder.appendWhere(" AND ocr.CONTACT_TYPE='ADMIN'");
            builder.appendWhere(" AND ocr.object_id =  whois.domain.ID");
            builder.appendWhere(" AND nh2.object_id =  con.ID");
            builder.appendWhere(" AND nh2.PREFIX||TO_CHAR(nh2.NUM)||'-'||nh2.SUFFIX = ?");
            builder.addParameters(adminHandle);
        }

        if ((registrar != null) && (!"".equals(registrar))) {
            if (!whoisDomainAdded) {
                builder.appendFrom(", whois.domain");
                builder.appendWhere(" AND nomdedomaine.NOM = whois.domain.NAME");
                whoisDomainAdded = true;
            }
            builder.appendFrom(", nicope.adherent");
            builder.appendWhere(" AND whois.domain.REF_REGISTRAR = nicope.adherent.ID");
            builder.appendWhere(" AND (upper(nicope.adherent.NOM) = upper(?) OR upper(nicope.adherent.CODE) = upper(?))");
            builder.addParameters(registrar);
            builder.addParameters(registrar);
        }

        return builder;
    }

    @Override
    public List<OperationForm> searchOperationForms(final FormSearchCriteria informationToFind, UserId userId, TldServiceFacade tld) throws ServiceException {
        SQLSelectQueryBuilder builder = this.buildMainSearchOperationFormQuery(informationToFind);

        QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlConnectionFactory);
        PreparedStatement preparedStatement = queryStatementManagement.initializeStatement(builder.getQuery());
        try {
            QueryStatementManagement.initPreparedStatementParameters(preparedStatement, builder.getListParameters());
        } catch (SQLException e) {
            throw new ServiceException("searchOperationForms() failed query:" + builder.getQuery(), e);
        }
        List<OperationForm> operationForm = queryStatementManagement.executeStatementList(OperationForm.class, preparedStatement, userId, tld);

        return operationForm;
    }
}
