/*
 * $Id: SqlRequestDao.java,v 1.25 2010/09/02 09:51:33 ginguene Exp $
 * $Revision: 1.25 $
 * $Author: ginguene $
 */

package fr.afnic.commons.services.sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import fr.afnic.commons.beans.documents.Document;
import fr.afnic.commons.beans.logs.ILogger;
import fr.afnic.commons.beans.profiling.users.User;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.beans.request.AuthorizationRequest;
import fr.afnic.commons.beans.request.AuthorizationRequestStatus;
import fr.afnic.commons.beans.request.Request;
import fr.afnic.commons.beans.request.RequestHistoryEvent;
import fr.afnic.commons.beans.request.RequestHistoryEventField;
import fr.afnic.commons.beans.request.RequestStatus;
import fr.afnic.commons.beans.request.TradeRequest;
import fr.afnic.commons.beans.request.TradeRequestStatus;
import fr.afnic.commons.services.IRequestService;
import fr.afnic.commons.services.exception.RequestFailedException;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.sql.utils.QueryStatementManagement;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.commons.utils.Preconditions;
import fr.afnic.utils.DateUtils;
import fr.afnic.utils.sql.ISqlConnectionFactory;
import fr.afnic.utils.sql.pool.PoolSqlConnectionFactoryFacade;
import fr.afnic.utils.sql.pool.SqlDatabaseEnum;

public class SqlRequestService implements IRequestService {

    private static final ILogger LOGGER = AppServiceFacade.getLoggerService().getLogger(SqlRequestService.class);
    private ISqlConnectionFactory sqlConnectionFactory = null;

    private static final String REQUEST_HISTORY_SELECTION = " nicope.request_History.request_id,"
                                                            + " nicope.request_History.request_type,"
                                                            + " nicope.request_History.old_value,"
                                                            + " nicope.request_History.new_value,"
                                                            + " nicope.request_History.update_Date ,"
                                                            + " nicope.request_History.comments,"
                                                            + " nicope.request_History.user_name,"
                                                            + " nicope.request_History.field, "
                                                            + " nicope.request_history.id";

    /**
     * Constructeur avec un sqlConnectionFactory permettant d'obtenir une connection vers la base de donnees
     * 
     * @param sqlConnectionFactory
     */
    public SqlRequestService(ISqlConnectionFactory sqlConnectionFactory) {
        this.sqlConnectionFactory = sqlConnectionFactory;
    }

    public SqlRequestService(SqlDatabaseEnum database, TldServiceFacade tld) throws ServiceException {
        this.sqlConnectionFactory = PoolSqlConnectionFactoryFacade.getSqlPoolConnectionFactory(database, tld);
    }

    @Override
    public boolean addHistoryEvent(RequestHistoryEvent event, UserId userId, TldServiceFacade tld) throws ServiceException {
        if (event == null) {
            throw new IllegalArgumentException("event cannot be null");
        }

        if (event.getField() == null) {
            throw new IllegalArgumentException("event.field cannot be null");
        }

        if (event.getUser() == null) {
            throw new IllegalArgumentException("event.user cannot be null");
        }

        if (event.getDate() == null) {
            throw new IllegalArgumentException("event.date cannot be null");
        }

        boolean ret = true;

        QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlConnectionFactory);
        ;
        try {

            String sql = "insert into nicope.request_History ("
                         + SqlRequestService.REQUEST_HISTORY_SELECTION
                         + " ) values (?,?,?,?,?,?,?,?,?)";

            if (SqlRequestService.LOGGER.isTraceEnabled())
                SqlRequestService.LOGGER.trace("Execute " + sql + " with event: " + event.toString());

            java.sql.Timestamp sqlDate = null;
            if (event.getDate() != null) {
                sqlDate = new java.sql.Timestamp(event.getDate().getTime());
            }

            String comment = event.getComment();
            if (comment != null) {
                comment = comment.replaceAll("'", "\\'");
            }

            int nextRequestHistoryId = this.getNextRequestHistoryId();

            PreparedStatement preparedStatement = queryStatementManagement.initializeStatement(sql);
            preparedStatement.setInt(1, event.getRequestId());
            preparedStatement.setString(2, event.getRequestType());
            preparedStatement.setString(3, event.getOldValue());
            preparedStatement.setString(4, event.getNewValue());
            preparedStatement.setTimestamp(5, sqlDate);
            preparedStatement.setString(6, comment);
            preparedStatement.setString(7, event.getUser());
            preparedStatement.setString(8, event.getField().getName());
            preparedStatement.setInt(9, nextRequestHistoryId);
            preparedStatement.executeUpdate();
            preparedStatement.close();

        } catch (SQLException e) {
            throw new ServiceException("addHistoryEvent() failed", e);
        } finally {
            queryStatementManagement.closeConnection();
        }
        return ret;
    }

    private int getNextRequestHistoryId() throws ServiceException {
        QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlConnectionFactory);
        ;

        try {
            String sql = "SELECT SEQ_REQUEST_HISTORY.nextval from dual";
            if (SqlRequestService.LOGGER.isTraceEnabled())
                SqlRequestService.LOGGER.trace("Execute " + sql);

            PreparedStatement preparedStatement = queryStatementManagement.initializeStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            int nextId = -1;
            if (resultSet.next()) {
                nextId = resultSet.getInt(1);
            }
            resultSet.close();
            preparedStatement.close();
            return nextId;
        } catch (SQLException e) {
            throw new ServiceException("getNextTradeRequestId() failed", e);
        } finally {
            queryStatementManagement.closeConnection();
        }
    }

    @Override
    public void history(Request request, UserId userId, TldServiceFacade tld) throws ServiceException {

        Date now = new Date();

        User user = userId.getObjectOwner(userId, tld);

        // On récupère l'objet dans les bases avant l'updates
        Request savedRequest = this.getRequestById(request.getClass().getSimpleName(), request.getId(), userId, tld);

        if (savedRequest == null) {
            throw new ServiceException("Cannot history request " + request.getId() + ": Request not found in database");
        }

        boolean hasBeenHistoried = false;

        if (request.getComment() != null && !request.getComment().equals(savedRequest.getComment())) {
            // le commentaire a été changé
            RequestHistoryEvent event = new RequestHistoryEvent();
            event.setUser(user.getNicpersId());
            event.setDate(now);
            event.setRequestId(request.getId());
            event.setField(RequestHistoryEventField.Comment);
            event.setOldValue(savedRequest.getComment());
            event.setNewValue(request.getComment());
            event.setRequestType(request.getClass().getSimpleName());
            AppServiceFacade.getRequestService().addHistoryEvent(event, userId, tld);
            hasBeenHistoried = true;
        }

        if (request.getStatus() != null && request.getStatus() != savedRequest.getStatus()) {
            // le statut a été changé mais n'a pas été mis à null
            RequestHistoryEvent event = new RequestHistoryEvent();
            event.setUser(user.getNicpersId());
            event.setDate(now);
            event.setRequestId(request.getId());
            event.setField(RequestHistoryEventField.Status);
            event.setOldValue(savedRequest.getStatus().toString());
            event.setNewValue(request.getStatus().toString());
            event.setRequestType(request.getClass().getSimpleName());
            AppServiceFacade.getRequestService().addHistoryEvent(event, userId, tld);
            hasBeenHistoried = true;
        }

        if (hasBeenHistoried)
            SqlRequestService.LOGGER.debug("Request " + request.getClass().getSimpleName() + " " + request.getId() + " by " + user + " historied");

    }

    private String getRequestTableName(Request request) throws ServiceException {
        if (request instanceof AuthorizationRequest)
            return "nicope.autorisationRequest";
        if (request instanceof TradeRequest)
            return "nicope.tradeRequest";
        throw new ServiceException("no table for request " + request.toString());
    }

    @Override
    public List<RequestHistoryEvent> getHistory(String type, int requestId, UserId userId, TldServiceFacade tld) throws ServiceException {

        List<RequestHistoryEvent> result = new ArrayList<RequestHistoryEvent>();

        final String sql = "SELECT "
                           + SqlRequestService.REQUEST_HISTORY_SELECTION
                           + " FROM "
                           + " nicope.request_History"
                           + " WHERE "
                           + " nicope.request_History.request_id = ? "
                           + " AND nicope.request_History.request_type = ? "
                           + " order by nicope.request_History.id desc";

        if (SqlRequestService.LOGGER.isTraceEnabled())
            SqlRequestService.LOGGER.trace("Execute " + sql
                                           + " with requestId=" + requestId
                                           + " and type=" + type);

        QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlConnectionFactory);
        ;

        try {
            PreparedStatement preparedStatement = queryStatementManagement.initializeStatement(sql);
            preparedStatement.setInt(1, requestId);
            preparedStatement.setString(2, type);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                RequestHistoryEvent event = this.getRequestHistoryEventFromResultSet(resultSet);
                result.add(event);
            }
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            throw new ServiceException("getHistory(" + type + ", " + requestId + ") failed", e);
        } finally {
            queryStatementManagement.closeConnection();
        }

        return result;
    }

    @Override
    public List<RequestHistoryEvent> getTodayHistoryWithUser(String userLogin, UserId userId, TldServiceFacade tld) throws ServiceException {

        List<RequestHistoryEvent> result = new ArrayList<RequestHistoryEvent>();

        final String sql = "SELECT "
                           + SqlRequestService.REQUEST_HISTORY_SELECTION
                           + " FROM "
                           + " nicope.request_History"
                           + " WHERE "
                           + " nicope.request_History.user_name = ? "
                           + " AND nicope.request_History.update_Date > ? "
                           + " order by nicope.request_History.id desc";

        if (SqlRequestService.LOGGER.isTraceEnabled())
            SqlRequestService.LOGGER.trace("Execute " + sql
                                           + " with requestId=" + userLogin);

        QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlConnectionFactory);
        ;

        try {
            PreparedStatement preparedStatement = queryStatementManagement.initializeStatement(sql);
            preparedStatement.setString(1, userLogin);
            preparedStatement.setDate(2, new java.sql.Date(DateUtils.getToday().getTime()));
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                RequestHistoryEvent event = this.getRequestHistoryEventFromResultSet(resultSet);
                result.add(event);
            }
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            throw new ServiceException("getTodayHistoryWithUser(" + userLogin + ") failed", e);
        } finally {
            queryStatementManagement.closeConnection();
        }

        return result;
    }

    @Override
    @Deprecated
    public void updateHistory(RequestHistoryEvent event, UserId userId, TldServiceFacade tld) throws ServiceException {

        final String sql = "update nicope.request_History "
                           + "set nicope.request_History.update_Date = ?"
                           + " WHERE "
                           + " nicope.request_History.id = ? ";

        if (SqlRequestService.LOGGER.isTraceEnabled())
            SqlRequestService.LOGGER.trace("Execute " + sql
                                           + " with newDate=" + event.getDate()
                                           + " and id=" + event.getId());

        QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlConnectionFactory);
        ;

        try {
            PreparedStatement preparedStatement = queryStatementManagement.initializeStatement(sql);
            preparedStatement.setTimestamp(1, new Timestamp(event.getDate().getTime()));
            preparedStatement.setInt(2, event.getId());
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            throw new ServiceException("updateHistory(" + event.getDate() + ", " + event.getId() + ") failed", e);
        } finally {
            queryStatementManagement.closeConnection();
        }

    }

    @Override
    public Request getRequestById(String type, int id, UserId userId, TldServiceFacade tld) throws ServiceException {

        if (TradeRequest.class.getSimpleName().equals(type)) {
            return AppServiceFacade.getTradeService()
                                   .getTradeRequestWithId(id, userId, tld);
        }

        if (AuthorizationRequest.class.getSimpleName().equals(type)) {
            return AppServiceFacade.getAuthorizationRequestService()
                                   .getAuthorizationRequestWithId(id, userId, tld);
        }
        return null;
    }

    @Override
    public void linkDocumentToRequest(Document document, Request request, UserId userId, TldServiceFacade tld) throws ServiceException {
        if (request == null) {
            throw new IllegalArgumentException("Argument request is null");
        }

        if (document == null) {
            throw new IllegalArgumentException("Argument document is null");
        }

        if (document.getHandle() == null) {
            throw new IllegalArgumentException("Argument document has null handle");
        }

        this.changeTitleInToHumanReadableTitle(document, request, userId, tld);
        this.linkNotLinkedDocumentToRequestInDatabase(request, document.getHandle());
    }

    private void changeTitleInToHumanReadableTitle(Document document, Request request, UserId userId, TldServiceFacade tld) throws ServiceException {
        // on renomme la doc avec le nom
        String header = "[" + request.getClass().getSimpleName() + " " + request.getId() + "]";

        String title = document.getTitle();
        if (title == null || !title.contains(header)) {
            String newTitle = header + " " + title;
            AppServiceFacade.getDocumentService().updateTitle(document.getHandle(), newTitle, userId, tld);
            document.setTitle(newTitle);
        }
    }

    private void linkNotLinkedDocumentToRequestInDatabase(Request request, String documentHandle) throws ServiceException {
        this.checkDocumentNotAlreadyLinkedToARequest(request, documentHandle);
        this.linkDocumentToRequestInDatabase(request, documentHandle);
    }

    private void linkDocumentToRequestInDatabase(Request request, String documentHandle) throws ServiceException {
        Preconditions.checkNotNull(request, "request");
        Preconditions.checkNotZero(request.getId(), "request.id");
        Preconditions.checkNotEmpty(documentHandle, "documentHandle");

        QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlConnectionFactory);

        try {

            final String sql = "insert into nicope.Request_document_R ("
                               + " nicope.Request_document_R.REQUEST_ID"
                               + " ,nicope.Request_document_R.REQUEST_TYPE"
                               + " ,nicope.Request_document_R.DOCUMENT_ID"
                               + ")" + " values (?,?,?)";

            SqlRequestService.LOGGER.debug("Excecute " + sql + " with " + request.getId() + ", " + documentHandle);

            queryStatementManagement.executeUpdate(sql, request.getId(), request.getClass().getSimpleName(), documentHandle);
        } catch (SQLException e) {
            throw new RequestFailedException("linkDocumentInDatabase(" + request.getId() + ", " + documentHandle + ") failed", e);
        }
    }

    private void checkDocumentNotAlreadyLinkedToARequest(Request request, String documentHandle) throws ServiceException {
        QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlConnectionFactory);

        try {

            final String sql = "select * from nicope.Request_document_R "
                               + " where nicope.Request_document_R.REQUEST_TYPE = ? "
                               + " and nicope.Request_document_R.REQUEST_ID=? "
                               + " and nicope.Request_document_R.DOCUMENT_ID=? ";

            SqlRequestService.LOGGER.debug("Excecute " + sql + " with " + request.getId() + ", " + documentHandle);

            ResultSetProcessor<Boolean> processor = new ResultSetProcessor<Boolean>() {
                @Override
                public void populateResultFromResultSet(ResultSet resultSet) throws SQLException, ServiceException {
                    this.result =
                                  Boolean.valueOf(resultSet.next());
                }
            };

            queryStatementManagement.executeQuery(processor, sql, request.getClass().getSimpleName(), request.getId(), documentHandle);
            if (processor.getResult().booleanValue()) {
                throw new ServiceException("Document " + documentHandle + " already linked to request " + request.getClass().getSimpleName() + " " + request.getId());
            }

        } catch (SQLException e) {
            throw new RequestFailedException("checkDocumentNotAlreadyLinkedToARequest(" + request.getId() + ", " +
                                             documentHandle + ") failed", e);
        }
    }

    @Override
    public Request getLinkedRequestToDocumentWithHandle(String handle, UserId userId, TldServiceFacade tld) throws ServiceException {
        if (handle == null) {
            throw new IllegalArgumentException("handle cannot be null");
        }

        Request result = null;

        String sql = " SELECT "
                     + " nicope.Request_document_R.REQUEST_ID"
                     + " ,nicope.Request_document_R.REQUEST_TYPE"
                     + " FROM "
                     + " nicope.Request_document_R"
                     + " WHERE "
                     + " nicope.Request_document_R.DOCUMENT_ID = ?";

        if (SqlRequestService.LOGGER.isTraceEnabled())
            SqlRequestService.LOGGER.trace("Execute " + sql + " with handle=" + handle);

        QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlConnectionFactory);
        ;

        try {
            PreparedStatement preparedStatement = queryStatementManagement.initializeStatement(sql);
            preparedStatement.setString(1, handle);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int idRequest = resultSet.getInt(1);
                String typeRequest = resultSet.getString(2);
                result = this.getRequestById(typeRequest, idRequest, userId, tld);
            }
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            throw new ServiceException("getRequestLinkedToDocumentWithHandle(" + handle + ") failed", e);
        } finally {
            queryStatementManagement.closeConnection();
        }
        return result;
    }

    @Override
    public void changeDateCreation(Request request, Date newDate, UserId userId, TldServiceFacade tld) throws ServiceException {
        String table = this.getRequestTableName(request);

        String sql = " update  " + table
                     + " set " + table + ".DATE_CREATION= ?"
                     + " WHERE " + table + ".id = ?";

        if (SqlRequestService.LOGGER.isTraceEnabled())
            SqlRequestService.LOGGER.trace("Execute " + sql + " whith id = " + request.getId() + " and newDate=" + newDate.toString());

        QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlConnectionFactory);
        ;

        try {
            PreparedStatement preparedStatement = queryStatementManagement.initializeStatement(sql);
            preparedStatement.setTimestamp(1, new java.sql.Timestamp(newDate.getTime()));
            preparedStatement.setInt(2, request.getId());
            preparedStatement.executeUpdate();

            preparedStatement.close();
        } catch (SQLException e) {
            throw new ServiceException("changeDateCreation(" + request.getId() + ", " + newDate.toString() + ") failed", e);
        } finally {
            queryStatementManagement.closeConnection();
        }

    }

    @Override
    public void changeLastStatusChange(Request request, Date newDate, UserId userId, TldServiceFacade tld) throws ServiceException {
        String table = this.getRequestTableName(request);

        String sql = " update  " + table
                     + " set " + table + ".last_status_update= ?"
                     + " WHERE " + table + ".id = ?";

        if (SqlRequestService.LOGGER.isTraceEnabled())
            SqlRequestService.LOGGER.trace("Execute " + sql + " whith id = " + request.getId() + " and newDate=" + newDate.toString());

        QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlConnectionFactory);
        ;

        try {
            PreparedStatement preparedStatement = queryStatementManagement.initializeStatement(sql);
            preparedStatement.setTimestamp(1, new java.sql.Timestamp(newDate.getTime()));
            preparedStatement.setInt(2, request.getId());
            preparedStatement.executeUpdate();

            preparedStatement.close();
        } catch (SQLException e) {
            throw new ServiceException("changeLastStatusChange(" + request.getId() + ", " + newDate.toString() + ") failed", e);
        } finally {
            queryStatementManagement.closeConnection();
        }

    }

    @Override
    public String getRequestComment(Request request, UserId userId, TldServiceFacade tld) throws ServiceException {

        String comment = null;
        String table = this.getRequestTableName(request);

        String sql = " SELECT "
                     + table + ".comments"
                     + " FROM "
                     + table
                     + " WHERE "
                     + table + ".id=?";

        if (SqlRequestService.LOGGER.isTraceEnabled())
            SqlRequestService.LOGGER.trace("Execute " + sql + " with id=" + request.getId());

        QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlConnectionFactory);
        ;

        try {
            PreparedStatement preparedStatement = queryStatementManagement.initializeStatement(sql);
            preparedStatement.setInt(1, request.getId());

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                comment = resultSet.getString(1);
            }
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            throw new ServiceException("getRequestComment(" + request.getId() + ") failed", e);
        } finally {
            queryStatementManagement.closeConnection();
        }
        return comment;
    }

    @Override
    public Date getFirstTimeStatusForRequest(RequestStatus status, Request request, UserId userId, TldServiceFacade tld) throws ServiceException {
        return this.getTimeStatusForRequest(status, request, "asc");
    }

    @Override
    public Date getLastTimeStatusForRequest(RequestStatus status, Request request, UserId userId, TldServiceFacade tld) throws ServiceException {
        return this.getTimeStatusForRequest(status, request, "desc");
    }

    private Date getTimeStatusForRequest(RequestStatus status, Request request, String order) throws ServiceException {

        final String sql = "SELECT "
                           + " nicope.request_History.update_Date "
                           + " FROM "
                           + " nicope.request_History"
                           + " WHERE "
                           + " nicope.request_History.request_id = ? "
                           + " AND nicope.request_History.request_type = ? "
                           + " AND instr(nicope.request_History.new_value, ?) > 0"
                           + " AND nicope.request_History.field = ? "
                           + " order by nicope.request_History.id " + order;

        if (SqlRequestService.LOGGER.isTraceEnabled())
            SqlRequestService.LOGGER.trace("Execute " + sql
                                           + " with requestId=" + request.getId()
                                           + " and type=" + request.getClass().getSimpleName() + " and status= " + status.getDictionaryKey());

        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        Date date = null;

        QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlConnectionFactory);
        ;

        try {
            preparedStatement = queryStatementManagement.initializeStatement(sql);
            preparedStatement.setInt(1, request.getId());
            preparedStatement.setString(2, request.getClass().getSimpleName());
            preparedStatement.setString(3, status.getDictionaryKey());
            preparedStatement.setString(4, RequestHistoryEventField.Status.toString());
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                date = new Date(resultSet.getTimestamp(1).getTime());
            }
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            throw new ServiceException("getFirstTimeStatus() failed", e);
        } finally {

            queryStatementManagement.closeConnection();

        }

        return date;
    }

    /**
     * Retourne un evenement d'historique d'une requete<br/>
     * à partir d'un resultset.
     * 
     * @param resultSet
     * @return
     * @throws DaoException
     * @throws SQLException
     */
    private RequestHistoryEvent getRequestHistoryEventFromResultSet(ResultSet resultSet) throws ServiceException, SQLException {
        RequestHistoryEvent event = new RequestHistoryEvent();
        int i = 1;

        event.setRequestId(resultSet.getInt(i++));
        event.setRequestType(resultSet.getString(i++));
        event.setOldValue(resultSet.getString(i++));
        event.setNewValue(resultSet.getString(i++));
        event.setDate(new Date(resultSet.getTimestamp(i++).getTime()));
        event.setComment(resultSet.getString(i++));
        event.setUser(resultSet.getString(i++));

        String fieldStr = resultSet.getString(i++);
        try {
            event.setField(RequestHistoryEventField.valueOf(fieldStr));
        } catch (Exception e) {
            throw new ServiceException("Invalid history event field:" + fieldStr);
        }
        event.setId(resultSet.getInt(i++));

        return event;
    }

    @Override
    public void cancelRequest(Request request, UserId userId, TldServiceFacade tld) throws ServiceException {
        if (request instanceof AuthorizationRequest) {
            this.cancelAuthorizationRequest((AuthorizationRequest) request, userId, tld);
        }

        if (request instanceof TradeRequest) {
            this.cancelTradeRequest((TradeRequest) request, userId, tld);
        }

    }

    /**
     * Annule une requete d'autorisation.
     * 
     * @param request
     *            Requete à annuler.
     * @param login
     *            Login du user à l'origine de l'action.
     * @throws DaoException
     *             Si l'opération échoue.
     * 
     */
    private void cancelAuthorizationRequest(AuthorizationRequest request, UserId userId, TldServiceFacade tld) throws ServiceException {
        try {
            request.setStatus(AuthorizationRequestStatus.Suppressed);
            AppServiceFacade.getAuthorizationRequestService().updateAuthorizationRequest(request, userId, tld);
        } catch (Exception e) {
            throw new ServiceException("cancelAuthorizationRequest(" + request.getId() + "," + userId + ") failed", e);
        }
    }

    /**
     * Annule une requete de trade.
     * 
     * @param request
     *            Requete à annuler.
     * @param login
     *            Login du user à l'origine de l'action.
     * @throws DaoException
     *             Si l'opération échoue.
     * 
     */
    private void cancelTradeRequest(TradeRequest request, UserId userId, TldServiceFacade tld) throws ServiceException {
        try {
            request.setStatus(TradeRequestStatus.Suppressed);
            AppServiceFacade.getTradeService().updateTradeRequest(request, userId, tld);
        } catch (Exception e) {
            throw new ServiceException("cancelTradeRequest(" + request.getId() + "," + userId + ") failed", e);
        }
    }

    @Override
    public void deleteRequest(Request request, String login, UserId userId, TldServiceFacade tld) throws ServiceException {

    }

    @Override
    public Set<String> getDocumentsHandleLinkedToRequest(Request request, UserId userId, TldServiceFacade tld) throws ServiceException {
        Preconditions.checkNotNull(request, "request");

        final String sql = new StringBuilder().append(" SELECT  nicope.Request_document_R.DOCUMENT_ID ")
                                              .append(" FROM  nicope.Request_document_R")
                                              .append(" WHERE ")
                                              .append(" nicope.Request_document_R.request_ID= ?")
                                              .append(" and  nicope.Request_document_R.request_type= ?")
                                              .toString();

        ResultSetProcessor<Set<String>> processor = new ResultSetProcessor<Set<String>>() {
            @Override
            public void populateResultFromResultSet(ResultSet resultSet) throws SQLException, ServiceException {
                this.result = SqlRequestService.this.getDocumentsHandleFromResultSet(resultSet);
            }
        };

        int id = request.getId();
        String type = request.getClass().getSimpleName();

        QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlConnectionFactory);

        try {
            queryStatementManagement.executeQuery(processor, sql, id, type);
            return processor.getResult();
        } catch (SQLException e) {
            throw new ServiceException("cannot find documents linked to " + type + " " + id, e);
        }
    }

    private Set<String> getDocumentsHandleFromResultSet(ResultSet resultSet) throws SQLException {
        Set<String> result = new HashSet<String>();
        while (resultSet.next()) {
            String docHandle = resultSet.getString(1);
            if (docHandle != null) {
                result.add(docHandle.trim());
            }
        }
        return result;
    }

}
