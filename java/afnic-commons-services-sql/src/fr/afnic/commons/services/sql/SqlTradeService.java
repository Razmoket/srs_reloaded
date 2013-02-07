/*
 * $Id: SqlTradeService.java,v 1.19 2010/06/21 14:25:35 ginguene Exp $ $Revision: 1.19 $ $Author: ginguene $
 */

package fr.afnic.commons.services.sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.google.common.base.Joiner;

import fr.afnic.commons.beans.documents.Document;
import fr.afnic.commons.beans.documents.GddDocument;
import fr.afnic.commons.beans.logs.ILogger;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.beans.request.TradeRequest;
import fr.afnic.commons.beans.request.TradeRequestStatus;
import fr.afnic.commons.beans.search.traderequest.TradeRequestSearchCriteria;
import fr.afnic.commons.services.ITradeService;
import fr.afnic.commons.services.exception.RequestFailedException;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.sql.utils.QueryStatementManagement;
import fr.afnic.commons.services.sql.utils.SQLSelectQueryBuilder;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.commons.utils.Preconditions;
import fr.afnic.utils.DateUtils;
import fr.afnic.utils.sql.ISqlConnectionFactory;
import fr.afnic.utils.sql.pool.PoolSqlConnectionFactoryFacade;
import fr.afnic.utils.sql.pool.SqlDatabaseEnum;

public class SqlTradeService implements ITradeService {

    private static final ILogger LOGGER = AppServiceFacade.getLoggerService().getLogger(SqlTradeService.class);

    //protected QueryStatementManagement queryStatementManagement = null;//this.queryStatementManagement.initializeStatement(sql);
    protected ISqlConnectionFactory sqlConnectionFactory = null;

    private static final String TRADE_REQUEST_SELECTION = " nicope.tradeRequest.id, "
                                                          + " nicope.tradeRequest.id_ticket,"
                                                          + " nicope.tradeRequest.status,"
                                                          + " nicope.tradeRequest.comments, "
                                                          + " nicope.tradeRequest.ticket_date_creation, "
                                                          + " nicope.tradeRequest.ndd, "
                                                          + " nicope.tradeRequest.last_status_update";

    /**
     * Constructeur avec un sqlConnectionFactory permettant d'obtenir une connection vers la base de donnees
     * 
     * @param sqlConnectionFactory
     */
    public SqlTradeService(final ISqlConnectionFactory sqlConnectionFactory) {
        this.sqlConnectionFactory = sqlConnectionFactory;
    }

    public SqlTradeService(SqlDatabaseEnum database, TldServiceFacade tld) throws ServiceException {
        this.sqlConnectionFactory = PoolSqlConnectionFactoryFacade.getSqlPoolConnectionFactory(database, tld);
    }

    /**
     * Retourne un Autorisation a partir d'un resulset qui a été obtenue un utilisant la methode getAuthorizationSelectionQuery() pour écrire la
     * partie select de la requete. La méthode next() du resultSet doit avoir été appelée avant de le passer comme parametre à cette méthode.
     * 
     * @param resultSet
     * @return
     * @throws SQLException
     */
    private TradeRequest getTradeRequestFromResultSet(final ResultSet resultSet, UserId userId, TldServiceFacade tld) throws SQLException {
        final TradeRequest result = new TradeRequest(userId, tld);

        int ind = 1;
        result.setId(resultSet.getInt(ind++));
        result.setTicketId(resultSet.getString(ind++));

        final String statusStr = resultSet.getString(ind++);
        result.setStatusFromString(statusStr);

        result.setComment(resultSet.getString(ind++));

        result.setCreateDate(resultSet.getTimestamp(ind++));
        result.setDomainName(resultSet.getString(ind++));

        final java.sql.Timestamp timeStamp = resultSet.getTimestamp(ind++);
        if (timeStamp != null) result.setLastStatusUpdate(new Date(timeStamp.getTime()));

        return result;
    }

    @Override
    public TradeRequest getTradeRequestWithId(final int id, UserId userId, TldServiceFacade tld) throws ServiceException {
        TradeRequest result = null;

        final String sql = " SELECT " + SqlTradeService.TRADE_REQUEST_SELECTION + " FROM " + " nicope.tradeRequest" + " WHERE "
                           + " nicope.tradeRequest.ID = ?";

        if (SqlTradeService.LOGGER.isTraceEnabled()) SqlTradeService.LOGGER.trace("Execute " + sql + " with id=" + id);

        QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlConnectionFactory);
        try {
            final PreparedStatement preparedStatement = queryStatementManagement.initializeStatement(sql);
            preparedStatement.setInt(1, id);

            final ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                result = this.getTradeRequestFromResultSet(resultSet, userId, tld);
            }

            resultSet.close();
            preparedStatement.close();
        } catch (final SQLException e) {
            throw new ServiceException("getTradeRequestWithId(" + id + ") failed", e);
        } finally {
            queryStatementManagement.closeConnection();
        }
        return result;
    }

    @Override
    public List<TradeRequest> getTradeRequests(final UserId userId, final TldServiceFacade tld) throws ServiceException {
        final String sql = " SELECT " + SqlTradeService.TRADE_REQUEST_SELECTION + " FROM " + " nicope.tradeRequest";

        if (SqlTradeService.LOGGER.isTraceEnabled()) SqlTradeService.LOGGER.trace("Execute " + sql);

        return this.getTradeRequestsFromSql(sql, userId, tld);
    }

    @Override
    public List<TradeRequest> getTradeRequestsToExpire(final UserId userId, final TldServiceFacade tld) throws ServiceException {
        final String sql = " SELECT " + SqlTradeService.TRADE_REQUEST_SELECTION + " FROM " + " nicope.tradeRequest" + "Where "
                           + " nicope.tradeRequest.status = '" + TradeRequestStatus.Waiting + "'" + " and nicope.tradeRequest.ticket_date_creation > ? ";

        if (SqlTradeService.LOGGER.isTraceEnabled()) SqlTradeService.LOGGER.trace("Execute " + sql);

        final GregorianCalendar calendar = new GregorianCalendar();
        calendar.add(GregorianCalendar.DATE, -15);
        final java.sql.Date fifteenDaysAgo = new java.sql.Date(calendar.getTimeInMillis());

        return this.getTradeRequestsFromSql(sql, userId, tld, fifteenDaysAgo);

    }

    @Override
    public List<TradeRequest> getTradeRequestsToDisplay(final UserId userId, final TldServiceFacade tld) throws ServiceException {
        final String sql = " SELECT " + SqlTradeService.TRADE_REQUEST_SELECTION + " FROM " + " nicope.tradeRequest" + " WHERE " + " ( " + " ( "
                           + " nicope.tradeRequest.status = '" + TradeRequestStatus.Aborded + "'" + " or nicope.tradeRequest.status = '"
                           + TradeRequestStatus.Finished + "'" + " )" + " and nicope.tradeRequest.last_status_update > ? " + " ) "
                           + " or  nicope.tradeRequest.status = '" + TradeRequestStatus.Running + "'" + " or  nicope.tradeRequest.status = '"
                           + TradeRequestStatus.Answered + "'" + " or  nicope.tradeRequest.status = '" + TradeRequestStatus.Problem + "'"
                           + " or  nicope.tradeRequest.status = '" + TradeRequestStatus.Waiting + "'";

        final java.sql.Date date = new java.sql.Date(DateUtils.getToday().getTime());

        if (SqlTradeService.LOGGER.isTraceEnabled()) SqlTradeService.LOGGER.trace("Execute " + sql + " with date=" + date);

        return this.getTradeRequestsFromSql(sql, userId, tld, date);
    }

    @Override
    public List<TradeRequest> getPendingTradeRequests(UserId userId, TldServiceFacade tld) throws ServiceException {
        final String sql = " SELECT " + SqlTradeService.TRADE_REQUEST_SELECTION + " FROM " + " nicope.tradeRequest" + " WHERE "
                           + " nicope.tradeRequest.status NOT IN ('" + TradeRequestStatus.Aborded + "'" + ", '" + TradeRequestStatus.Finished + "'" + ", '"
                           + TradeRequestStatus.Suppressed + "'" + ")";

        if (SqlTradeService.LOGGER.isTraceEnabled()) SqlTradeService.LOGGER.trace("Execute " + sql);
        return this.getTradeRequestsFromSql(sql, userId, tld);

    }

    private List<TradeRequest> getTradeRequestsFromSql(final String sql, final UserId userId, final TldServiceFacade tld, final Object... params) throws ServiceException {
        QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlConnectionFactory);

        try {
            final ResultSetProcessor<List<TradeRequest>> processor = new ResultSetProcessor<List<TradeRequest>>() {

                @Override
                public void populateResultFromResultSet(final ResultSet resultSet) throws SQLException, ServiceException {
                    this.result = new ArrayList<TradeRequest>();
                    while (resultSet.next()) {
                        final TradeRequest tradeRequest = SqlTradeService.this.getTradeRequestFromResultSet(resultSet, userId, tld);
                        if (tradeRequest != null) {
                            this.result.add(tradeRequest);
                        }
                    }
                }
            };

            queryStatementManagement.executeQuery(processor, sql, params);
            return processor.getResult();
        } catch (final Exception e) {
            throw new ServiceException("getTradeRequestsFromSql(" + sql + ") failed", e);
        }
    }

    @Override
    public List<TradeRequest> getTradeRequestsWithDomain(final String domain, final UserId userId, final TldServiceFacade tld) throws ServiceException {

        QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlConnectionFactory);
        final String sql = " SELECT " + SqlTradeService.TRADE_REQUEST_SELECTION + " FROM " + " nicope.tradeRequest" + " WHERE "
                           + " nicope.tradeRequest.NDD=?";

        if (SqlTradeService.LOGGER.isTraceEnabled()) SqlTradeService.LOGGER.trace("Execute " + sql + " with domain=" + domain);

        final ResultSetProcessor<List<TradeRequest>> processor = new ResultSetProcessor<List<TradeRequest>>() {
            @Override
            public void populateResultFromResultSet(final ResultSet resultSet) throws SQLException, ServiceException {
                this.result = new ArrayList<TradeRequest>();
                while (resultSet.next()) {
                    this.result.add(SqlTradeService.this.getTradeRequestFromResultSet(resultSet, userId, tld));
                }
            }
        };

        try {
            queryStatementManagement.executeQuery(processor, sql, domain);
        } catch (final SQLException e) {
            throw new RequestFailedException("Error in getTradeRequestsWithDomain(" + domain + ")", e);
        }
        return processor.getResult();

    }

    @Override
    public TradeRequest getRequestToLinkWithDocument(final GddDocument document, final UserId userId, final TldServiceFacade tld) throws ServiceException {

        Preconditions.checkNotNull(document, "document");

        QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlConnectionFactory);

        final String sql = " SELECT " + SqlTradeService.TRADE_REQUEST_SELECTION + " FROM " + " nicope.tradeRequest" + " WHERE "
                           + " nicope.tradeRequest.NDD=?" + " AND nicope.tradeRequest.status NOT IN (" + "'" + TradeRequestStatus.Finished + "'," + "'"
                           + TradeRequestStatus.Aborded + "'," + "'" + TradeRequestStatus.Suppressed + "'" + ")";

        if (SqlTradeService.LOGGER.isDebugEnabled()) SqlTradeService.LOGGER.debug("Execute " + sql + " with document=" + document.getHandle());

        final ResultSetProcessor<TradeRequest> processor = new ResultSetProcessor<TradeRequest>() {
            @Override
            public void populateResultFromResultSet(final ResultSet resultSet) throws SQLException, ServiceException {
                if (resultSet.next()) {
                    final TradeRequest tradeRequest = SqlTradeService.this.getTradeRequestFromResultSet(resultSet, userId, tld);

                    if (SqlTradeService.this.isTradeRequestCorrespondingToDocument(tradeRequest, document)) {
                        this.result = tradeRequest;
                    }
                }
            }
        };

        try {
            queryStatementManagement.executeQuery(processor, sql, document.getDomain());
        } catch (final SQLException e) {
            throw new RequestFailedException("getRequestToLinkWithDocument(" + document.getHandle() + ") failed", e);
        }
        return processor.getResult();
    }

    /**
     * Indique si un document Gdd correspond à une requete de Trade.<br/>
     * On regarde si la requete a déja un document rattaché avec le même nom de domaine
     * 
     * @param tradeRequest
     *            Requete de trade à laquele on veut savoir si le document correspond
     * @param gdddocument
     *            Document que l'on souhaite tester
     * @return vrai si le document correspond à la requete
     */
    private boolean isTradeRequestCorrespondingToDocument(final TradeRequest tradeRequest, final GddDocument gdddocument) {

        for (final Document document : tradeRequest.getDocuments()) {
            if (document instanceof GddDocument) {
                final GddDocument alreadyLinkedDocument = (GddDocument) document;
                return alreadyLinkedDocument.getDomain().equals(gdddocument.getDomain());
            }
        }
        return false;
    }

    /**
     * Retourne l'id que devra avoir la prochaine requete de trade.<br/>
     * l'id est géré par uen sequence oracle appelée SEQ_TRADEREQUES.
     * 
     * @return l'id à utiliser pour créer la prochaine requete de trade
     * @throws ServiceException
     */
    private int getNextTradeRequestId() throws ServiceException {
        QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlConnectionFactory);
        try {
            final String sql = "SELECT SEQ_TRADEREQUEST.nextval from dual";
            if (SqlTradeService.LOGGER.isTraceEnabled()) SqlTradeService.LOGGER.trace("Execute " + sql);

            final PreparedStatement preparedStatement = queryStatementManagement.initializeStatement(sql);
            final ResultSet resultSet = preparedStatement.executeQuery();
            int nextId = -1;
            if (resultSet.next()) {
                nextId = resultSet.getInt(1);
            }

            resultSet.close();
            preparedStatement.close();
            return nextId;
        } catch (final SQLException e) {
            throw new ServiceException("getNextTradeRequestId() failed", e);
        } finally {
            queryStatementManagement.closeConnection();
        }
    }

    @Override
    public int createTradeRequest(final TradeRequest tradeRequest, UserId userId, TldServiceFacade tld) throws ServiceException {
        if (tradeRequest == null) throw new RequestFailedException("tradeRequest cannot be null");
        if (tradeRequest.getStatus() == null) throw new RequestFailedException("tradeRequest.status cannot be null");
        if (tradeRequest.getTicketId() == null) throw new RequestFailedException("tradeRequest.ticketId cannot be null");
        QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlConnectionFactory);

        try {

            final int nextId = this.getNextTradeRequestId();

            String comment = "";
            if (tradeRequest.getComment() != null) {
                comment = tradeRequest.getComment().replaceAll("'", "\\'");
            }

            final String sql = "insert into nicope.tradeRequest (" + " nicope.tradeRequest.ID" + " ,nicope.tradeRequest.ID_TICKET"
                               + " ,nicope.tradeRequest.STATUS" + " ,nicope.tradeRequest.COMMENTS" + " ,nicope.tradeRequest.TICKET_DATE_CREATION"
                               + " ,nicope.tradeRequest.NDD" + ")" + " values (?,?,?,?,?,?)";

            if (SqlTradeService.LOGGER.isTraceEnabled()) SqlTradeService.LOGGER.trace("Execute " + sql + "with id " + nextId);

            java.sql.Timestamp sqlCreateDate = null;
            if (tradeRequest.getTradeTicket() != null && tradeRequest.getTradeTicket().getCreateDate() != null) {
                sqlCreateDate = new java.sql.Timestamp(tradeRequest.getTradeTicket().getCreateDate().getTime());
            }

            final PreparedStatement preparedStatement = queryStatementManagement.initializeStatement(sql);
            preparedStatement.setInt(1, nextId);
            preparedStatement.setString(2, tradeRequest.getTicketId());
            preparedStatement.setString(3, tradeRequest.getStatus().toString());
            preparedStatement.setString(4, comment);
            preparedStatement.setTimestamp(5, sqlCreateDate);
            preparedStatement.setString(6, tradeRequest.getDomainName());
            preparedStatement.executeUpdate();
            preparedStatement.close();

            tradeRequest.setId(nextId);
        } catch (final SQLException e) {
            throw new ServiceException("insertTradeRequest() for domain " + tradeRequest.getDomainName() + " failed", e);
        } finally {
            queryStatementManagement.closeConnection();
        }
        return tradeRequest.getId();
    }

    @Override
    public void updateTradeRequest(final TradeRequest tradeRequest, final UserId userId, TldServiceFacade tld) throws ServiceException {

        QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlConnectionFactory);
        try {

            // on créé un historique avant l'update avec le login nicpers
            AppServiceFacade.getRequestService().history(tradeRequest, userId, tld);

            final String sql = "UPDATE nicope.tradeRequest " + " SET " + " nicope.tradeRequest.STATUS=?" + " ,nicope.tradeRequest.COMMENTS=? "
                               + " ,nicope.tradeRequest.TICKET_DATE_CREATION=? " + " ,nicope.tradeRequest.NDD=? "
                               + " ,nicope.tradeRequest.Last_status_update=SYSDATE" + " WHERE " + " nicope.tradeRequest.id=?";

            if (SqlTradeService.LOGGER.isTraceEnabled()) SqlTradeService.LOGGER.trace("Execute " + sql);

            final PreparedStatement preparedStatement = queryStatementManagement.initializeStatement(sql);
            preparedStatement.setString(1, tradeRequest.getStatus().toString());
            preparedStatement.setString(2, tradeRequest.getComment());

            // Update de la date de creation utile uniquement pour remettre les données d'aplomb
            // suite à l'ajout du champs TICKET_DATE_CREATION
            // La maj des champs NDD et TICKET_DATE_CREATION doit disparaitre dans les prochaines version
            // car à ce moment là les données auront été remis en ordre (ajouté en version 1.3.0)
            Timestamp sqlCreateDate = null;
            if (tradeRequest.getTradeTicket() != null && tradeRequest.getTradeTicket().getCreateDate() != null) {
                sqlCreateDate = new Timestamp(tradeRequest.getTradeTicket().getCreateDate().getTime());

            }

            preparedStatement.setTimestamp(3, sqlCreateDate);
            preparedStatement.setString(4, tradeRequest.getDomainName());
            preparedStatement.setInt(5, tradeRequest.getId());
            preparedStatement.executeUpdate();
            preparedStatement.close();

            if (tradeRequest.hasFinalStatus()) {
                tradeRequest.archiveDocuments();
            }
        } catch (final SQLException e) {
            throw new ServiceException("updateTradeRequest(" + tradeRequest.getId() + ", " + userId + ") failed", e);
        } finally {
            queryStatementManagement.closeConnection();
        }
    }

    @Override
    public List<TradeRequest> searchTradeRequest(TradeRequestSearchCriteria criteria, final UserId userId, final TldServiceFacade tld) throws ServiceException {
        SQLSelectQueryBuilder builder = this.buildSearchTradeRequestQuery(criteria);

        final ResultSetProcessor<List<TradeRequest>> processor = new ResultSetProcessor<List<TradeRequest>>() {
            @Override
            public void populateResultFromResultSet(final ResultSet resultSet) throws SQLException, ServiceException {
                this.result = new ArrayList<TradeRequest>();
                while (resultSet.next()) {
                    this.result.add(SqlTradeService.this.getTradeRequestFromResultSet(resultSet, userId, tld));
                }
            }
        };

        QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlConnectionFactory);
        try {
            queryStatementManagement.executeQuery(processor, builder.getQuery(), builder.getListParameters());
        } catch (SQLException e) {
            queryStatementManagement.closeConnection();
        }

        return processor.getResult();
    }

    private SQLSelectQueryBuilder buildSearchTradeRequestQuery(final TradeRequestSearchCriteria criteria) {
        SQLSelectQueryBuilder builder = new SQLSelectQueryBuilder();

        String ldhDomainName = criteria.getLdhDomainName();
        List<TradeRequestStatus> excludedStatus = criteria.getExcludedStatus();
        List<TradeRequestStatus> includedStatus = criteria.getIncludedStatus();

        builder.appendSelect(TRADE_REQUEST_SELECTION);
        builder.appendFrom("nicope.traderequest");

        List<String> where = new ArrayList<String>();

        if (!StringUtils.isEmpty(ldhDomainName)) {
            where.add(" nicope.tradeRequest.ndd = ?");
            builder.addParameters(ldhDomainName);
        }

        if (excludedStatus != null && !excludedStatus.isEmpty()) {
            where.add(" nicope.tradeRequest.status not in ( '" + Joiner.on("', '").join(excludedStatus) + "')");
        }

        if (includedStatus != null && !includedStatus.isEmpty()) {
            where.add(" nicope.tradeRequest.status in ( '" + Joiner.on("', '").join(includedStatus) + "')");
        }

        if (!where.isEmpty()) {
            builder.appendWhere(Joiner.on(" and ").join(where));
        }

        return builder;
    }

}
