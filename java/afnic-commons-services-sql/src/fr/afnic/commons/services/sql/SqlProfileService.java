package fr.afnic.commons.services.sql;

import java.sql.PreparedStatement;
import java.util.Collections;
import java.util.List;

import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.beans.profiling.users.UserProfile;
import fr.afnic.commons.beans.profiling.users.UserRight;
import fr.afnic.commons.services.IProfileService;
import fr.afnic.commons.services.exception.NotFoundException;
import fr.afnic.commons.services.exception.NotImplementedException;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.sql.converter.SqlConverterFacade;
import fr.afnic.commons.services.sql.query.boa.ProfileSqlQueries;
import fr.afnic.commons.services.sql.utils.QueryStatementManagement;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.utils.sql.ISqlConnectionFactory;
import fr.afnic.utils.sql.pool.PoolSqlConnectionFactoryFacade;
import fr.afnic.utils.sql.pool.SqlDatabaseEnum;

public class SqlProfileService implements IProfileService {

    private final ISqlConnectionFactory sqlConnectionFactory;

    public SqlProfileService(final ISqlConnectionFactory sqlConnectionFactory) {
        super();
        this.sqlConnectionFactory = sqlConnectionFactory;
    }

    public SqlProfileService(SqlDatabaseEnum database, TldServiceFacade tld) throws ServiceException {
        super();
        this.sqlConnectionFactory = PoolSqlConnectionFactoryFacade.getSqlPoolConnectionFactory(database, tld);
        ;
    }

    @Override
    public UserProfile getProfileWithName(String profileName, UserId userId, TldServiceFacade tld) throws ServiceException {
        throw new NotImplementedException("Deprecated");
    }

    @Override
    public UserProfile getProfileWithId(int profileId, UserId userId, TldServiceFacade tld) throws ServiceException {
        UserProfile ret = null;

        QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlConnectionFactory);
        PreparedStatement preparedStatement = queryStatementManagement.initializeStatement(ProfileSqlQueries.GET_PROFILE_CONTENT_WITH_ID);

        try {
            preparedStatement.setInt(1, profileId);
            ret = queryStatementManagement.executeStatement(UserProfile.class, preparedStatement, userId, tld);

        } catch (Exception e) {
            queryStatementManagement.closeConnection();
            throw new ServiceException("getProfileWithId(" + profileId + ") failed", e);
        }

        if (ret != null) {
            return ret;
        } else {
            throw new NotFoundException("No Profile found with id " + profileId);
        }
    }

    public List<UserRight> getProfileRights(int profilId) throws ServiceException {

        return Collections.emptyList();
    }

    @Override
    public List<UserRight> getProfileRight(int profilId, UserId userId, TldServiceFacade tld) throws ServiceException {
        QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlConnectionFactory);
        PreparedStatement preparedStatement = queryStatementManagement.initializeStatement(ProfileSqlQueries.GET_PROFILE_RIGHT_WITH_PROFILE_ID);

        try {
            preparedStatement.setInt(1, profilId);
            List<Integer> rightIdList = queryStatementManagement.executeStatementAsIntegerList(preparedStatement);
            return SqlConverterFacade.convertList(rightIdList, UserRight.class, userId, tld);
        } catch (Exception e) {
            queryStatementManagement.closeConnection();
            throw new ServiceException("getProfileRight(" + profilId + ") failed", e);
        }
    }

    @Override
    public List<UserProfile> getProfiles(UserId userId, TldServiceFacade tld) throws ServiceException {
        QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlConnectionFactory);
        PreparedStatement preparedStatement = queryStatementManagement.initializeStatement(ProfileSqlQueries.GET_PROFILES);

        try {
            return queryStatementManagement.executeStatementList(UserProfile.class, preparedStatement, userId, tld);
        } catch (Exception e) {
            queryStatementManagement.closeConnection();
            throw new ServiceException("getProfiles() failed", e);
        }

    }
}
