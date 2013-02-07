package fr.afnic.commons.services.sql;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fr.afnic.commons.beans.logs.ILogger;
import fr.afnic.commons.beans.profiling.users.User;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.beans.profiling.users.UserRight;
import fr.afnic.commons.services.IUserService;
import fr.afnic.commons.services.exception.NotFoundException;
import fr.afnic.commons.services.exception.NotImplementedException;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.sql.query.boa.UserSqlQueries;
import fr.afnic.commons.services.sql.utils.QueryStatementManagement;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.commons.utils.Preconditions;
import fr.afnic.utils.sql.ISqlConnectionFactory;
import fr.afnic.utils.sql.pool.PoolSqlConnectionFactoryFacade;
import fr.afnic.utils.sql.pool.SqlDatabaseEnum;

public class SqlUserService implements IUserService {

    protected static final ILogger LOGGER = AppServiceFacade.getLoggerService().getLogger(SqlUserService.class);

    private final ISqlConnectionFactory sqlConnectionFactory;

    public SqlUserService(ISqlConnectionFactory sqlConnectionFactory) {
        this.sqlConnectionFactory = Preconditions.checkNotNull(sqlConnectionFactory, "sqlConnectionFactory");
    }

    public SqlUserService(SqlDatabaseEnum database, TldServiceFacade tld) throws ServiceException {
        this.sqlConnectionFactory = PoolSqlConnectionFactoryFacade.getSqlPoolConnectionFactory(database, tld);
    }

    private User getUser(String sqlQuery, String identifiant, String password, UserId userId) throws ServiceException {

        QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlConnectionFactory);

        PreparedStatement preparedStatement = queryStatementManagement.initializeStatement(sqlQuery);
        try {
            int i = 1;
            if (identifiant != null) {
                preparedStatement.setString(i++, identifiant);
            }
            if (password != null) {
                preparedStatement.setString(i++, password);
            }
            if (userId != null) {
                preparedStatement.setInt(i++, userId.getIntValue());
            }

            if (i == 1) {
                throw new NotFoundException("No user found");
            }

        } catch (SQLException e) {
            queryStatementManagement.closeConnection();
            throw new ServiceException("getUser(" + sqlQuery + ", " + identifiant + ", " + password + ", " + userId + ") failed", e);
        }

        try {
            return queryStatementManagement.executeStatement(User.class, preparedStatement, userId, TldServiceFacade.Fr);
        } catch (ServiceException e) {
            e.printStackTrace();
            queryStatementManagement.closeConnection();
            throw new ServiceException("getUser(" + sqlQuery + ", " + identifiant + ")", e);
        }
    }

    @Override
    public List<User> getUsers(UserId userId, TldServiceFacade tld) throws ServiceException {
        QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlConnectionFactory);
        PreparedStatement preparedStatement = queryStatementManagement.initializeStatement(UserSqlQueries.GET_USERS);
        return queryStatementManagement.executeStatementList(User.class, preparedStatement, userId, tld);
    }

    @Override
    public User authenticate(String login, String pwd) throws ServiceException {
        User user = this.getUser(UserSqlQueries.GET_USER_CONTENT_WITH_LOGIN_PWD, login, pwd, null);
        if (user == null) {
            throw new NotFoundException("User not found with following login :" + login);
        }
        return user;
    }

    @Override
    public User getUser(String login, UserId userId, TldServiceFacade tld) throws ServiceException {
        User user = this.getUser(UserSqlQueries.GET_USER_CONTENT_WITH_LOGIN, login, null, null);

        if (user == null) {
            throw new NotFoundException("User not found with following login :" + login);
        }
        return user;
    }

    @Override
    public User getUser(UserId userid, UserId userId, TldServiceFacade tld) throws ServiceException {
        User user = this.getUser(UserSqlQueries.GET_USER_CONTENT_WITH_ID, null, null, userid);
        if (user == null) {
            throw new NotFoundException("User not found with following userid :" + userid);
        }
        return user;
    }

    @Override
    public String getUserLogin(String nicopeId, UserId userId, TldServiceFacade tld) throws ServiceException {

        User user = this.getUser(UserSqlQueries.GET_USER_CONTENT_WITH_NICPERS, nicopeId, null, null);
        if (user == null) {
            throw new NotFoundException("No User found with nicpersId '" + nicopeId + "'");
        }
        return user.getLogin();
    }

    @Override
    public User getUserWithNicpersId(String nicopeId, UserId userId, TldServiceFacade tld) throws ServiceException {

        User user = this.getUser(UserSqlQueries.GET_USER_CONTENT_WITH_NICPERS, nicopeId, null, null);
        if (user == null) {
            throw new NotFoundException("No User found with nicpersId '" + nicopeId + "'");
        } else {
            return user;
        }

    }

    @Override
    public void addUser(User user, UserId userId, TldServiceFacade tld) throws ServiceException {
        throw new NotImplementedException();
    }

    @Override
    public void removeUser(String userLogin, UserId userId, TldServiceFacade tld) throws ServiceException {
        throw new NotImplementedException();
    }

    @Override
    public boolean isKnownUser(String userLogin, UserId userId, TldServiceFacade tld) throws ServiceException {
        try {
            if (this.getUser(userLogin, userId, tld) != null) {
                return true;
            }
        } catch (ServiceException e) {
            return false;
        }
        return false;
    }

    @Override
    public boolean isNotKnownUser(String userLogin, UserId userId, TldServiceFacade tld) throws ServiceException {
        return !this.isKnownUser(userLogin, userId, tld);
    }

    @Override
    public List<String> getAccountManagersLogin(UserRight userRight, UserId userId, TldServiceFacade tld) throws ServiceException {

        List<String> logins = new ArrayList<String>();
        for (User user : this.getUsers(userId, tld)) {
            if (user.hasRight(userRight)) {
                logins.add(user.getLogin());
            }
        }
        return logins;

    }

    @Override
    public User createAndGetUser(User user, UserId createUserId, TldServiceFacade tld) throws ServiceException {
        throw new NotImplementedException();
    }

    @Override
    public void updatePassord(UserId userId, String newPassword, UserId updater, TldServiceFacade tld) throws ServiceException {

        Preconditions.checkIsExistingIdentifier(userId, "userId", updater, tld);
        Preconditions.checkIsExistingIdentifier(updater, "updater", updater, tld);
        Preconditions.checkNotBlank(newPassword, "newPassword");

        QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlConnectionFactory);
        try {

            PreparedStatement preparedStatement = queryStatementManagement.initializeStatement(UserSqlQueries.UPDATE_PASSWORD);
            preparedStatement.setString(1, newPassword);
            preparedStatement.setInt(2, userId.getIntValue());
            preparedStatement.executeUpdate();
            preparedStatement.close();

            if (SqlAuthorizationService.LOGGER.isTraceEnabled()) {
                SqlAuthorizationService.LOGGER.trace("User password " + userId + " updated by " + updater);
            }

        } catch (SQLException e) {
            throw new ServiceException("updatePassord(" + userId + ", *******, " + updater + ") failed", e);
        } finally {
            queryStatementManagement.closeConnection();
        }

    }

}
