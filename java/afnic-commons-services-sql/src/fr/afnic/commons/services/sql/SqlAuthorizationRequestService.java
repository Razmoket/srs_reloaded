package fr.afnic.commons.services.sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.joda.time.Interval;

import fr.afnic.commons.beans.Authorization;
import fr.afnic.commons.beans.AuthorizationOperation;
import fr.afnic.commons.beans.documents.GddDocument;
import fr.afnic.commons.beans.logs.ILogger;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.beans.request.AuthorizationRequest;
import fr.afnic.commons.beans.request.AuthorizationRequestStatus;
import fr.afnic.commons.services.IAuthorizationRequestService;
import fr.afnic.commons.services.exception.NotFoundException;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.proxy.ProxyAuthorizationRequestService;
import fr.afnic.commons.services.sql.query.boa.AuthorizationRequestSqlQueries;
import fr.afnic.commons.services.sql.utils.QueryStatementManagement;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.commons.utils.Preconditions;
import fr.afnic.utils.sql.ISqlConnectionFactory;
import fr.afnic.utils.sql.pool.PoolSqlConnectionFactoryFacade;
import fr.afnic.utils.sql.pool.SqlDatabaseEnum;

public class SqlAuthorizationRequestService extends ProxyAuthorizationRequestService {

    private static final String AUTHORIZATION_REQUEST_SELECTION = " nicope.autorisationRequest.id"
                                                                  + " ,nicope.autorisationRequest.REF_HOLDER_REQUESTED"
                                                                  + " ,nicope.autorisationRequest.CODE_REGISTRAR"
                                                                  + " ,nicope.autorisationRequest.OPERATION"
                                                                  + " ,nicope.autorisationRequest.RESOURCE_REQUESTED"
                                                                  + " ,nicope.autorisationRequest.DATE_VALIDITE"
                                                                  + " ,nicope.autorisationRequest.DATE_CREATION"
                                                                  + " ,nicope.autorisationRequest.USER_NAME"
                                                                  + " ,nicope.autorisationRequest.STATUS"
                                                                  + " ,nicope.autorisationRequest.COMMENTS"
                                                                  + " ,nicope.autorisationRequest.AUTORISATION_ID"
                                                                  + " ,nicope.autorisationRequest.Domain_requests_ID"
                                                                  + " ,nicope.autorisationRequest.last_status_update";

    private static final String AUTHORIZATION_REQUEST_INSERTION = "insert into nicope.autorisationRequest ("
                                                                  + " nicope.autorisationRequest.id"
                                                                  + " ,nicope.autorisationRequest.REF_HOLDER_REQUESTED"
                                                                  + " ,nicope.autorisationRequest.CODE_REGISTRAR"
                                                                  + " ,nicope.autorisationRequest.OPERATION"
                                                                  + " ,nicope.autorisationRequest.NDD"
                                                                  + " ,nicope.autorisationRequest.DATE_VALIDITE"
                                                                  + " ,nicope.autorisationRequest.DATE_CREATION"
                                                                  + " ,nicope.autorisationRequest.AUTORISATION_ID"
                                                                  + " ,nicope.autorisationRequest.USER_NAME"
                                                                  + " ,nicope.autorisationRequest.STATUS"
                                                                  + " ,nicope.autorisationRequest.COMMENTS"
                                                                  + " ,nicope.autorisationRequest.LAST_STATUS_UPDATE"
                                                                  + " ,nicope.autorisationRequest.Domain_requests_ID"
                                                                  + ")"
                                                                  + " values (?,?,?,?,?,?,?,?,?,?,?,?,?)";

    protected static final ILogger LOGGER = AppServiceFacade.getLoggerService().getLogger(SqlAuthorizationRequestService.class);

    private final ISqlConnectionFactory sqlConnectionFactory;

    public SqlAuthorizationRequestService(ISqlConnectionFactory sqlConnectionFactory) {
        this.sqlConnectionFactory = sqlConnectionFactory;
    }

    public SqlAuthorizationRequestService(SqlDatabaseEnum database, TldServiceFacade tld) throws ServiceException {
        this.sqlConnectionFactory = PoolSqlConnectionFactoryFacade.getSqlPoolConnectionFactory(database, tld);
    }

    public SqlAuthorizationRequestService(SqlDatabaseEnum database, TldServiceFacade tld, IAuthorizationRequestService delegationService) throws ServiceException {
        super(delegationService);
        this.sqlConnectionFactory = PoolSqlConnectionFactoryFacade.getSqlPoolConnectionFactory(database, tld);
    }

    @Override
    public List<AuthorizationRequest> getAuthorizationRequestsWithDomain(String domain, UserId userId, TldServiceFacade tld) throws ServiceException {

        List<AuthorizationRequest> result = new ArrayList<AuthorizationRequest>();

        String sql = " SELECT "
                     + SqlAuthorizationRequestService.AUTHORIZATION_REQUEST_SELECTION
                     + " FROM "
                     + " nicope.autorisationRequest"
                     + " WHERE "
                     + " nicope.autorisationRequest.RESOURCE_REQUESTED = ?"
                     + " order by nicope.autorisationRequest.id desc";

        if (SqlAuthorizationService.LOGGER.isTraceEnabled())
            SqlAuthorizationService.LOGGER.trace("Execute " + sql + " with domain=" + domain);
        QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlConnectionFactory);
        try {
            PreparedStatement preparedStatement = queryStatementManagement.initializeStatement(sql);
            preparedStatement.setString(1, domain);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                result.add(this.getAuthorizationRequestFromResultSet(resultSet, userId, tld));
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            throw new ServiceException("getAuthorizationRequestsWithDomain(" + domain + ") failed", e);
        } finally {
            queryStatementManagement.closeConnection();
        }
        return result;
    }

    @Override
    public List<AuthorizationRequest> getAuthorizationRequestsWithDomainLike(String domain, UserId userId, TldServiceFacade tld) throws ServiceException {

        List<AuthorizationRequest> result = new ArrayList<AuthorizationRequest>();

        String sql = " SELECT "
                     + SqlAuthorizationRequestService.AUTHORIZATION_REQUEST_SELECTION
                     + " FROM "
                     + " nicope.autorisationRequest"
                     + " WHERE "
                     + " nicope.autorisationRequest.RESOURCE_REQUESTED like ?"
                     + " order by nicope.autorisationRequest.id desc";

        if (SqlAuthorizationService.LOGGER.isTraceEnabled())
            SqlAuthorizationService.LOGGER.trace("Execute " + sql + " with domain=" + domain);
        QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlConnectionFactory);
        try {
            PreparedStatement preparedStatement = queryStatementManagement.initializeStatement(sql);
            preparedStatement.setString(1, "%" + domain + "%");
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                result.add(this.getAuthorizationRequestFromResultSet(resultSet, userId, tld));
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            throw new ServiceException("getAuthorizationRequestsWithDomainLike(" + domain + ") failed", e);
        } finally {
            queryStatementManagement.closeConnection();
        }
        return result;
    }

    @Override
    public void updateAuthorizationRequest(AuthorizationRequest authorizationRequest, UserId userId, TldServiceFacade tld) throws ServiceException {
        Preconditions.checkNotNull(authorizationRequest, "authorizationRequest");
        Preconditions.checkIsExistingIdentifier(userId, "userId", userId, tld);

        QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlConnectionFactory);
        try {

            AppServiceFacade.getRequestService().history(authorizationRequest, userId, tld);

            String sql = "UPDATE nicope.autorisationRequest "
                         + " SET "
                         + " nicope.autorisationRequest.REF_HOLDER_REQUESTED=?"
                         + " ,nicope.autorisationRequest.Code_REGISTRAR=?"
                         + " ,nicope.autorisationRequest.OPERATION=?"
                         + " ,nicope.autorisationRequest.RESOURCE_REQUESTED=?"
                         + " ,nicope.autorisationRequest.USER_NAME=?"
                         + " ,nicope.autorisationRequest.AUTORISATION_ID=?"
                         + " ,nicope.autorisationRequest.Status=?"
                         + " ,nicope.autorisationRequest.COMMENTS=?"
                         + " ,nicope.autorisationRequest.Last_status_update=SYSDATE"
                         + " WHERE "
                         + " nicope.autorisationRequest.id =  " + authorizationRequest.getId();

            if (SqlAuthorizationService.LOGGER.isTraceEnabled())
                SqlAuthorizationService.LOGGER.trace("Execute " + sql + " with id " + authorizationRequest.getId() + " and comment=" + authorizationRequest.getComment());

            PreparedStatement preparedStatement = queryStatementManagement.initializeStatement(sql);
            preparedStatement.setString(1, authorizationRequest.getRequestedHolderHandle());
            preparedStatement.setString(2, authorizationRequest.getRegistrarCode());
            preparedStatement.setString(3, authorizationRequest.getOperation().toString());
            preparedStatement.setString(4, authorizationRequest.getRequestedDomainName());
            preparedStatement.setString(5, userId.getObjectOwner(userId, tld).getNicpersId());
            preparedStatement.setInt(6, authorizationRequest.getAuthorizationId());
            preparedStatement.setString(7, authorizationRequest.getStatus().toString());
            preparedStatement.setString(8, authorizationRequest.getComment());
            preparedStatement.executeUpdate();
            preparedStatement.close();

            if (SqlAuthorizationService.LOGGER.isTraceEnabled()) {
                SqlAuthorizationService.LOGGER.trace("Authorization request " + authorizationRequest.getId() + " updated");
            }

            if (authorizationRequest.hasFinalStatus()) {
                authorizationRequest.archiveDocuments();

            }

        } catch (SQLException e) {
            SqlAuthorizationService.LOGGER.error("update failed", e);

            throw new ServiceException("updateAuthorizationRequest(" + authorizationRequest.getId() + ", " + userId + ") failed", e);
        } finally {
            queryStatementManagement.closeConnection();
        }
    }

    @Override
    public AuthorizationRequest getRequestToLinkWithDocument(GddDocument document, final UserId userId, final TldServiceFacade tld) throws ServiceException {
        String sql = " SELECT "
                     + SqlAuthorizationRequestService.AUTHORIZATION_REQUEST_SELECTION
                     + " FROM "
                     + " nicope.autorisationRequest"
                     + " WHERE "
                     + " nicope.autorisationRequest.RESOURCE_REQUESTED = ?"
                     + " AND nicope.autorisationRequest.REF_HOLDER_REQUESTED = ?"
                     + " AND nicope.autorisationRequest.code_registrar = ?"
                     + " AND nicope.autorisationRequest.operation = ?"
                     + " AND nicope.autorisationRequest.status NOT IN ("
                     + "'" + AuthorizationRequestStatus.Aborded + "',"
                     + "'" + AuthorizationRequestStatus.Rejected + "',"
                     + "'" + AuthorizationRequestStatus.UsedCode + "',"
                     + "'" + AuthorizationRequestStatus.ExpiredCode + "',"
                     + "'" + AuthorizationRequestStatus.Suppressed + "'"
                     + ")";

        if (SqlAuthorizationService.LOGGER.isTraceEnabled())
            SqlAuthorizationService.LOGGER.trace("Execute " + sql + " with document=" + document.getHandle());

        ResultSetProcessor<AuthorizationRequest> processor = new ResultSetProcessor<AuthorizationRequest>() {
            @Override
            public void populateResultFromResultSet(ResultSet resultSet) throws SQLException, ServiceException {
                if (resultSet.next()) {
                    this.result = SqlAuthorizationRequestService.this.getAuthorizationRequestFromResultSet(resultSet, userId, tld);
                }
            }
        };

        String refHholder = document.getContactHandle();
        String ndd = document.getDomain();
        String operation = "";
        if (document.getRequestOperation() != null) {
            operation = document.getRequestOperation().toString();
        }
        String codeRegistrar = document.getRegistrarCode();

        QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlConnectionFactory);
        try {
            queryStatementManagement.executeQuery(processor, sql, ndd, refHholder, codeRegistrar, operation);
        } catch (SQLException e) {
            throw new ServiceException("Error in getRequestToLinkWithDocument(" + document.getHandle() + ")", e);
        }

        AuthorizationRequest request = processor.getResult();
        if (request != null && request.hasAuthorization()) {

            // si une requete correspond on verifie le code d'autorisation auquel il est associé
            Authorization authorization = request.getAuthorization();

            // Si le code d'autorisation a deja ete utilisé ou si il est perime
            if (authorization.hasBeenUsed() || authorization.isNoLongerValid()) {
                return null;
            }
        }

        return processor.getResult();
    }

    @Override
    public List<AuthorizationRequest> getCreatedRequestsBewteenTwoDates(Date start, Date end, UserId userId, TldServiceFacade tld) throws ServiceException {

        List<AuthorizationRequest> result = null;

        String sql = " SELECT "
                     + SqlAuthorizationRequestService.AUTHORIZATION_REQUEST_SELECTION
                     + " FROM "
                     + " nicope.autorisationRequest"
                     + " WHERE "
                     + " nicope.autorisationRequest.date_creation >= ?"
                     + " and  nicope.autorisationRequest.date_creation <= ?"
                     + " and  nicope.autorisationRequest.status != ?";

        if (SqlAuthorizationService.LOGGER.isTraceEnabled()) SqlAuthorizationService.LOGGER.trace("Execute " + sql + " whith start:" + start + "; end:" + end);

        java.sql.Date sqlStart = new java.sql.Date(start.getTime());
        java.sql.Date sqlEnd = new java.sql.Date(end.getTime());

        QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlConnectionFactory);
        try {
            PreparedStatement preparedStatement = queryStatementManagement.initializeStatement(sql);
            preparedStatement.setDate(1, sqlStart);
            preparedStatement.setDate(2, sqlEnd);
            preparedStatement.setString(3, AuthorizationRequestStatus.Suppressed.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            result = new ArrayList<AuthorizationRequest>();

            while (resultSet.next()) {
                result.add(this.getAuthorizationRequestFromResultSet(resultSet, userId, tld));
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            throw new ServiceException("getCreatedRequestsBewteenTwoDates() failed " + " whith start:" + start + "; end:" + end, e);
        } finally {
            queryStatementManagement.closeConnection();
        }
        return result;
    }

    @Override
    public List<AuthorizationRequest> getAuthorizationRequests(UserId userId, TldServiceFacade tld) throws ServiceException {
        List<AuthorizationRequest> result = null;

        final String sql = " SELECT "
                           + SqlAuthorizationRequestService.AUTHORIZATION_REQUEST_SELECTION
                           + " FROM "
                           + " nicope.autorisationRequest";

        if (SqlAuthorizationService.LOGGER.isTraceEnabled())
            SqlAuthorizationService.LOGGER.trace("Execute " + sql);

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlConnectionFactory);
        try {
            preparedStatement = queryStatementManagement.initializeStatement(sql);
            resultSet = preparedStatement.executeQuery();
            result = new ArrayList<AuthorizationRequest>();

            while (resultSet.next()) {
                result.add(this.getAuthorizationRequestFromResultSet(resultSet, userId, tld));
            }

        } catch (SQLException e) {
            throw new ServiceException("getAuthorizationRequests() failed", e);
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    SqlAuthorizationService.LOGGER.error("close resultset failed", e);
                }
            }

            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    SqlAuthorizationService.LOGGER.error("close preparedStatement failed", e);
                }
            }

            queryStatementManagement.closeConnection();
        }
        return result;
    }

    @Override
    public List<AuthorizationRequest> getAuthorizationRequestsInNonFinalStatus(UserId userId, TldServiceFacade tld) throws ServiceException {
        List<AuthorizationRequest> result = null;

        String sql = " SELECT "
                     + SqlAuthorizationRequestService.AUTHORIZATION_REQUEST_SELECTION
                     + " FROM "
                     + " nicope.autorisationRequest"
                     + " WHERE "
                     + " nicope.autorisationRequest.status not in ( '" + AuthorizationRequestStatus.Aborded + "',"
                     + "'" + AuthorizationRequestStatus.Rejected + "',"
                     + "'" + AuthorizationRequestStatus.ExpiredCode + "',"
                     + "'" + AuthorizationRequestStatus.UsedCode + "',"
                     + "'" + AuthorizationRequestStatus.Suppressed + "'"
                     + ")";

        if (SqlAuthorizationService.LOGGER.isTraceEnabled())
            SqlAuthorizationService.LOGGER.trace("Execute " + sql);
        QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlConnectionFactory);
        try {
            PreparedStatement preparedStatement = queryStatementManagement.initializeStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            result = new ArrayList<AuthorizationRequest>();

            while (resultSet.next()) {
                result.add(this.getAuthorizationRequestFromResultSet(resultSet, userId, tld));
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            throw new ServiceException("getAuthorizationRequests() failed", e);
        } finally {
            queryStatementManagement.closeConnection();
        }
        return result;
    }

    @Override
    public AuthorizationRequest getAuthorizationRequestWithId(int id, UserId userId, TldServiceFacade tld) throws ServiceException {
        AuthorizationRequest result = null;

        String sql = " SELECT "
                     + SqlAuthorizationRequestService.AUTHORIZATION_REQUEST_SELECTION
                     + " FROM "
                     + " nicope.autorisationRequest"
                     + " WHERE "
                     + " nicope.autorisationRequest.id = ?";

        if (SqlAuthorizationService.LOGGER.isTraceEnabled())
            SqlAuthorizationService.LOGGER.trace("Execute " + sql + " with id=" + id);

        QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlConnectionFactory);
        try {
            PreparedStatement preparedStatement = queryStatementManagement.initializeStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                result = this.getAuthorizationRequestFromResultSet(resultSet, userId, tld);
            }
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            throw new ServiceException("getAuthorizationRequestWithId(" + id + ") failed", e);
        } finally {
            queryStatementManagement.closeConnection();
        }
        return result;
    }

    /*  @Override
      public int createAuthorizationRequest(AuthorizationRequest authorizationRequest) throws ServiceException {

          //Ne fonctionne plus depuis la création de l'implémentation soap.

          Preconditions.checkNotNull(authorizationRequest, "authorizationRequest");
          Preconditions.checkNotNull(authorizationRequest.getOperation(), "authorizationRequest.operation");
          Preconditions.checkNotNull(authorizationRequest.getStatus(), "authorizationRequest.status");
          Preconditions.checkIsExistingIdentifier(authorizationRequest.getUserId(), "authorizationRequest.userId");
          Preconditions.checkIsNull(authorizationRequest.getStatus(), "authorizationRequest.status");

          authorizationRequest.setStatus(AuthorizationRequestStatus.Running);

          QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlConnectionFactory);
          try {
              int nextId = this.getNextAuthorizationRequestId();

              String sql = SqlAuthorizationRequestService.AUTHORIZATION_REQUEST_INSERTION;
              if (SqlAuthorizationService.LOGGER.isTraceEnabled())
                  SqlAuthorizationService.LOGGER.trace("Execute " + sql);

              java.sql.Timestamp sqlCreateDate = null;
              if (authorizationRequest.getCreateDate() != null) {
                  sqlCreateDate = new java.sql.Timestamp(authorizationRequest.getCreateDate().getTime());
              }

              java.sql.Timestamp sqlValidityDate = null;
              if (authorizationRequest.getValidityDate() != null) {
                  sqlValidityDate = new java.sql.Timestamp(authorizationRequest.getValidityDate().getTime());
              }

              String comment = authorizationRequest.getComment();
              if (comment != null) {
                  comment = comment.replaceAll("'", "\\'");
              }

              PreparedStatement preparedStatement = queryStatementManagement.initializeStatement(sql);
              preparedStatement.setInt(1, nextId);
              preparedStatement.setString(2, authorizationRequest.getRequestedHolderHandle());
              preparedStatement.setString(3, authorizationRequest.getRegistrarCode());
              preparedStatement.setString(4, authorizationRequest.getOperation().toString().trim());
              preparedStatement.setString(5, authorizationRequest.getRequestedDomainName());
              preparedStatement.setTimestamp(6, sqlValidityDate);
              preparedStatement.setTimestamp(7, sqlCreateDate);
              preparedStatement.setInt(8, authorizationRequest.getAuthorizationId());

              preparedStatement.setString(9, authorizationRequest.getUserId().getObjectOwner().getNicpersId());

              preparedStatement.setString(10, authorizationRequest.getStatus().toString());
              preparedStatement.setString(11, comment);
              preparedStatement.setTimestamp(12, sqlCreateDate);
              preparedStatement.setInt(13, authorizationRequest.getAuthorizationPreliminaryExamId());

              preparedStatement.executeUpdate();
              preparedStatement.close();

              authorizationRequest.setId(nextId);

          } catch (SQLException e) {
              throw new ServiceException("createAuthorizationRequest(" + authorizationRequest.getRequestedDomainName() + ", "
                                         + authorizationRequest.getRegistrarCode() + ", "
                                         + authorizationRequest.getRequestedHolderHandle()
                                         + ")  failed", e);
          } finally {
              queryStatementManagement.closeConnection();
          }
          return authorizationRequest.getId();
      }*/

    private int getNextAuthorizationRequestId() throws ServiceException {

        QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlConnectionFactory);
        try {

            String sql = "SELECT SEQ_AUTORISATIONREQUEST.nextval from dual";

            if (SqlAuthorizationService.LOGGER.isTraceEnabled())
                SqlAuthorizationService.LOGGER.trace("Execute " + sql);

            PreparedStatement preparedStatement = queryStatementManagement.initializeStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            int nextId = -1;
            if (resultSet.next()) {
                nextId = resultSet.getInt(1);
            }
            resultSet.close();
            preparedStatement.close();

            return nextId;
        } catch (Exception e) {
            throw new ServiceException("getNextAuthorizationRequestId() failed", e);
        } finally {
            queryStatementManagement.closeConnection();
        }
    }

    /**
     * Retourne un AuthorizationRequest a partir d'un resulset qui a été obtenue un utilisant la methode getAuthorizationRequestSelectionQuery() pour
     * écrire la partie select de la requete. La méthode next() du resultSet doit avoir été appelée avant de le passer comme parametre à cette
     * méthode.
     * 
     * @param resultSet
     * @return
     * @throws SQLException
     * @throws ServiceException
     */
    private AuthorizationRequest getAuthorizationRequestFromResultSet(ResultSet resultSet, UserId userId, TldServiceFacade tld) throws SQLException, ServiceException {
        AuthorizationRequest result = new AuthorizationRequest(userId, tld);

        int ind = 1;
        result.setId(resultSet.getInt(ind++));
        result.setRequestedHolderHandle(resultSet.getString(ind++));

        result.setRegistrarCode(resultSet.getString(ind++));
        String operationStr = null;
        try {
            operationStr = resultSet.getString(ind++);
            if (operationStr != null && !operationStr.equals("null")) {
                result.setOperation(AuthorizationOperation.valueOf(operationStr));
            }
        } catch (Exception e) {
            throw new ServiceException("Invalid operation name: '" + operationStr + "' for " + result.getId());
        }

        result.setRequestedDomainName(resultSet.getString(ind++));
        result.setValidityDate(resultSet.getTimestamp(ind++));
        result.setCreateDate(resultSet.getTimestamp(ind++));

        String nicopeId = resultSet.getString(ind++);

        String statusStr = null;
        try {
            statusStr = resultSet.getString(ind++);

            //Pour compatibilité avec l'existant car le statut Problem a été renommé en Failed
            //A terme on pourrait remplacer ce test en renommant dans la base toutes les occurence de Problem en Failed
            if ("Problem".equals(statusStr)) {
                result.setStatus(AuthorizationRequestStatus.Failed);
            } else {
                result.setStatus(AuthorizationRequestStatus.valueOf(statusStr));
            }
        } catch (Exception e) {
            throw new ServiceException("Invalid status name: '" + statusStr + "' for " + result.getId());
        }

        result.setComment(resultSet.getString(ind++));
        result.setAuthorizationId(resultSet.getInt(ind++));
        result.setAuthorizationPreliminaryExamId(resultSet.getInt(ind++));

        java.sql.Timestamp timeStamp = resultSet.getTimestamp(ind++);
        if (timeStamp != null) {
            result.setLastStatusUpdate(new Date(timeStamp.getTime()));
        }

        return result;
    }

    @Override
    public Interval getSunrisePeriod(UserId userId, TldServiceFacade tld) throws ServiceException {
        Interval result = null;
        QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlConnectionFactory);

        try {

            String sql = AuthorizationRequestSqlQueries.GET_SUNRISE_PERIOD;

            if (SqlAuthorizationService.LOGGER.isTraceEnabled()) {
                SqlAuthorizationService.LOGGER.trace("Execute " + sql);
            }

            PreparedStatement preparedStatement = queryStatementManagement.initializeStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                long start = resultSet.getTimestamp(1).getTime();
                long stop = resultSet.getTimestamp(2).getTime();
                result = new Interval(start, stop);
            } else {
                throw new NotFoundException("No sunrise period found in database");
            }

            resultSet.close();
            preparedStatement.close();
            return result;
        } catch (Exception e) {
            throw new ServiceException("getSunrisePeriod() failed", e);
        } finally {
            queryStatementManagement.closeConnection();
        }
    }

}
