package fr.afnic.commons.services.sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fr.afnic.commons.beans.logs.ILogger;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.beans.request.AuthorizationPreliminaryExam;
import fr.afnic.commons.beans.request.AuthorizationPreliminaryExamStatus;
import fr.afnic.commons.beans.request.AuthorizationRequestStatus;
import fr.afnic.commons.services.IAuthorizationPreliminaryExamService;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.proxy.ProxyAuthorizationPreliminaryExamService;
import fr.afnic.commons.services.sql.converter.CommonsToSqlAuthorizationPreliminaryExamStatusConverter;
import fr.afnic.commons.services.sql.converter.SqlToCommonsAuthorizationPreliminaryExamStatusConverter;
import fr.afnic.commons.services.sql.utils.QueryStatementManagement;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.utils.ObjectSerializer;
import fr.afnic.utils.sql.ISqlConnectionFactory;
import fr.afnic.utils.sql.pool.PoolSqlConnectionFactoryFacade;
import fr.afnic.utils.sql.pool.SqlDatabaseEnum;

public class SqlAuthorizationPreliminaryExamService extends ProxyAuthorizationPreliminaryExamService {

    private static final String PREMILINARY_EXAM_INSERTION = new StringBuilder().append("insert into nicope.L45_DOMAIN_REQUESTS (")
                                                                                .append(" id, ")
                                                                                .append(" created_at, ")
                                                                                .append(" current_status, ")
                                                                                .append(" holder_nichandle, ")
                                                                                .append(" registrar_code, ")
                                                                                .append(" domain_name, ")
                                                                                .append(" reason_text ")
                                                                                .append(" )")
                                                                                .append(" values (?,?,?,?,?,?,?)")
                                                                                .toString();

    private static final String PREMILINARY_EXAM_SELECTION = new StringBuilder().append("select ")
                                                                                .append(" id, ")
                                                                                .append(" created_at, ")
                                                                                .append(" current_status, ")
                                                                                .append(" holder_nichandle, ")
                                                                                .append(" RTRIM(LTRIM(registrar_code)) as registrar_code, ")
                                                                                .append(" RTRIM(LTRIM(LOWER(nicope.L45_DOMAIN_REQUESTS.domain_name))) as domain_name, ")
                                                                                .append(" reason_text ")
                                                                                .append(" from  nicope.L45_DOMAIN_REQUESTS ")
                                                                                .toString();

    private static final String PREMILINARY_EXAM_UPDATE = new StringBuilder().append("update  nicope.L45_DOMAIN_REQUESTS  ")
                                                                             .append(" set current_status = ?")
                                                                             .append(" where id = ? ")
                                                                             .toString();

    private static final String PREMILINARY_EXAM_DELETE = new StringBuilder().append("delete from  nicope.L45_DOMAIN_REQUESTS")
                                                                             .append(" where id = ? ")
                                                                             .toString();

    protected static final ILogger LOGGER = AppServiceFacade.getLoggerService().getLogger(SqlAuthorizationRequestService.class);

    private final ISqlConnectionFactory sqlConnectionFactory;

    public SqlAuthorizationPreliminaryExamService(ISqlConnectionFactory sqlConnectionFactory) {
        this.sqlConnectionFactory = sqlConnectionFactory;
    }

    public SqlAuthorizationPreliminaryExamService(SqlDatabaseEnum database, TldServiceFacade tld, IAuthorizationPreliminaryExamService delegationService) throws ServiceException {
        super(delegationService);
        this.sqlConnectionFactory = PoolSqlConnectionFactoryFacade.getSqlPoolConnectionFactory(database, tld);
    }

    public SqlAuthorizationPreliminaryExamService(SqlDatabaseEnum database, TldServiceFacade tld) throws ServiceException {
        this.sqlConnectionFactory = PoolSqlConnectionFactoryFacade.getSqlPoolConnectionFactory(database, tld);
    }

    @Override
    public AuthorizationPreliminaryExam createAuthorizationPreliminaryExam(AuthorizationPreliminaryExam authorizationPreliminaryExam, UserId userId, TldServiceFacade tld) throws ServiceException {

        if (authorizationPreliminaryExam.getMotivation().length() > 4000) {
            throw new ServiceException("authorizationPreliminaryExam.motivation cannot have more than 4000 caracters");
        }

        int nextId = -1;
        QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlConnectionFactory);
        try {
            nextId = this.getNextAuthorizationPreliminaryExamId();

            String sql = SqlAuthorizationPreliminaryExamService.PREMILINARY_EXAM_INSERTION;

            java.sql.Timestamp sqlCreateDate = null;
            if (authorizationPreliminaryExam.getCreateDate() == null) {
                authorizationPreliminaryExam.setCreateDate(new Date());
            }
            sqlCreateDate = new java.sql.Timestamp(authorizationPreliminaryExam.getCreateDate().getTime());

            new StringBuilder().append("insert into nicope.L45_DOMAIN_REQUESTS (")
                               .append(" id, ")
                               .append(" created_at, ")
                               .append(" current_status, ")
                               .append(" holder_nichandle, ")
                               .append(" registrar_code, ")
                               .append(" domain_name, ")
                               .append(" reason_text ")
                               .append(" )");

            PreparedStatement preparedStatement = queryStatementManagement.initializeStatement(sql);
            preparedStatement.setInt(1, nextId);
            preparedStatement.setTimestamp(2, sqlCreateDate);
            preparedStatement.setInt(3, new CommonsToSqlAuthorizationPreliminaryExamStatusConverter().convert(authorizationPreliminaryExam.getStatus(), userId, tld));
            preparedStatement.setString(4, authorizationPreliminaryExam.getHolderHandle());
            preparedStatement.setString(5, authorizationPreliminaryExam.getRegistrarCode());
            preparedStatement.setString(6, authorizationPreliminaryExam.getDomainName());
            preparedStatement.setString(7, authorizationPreliminaryExam.getMotivation());

            preparedStatement.executeUpdate();
            preparedStatement.close();

        } catch (SQLException e) {
            throw new ServiceException("insertAuthorizationPreliminaryExam() failed:\n" + ObjectSerializer.toXml(authorizationPreliminaryExam), e);
        } finally {
            queryStatementManagement.closeConnection();
        }

        AuthorizationPreliminaryExam ret = authorizationPreliminaryExam.copy();
        ret.setId(nextId);
        return ret;
    }

    private int getNextAuthorizationPreliminaryExamId() throws ServiceException {
        QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlConnectionFactory);
        try {

            String sql = "SELECT L45_DOMAIN_REQUESTS_SEQ.nextval from dual";

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

    @Override
    public List<AuthorizationPreliminaryExam> getAuthorizationPreliminaryExamsToUseForAuthorizationRequestCreation(UserId userId, TldServiceFacade tld) throws ServiceException {

        String sql = new StringBuilder().append(SqlAuthorizationPreliminaryExamService.PREMILINARY_EXAM_SELECTION)
                                        .append(" where current_status = ?")
                                        .append("and not exists(")
                                        .append("      select 1 from nicope.L45_DOMAIN_REQUESTS running ")
                                        .append("      where current_status=2 ")
                                        .append("       and  RTRIM(LTRIM(LOWER(nicope.L45_DOMAIN_REQUESTS.domain_name)))  = RTRIM(LTRIM(LOWER(running.domain_name))) ")
                                        .append(")")
                                        .append("and not exists(")
                                        .append("      select 1 from nicope.L45_DOMAIN_REQUESTS anotherPending ")
                                        .append("      where current_status=1 ")
                                        .append("       and  RTRIM(LTRIM(LOWER(nicope.L45_DOMAIN_REQUESTS.domain_name))) =  RTRIM(LTRIM(LOWER(anotherPending.domain_name))) ")
                                        .append("       and nicope.L45_DOMAIN_REQUESTS.id > anotherPending.id ")
                                        .append(")")
                                        .append("and not exists(")
                                        .append("      select 1 from nicope.autorisationrequest ")
                                        .append("      where status in('" + AuthorizationRequestStatus.Running + "'")
                                        .append("                      ,'" + AuthorizationRequestStatus.Answered + "'")
                                        .append("                      ,'" + AuthorizationRequestStatus.Generated + "'")
                                        .append("                      ,'" + AuthorizationRequestStatus.Failed + "')")
                                        .append("       and RTRIM(LTRIM(LOWER(nicope.L45_DOMAIN_REQUESTS.domain_name))) = nicope.autorisationrequest.RESOURCE_REQUESTED ")
                                        .append(")")
                                        .append(" order by id")
                                        .toString();

        List<AuthorizationPreliminaryExam> results = new ArrayList<AuthorizationPreliminaryExam>();

        if (SqlAuthorizationService.LOGGER.isTraceEnabled())
            SqlAuthorizationService.LOGGER.trace("Execute " + sql);

        QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlConnectionFactory);
        try {
            PreparedStatement preparedStatement = queryStatementManagement.initializeStatement(sql);
            preparedStatement.setInt(1, new CommonsToSqlAuthorizationPreliminaryExamStatusConverter().convert(AuthorizationPreliminaryExamStatus.Pending, userId, tld));
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                results.add(this.getAuthorizationPreliminaryExamFromResultSet(resultSet, userId, tld));
            }
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            throw new ServiceException("getAuthorizationPreliminaryExams() failed with query: " + sql, e);
        } finally {
            queryStatementManagement.closeConnection();
        }
        return results;

    }

    @Override
    public AuthorizationPreliminaryExam getAuthorizationPreliminaryExamWithId(int id, UserId userId, TldServiceFacade tld) throws ServiceException {

        String sql = new StringBuilder().append(SqlAuthorizationPreliminaryExamService.PREMILINARY_EXAM_SELECTION)
                                        .append(" where id = ?")
                                        .toString();

        AuthorizationPreliminaryExam result = null;

        if (SqlAuthorizationService.LOGGER.isTraceEnabled()) {
            SqlAuthorizationService.LOGGER.trace("Execute " + sql + " with id=" + id);
        }

        QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlConnectionFactory);
        try {
            PreparedStatement preparedStatement = queryStatementManagement.initializeStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                result = this.getAuthorizationPreliminaryExamFromResultSet(resultSet, userId, tld);
            }
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            throw new ServiceException("getAuthorizationPreliminaryExamWithId(" + id + ") failed", e);
        } finally {
            queryStatementManagement.closeConnection();
        }
        return result;

    }

    private AuthorizationPreliminaryExam getAuthorizationPreliminaryExamFromResultSet(ResultSet resultSet, UserId userId, TldServiceFacade tld) throws SQLException, ServiceException {
        AuthorizationPreliminaryExam result = new AuthorizationPreliminaryExam();

        int ind = 1;
        result.setId(resultSet.getInt(ind++));

        result.setCreateDate(new Date(resultSet.getTimestamp(ind++).getTime()));
        result.setStatus(new SqlToCommonsAuthorizationPreliminaryExamStatusConverter().convert(resultSet.getInt(ind++), userId, tld));
        result.setHolderHandle(resultSet.getString(ind++));
        result.setRegistrarCode(resultSet.getString(ind++));

        String domainName = resultSet.getString(ind++);
        if (domainName != null) {
            domainName = domainName.toLowerCase().trim();
        }
        result.setDomainName(domainName);

        result.setMotivation(resultSet.getString(ind++));

        return result;
    }

    @Override
    public AuthorizationPreliminaryExam updateAuthorizationPreliminaryExam(AuthorizationPreliminaryExam authorizationPreliminaryExam, UserId userId, TldServiceFacade tld) throws ServiceException {
        QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlConnectionFactory);
        try {
            PreparedStatement preparedStatement = queryStatementManagement.initializeStatement(SqlAuthorizationPreliminaryExamService.PREMILINARY_EXAM_UPDATE);
            preparedStatement.setInt(1, new CommonsToSqlAuthorizationPreliminaryExamStatusConverter().convert(authorizationPreliminaryExam.getStatus(), userId, tld));
            preparedStatement.setInt(2, authorizationPreliminaryExam.getId());
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            throw new ServiceException("updateAuthorizationPreliminaryExam() failed with " + ObjectSerializer.toXml(authorizationPreliminaryExam), e);
        } finally {
            queryStatementManagement.closeConnection();
        }
        return authorizationPreliminaryExam.copy();
    }

    @Override
    public List<AuthorizationPreliminaryExam> getPendingAuthorizationPreliminaryExamWithDomainName(String domainName, UserId userId, TldServiceFacade tld) throws ServiceException {
        String sql = new StringBuilder().append(SqlAuthorizationPreliminaryExamService.PREMILINARY_EXAM_SELECTION)
                                        .append("Where current_status  = ?")
                                        .append("and RTRIM(LTRIM(LOWER(domain_name)))  = ?")
                                        .append(" order by id")
                                        .toString();

        List<AuthorizationPreliminaryExam> results = new ArrayList<AuthorizationPreliminaryExam>();

        if (SqlAuthorizationService.LOGGER.isTraceEnabled()) {
            SqlAuthorizationService.LOGGER.trace("Execute " + sql);
        }

        if (domainName != null) {
            domainName = domainName.trim().toLowerCase();
        }
        QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlConnectionFactory);
        try {
            PreparedStatement preparedStatement = queryStatementManagement.initializeStatement(sql);
            preparedStatement.setInt(1, new CommonsToSqlAuthorizationPreliminaryExamStatusConverter().convert(AuthorizationPreliminaryExamStatus.Pending, userId, tld));
            preparedStatement.setString(2, domainName);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                results.add(this.getAuthorizationPreliminaryExamFromResultSet(resultSet, userId, tld));
            }
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            throw new ServiceException("getAuthorizationPreliminaryExams() failed", e);
        } finally {
            queryStatementManagement.closeConnection();
        }
        return results;

    }

    @Override
    public List<AuthorizationPreliminaryExam> getAuthorizationPreliminaryExams(UserId userId, TldServiceFacade tld) throws ServiceException {
        String sql = new StringBuilder().append(SqlAuthorizationPreliminaryExamService.PREMILINARY_EXAM_SELECTION)
                                        .append(" order by id")
                                        .toString();

        List<AuthorizationPreliminaryExam> results = new ArrayList<AuthorizationPreliminaryExam>();

        if (SqlAuthorizationService.LOGGER.isTraceEnabled())
            SqlAuthorizationService.LOGGER.trace("Execute " + sql);

        QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlConnectionFactory);
        try {
            PreparedStatement preparedStatement = queryStatementManagement.initializeStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                results.add(this.getAuthorizationPreliminaryExamFromResultSet(resultSet, userId, tld));
            }
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            throw new ServiceException("getAuthorizationPreliminaryExams() failed", e);
        } finally {
            queryStatementManagement.closeConnection();
        }
        return results;
    }

    @Override
    public void delete(AuthorizationPreliminaryExam exam, UserId userId, TldServiceFacade tld) throws ServiceException {
        QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlConnectionFactory);
        try {

            PreparedStatement preparedStatement = queryStatementManagement.initializeStatement(SqlAuthorizationPreliminaryExamService.PREMILINARY_EXAM_DELETE);
            preparedStatement.setInt(1, exam.getId());
            preparedStatement.executeUpdate();

            preparedStatement.close();
        } catch (SQLException e) {
            throw new ServiceException("getAuthorizationPreliminaryExams() failed", e);
        } finally {
            queryStatementManagement.closeConnection();
        }
    }
}
