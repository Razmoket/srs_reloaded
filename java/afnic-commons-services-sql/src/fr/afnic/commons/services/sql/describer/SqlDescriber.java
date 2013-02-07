/*
 * $Id: $
 * $Revision: $
 */

package fr.afnic.commons.services.sql.describer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import fr.afnic.commons.beans.description.IDescribedExternallyObject;
import fr.afnic.commons.beans.description.IDescriber;
import fr.afnic.commons.beans.logs.ILogger;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.utils.closer.Closer;
import fr.afnic.utils.sql.ISqlConnectionFactory;

public abstract class SqlDescriber<DESCRIBED_OBJECT extends IDescribedExternallyObject> implements IDescriber<DESCRIBED_OBJECT> {

    private static final ILogger LOGGER = AppServiceFacade.getLoggerService().getLogger(SqlDescriber.class);

    private DescriptionMap<DESCRIBED_OBJECT> descriptionMap = null;

    protected final ISqlConnectionFactory sqlConnectionFactory;

    public SqlDescriber(final ISqlConnectionFactory sqlConnectionFactory) {
        this.sqlConnectionFactory = sqlConnectionFactory;
    }

    @Override
    public String getDescription(final DESCRIBED_OBJECT object, Locale locale, UserId userId, TldServiceFacade tld) throws ServiceException {
        return this.getOrCreateDescriptionMap(userId, tld).getDescription(object, locale);
    }

    public Set<DESCRIBED_OBJECT> getObjects(UserId userId, TldServiceFacade tld) throws ServiceException {
        return this.getOrCreateDescriptionMap(userId, tld).getObjects();
    }

    public DESCRIBED_OBJECT getObject(String dictionaryKey, UserId userId, TldServiceFacade tld) throws ServiceException {
        for (DESCRIBED_OBJECT object : this.getOrCreateDescriptionMap(userId, tld).getObjects()) {
            if (object != null && StringUtils.equals(object.getDictionaryKey(), dictionaryKey)) {
                return object;
            }
        }
        throw new IllegalArgumentException("No object with dictionary key " + dictionaryKey);
    }

    protected DescriptionMap<DESCRIBED_OBJECT> getOrCreateDescriptionMap(UserId userId, TldServiceFacade tld) throws ServiceException {
        if (this.descriptionMap == null) {
            this.descriptionMap = this.createAndPopulateDescriptionMap(userId, tld);
        }
        return this.descriptionMap;
    }

    private DescriptionMap<DESCRIBED_OBJECT> createAndPopulateDescriptionMap(UserId userId, TldServiceFacade tld) throws ServiceException {
        this.descriptionMap = this.createNewDescriptionMap();

        final Connection connection = this.sqlConnectionFactory.createConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement(this.getSql());
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                final int id = this.getId(resultSet);
                final String code = this.getCode(resultSet);

                final String fr_desc = this.getFrDesc(resultSet);
                final String en_desc = this.getEnDesc(resultSet);

                DESCRIBED_OBJECT object;
                try {
                    object = this.createNewObject(id, code, resultSet, userId, tld);
                } catch (Exception e) {
                    throw new RuntimeException("createNewObject('" + id + "', '" + code + "') failed ", e);
                }

                this.descriptionMap.addDesc(object, Locale.FRENCH, fr_desc);
                this.descriptionMap.addDesc(object, Locale.ENGLISH, en_desc);

            }

            resultSet.close();
            preparedStatement.close();
        } catch (final SQLException e) {
            SqlDescriber.LOGGER.error(e.getMessage(), e);
        } finally {
            this.sqlConnectionFactory.closeConnection(connection);
            Closer.close(preparedStatement, resultSet);

        }
        return this.descriptionMap;
    }

    protected void addDescription(DESCRIBED_OBJECT object, Locale locale, String desc, UserId userId, TldServiceFacade tld) throws ServiceException {
        this.getOrCreateDescriptionMap(userId, tld).addDesc(object, locale, desc);
    }

    protected DescriptionMap<DESCRIBED_OBJECT> createNewDescriptionMap() {
        return new DescriptionMap<DESCRIBED_OBJECT>();
    }

    protected int getId(ResultSet resultSet) throws SQLException {
        return resultSet.getInt(this.getIdColumn());
    }

    protected String getCode(ResultSet resultSet) throws SQLException {
        return resultSet.getString(this.getCodeColumn());
    }

    protected String getFrDesc(ResultSet resultSet) throws SQLException {
        return resultSet.getString(this.getFrDescColumn());
    }

    protected String getEnDesc(ResultSet resultSet) throws SQLException {
        return resultSet.getString(this.getEnDescColumn());
    }

    protected abstract String getSql();

    protected abstract int getIdColumn();

    protected abstract int getCodeColumn();

    protected abstract int getFrDescColumn();

    protected abstract int getEnDescColumn();

    protected abstract DESCRIBED_OBJECT createNewObject(int id, String code, ResultSet resultSet, UserId userId, TldServiceFacade tld) throws Exception;

}
