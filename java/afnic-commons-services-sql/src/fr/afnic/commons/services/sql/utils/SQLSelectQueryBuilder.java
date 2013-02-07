package fr.afnic.commons.services.sql.utils;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fr.afnic.commons.beans.logs.ILogger;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;

public class SQLSelectQueryBuilder {
    protected static final ILogger LOGGER = AppServiceFacade.getLoggerService().getLogger(SQLSelectQueryBuilder.class);

    private final StringBuilder selectClause = new StringBuilder();
    private final StringBuilder fromClause = new StringBuilder();
    private final StringBuilder whereClause = new StringBuilder();
    private final StringBuilder endingClause = new StringBuilder();
    private final List<Object> listParameters = new ArrayList<Object>();

    public void appendSelect(String append) {
        this.selectClause.append(append);
    }

    public void appendFirstSelect(String append) {
        this.selectClause.insert(0, append);
    }

    public void appendFrom(String append) {
        this.fromClause.append(append);
    }

    public void appendWhere(String append) {
        this.whereClause.append(append);
    }

    public void appendEnding(String append) {
        this.endingClause.append(append);
    }

    public void addParameters(Object parameter) {
        this.listParameters.add(parameter);
    }

    public void addParameters(int pos, Object parameter) {
        this.listParameters.add(pos, parameter);
    }

    public PreparedStatement createQuery(QueryStatementManagement queryStatementManagement) throws ServiceException {
        try {
            PreparedStatement preparedStatement = queryStatementManagement.initializeStatement("SELECT " + this.selectClause.toString() + " FROM " + this.fromClause.toString() + " WHERE "
                                                                                               + this.whereClause.toString() + " " + this.endingClause.toString());
            QueryStatementManagement.initPreparedStatementParameters(preparedStatement, this.listParameters);
            return preparedStatement;
        } catch (SQLException e) {
            QueryStatementManagement.LOGGER.error("createQuery(" + queryStatementManagement + ") failed ", e);
            throw new ServiceException(e.getMessage(), e);
        }
    }

    public String getQuery() {
        return "SELECT " + this.selectClause.toString() + " FROM " + this.fromClause.toString() + " WHERE " + this.whereClause.toString()
               + " " + this.endingClause.toString();
    }

    public List<Object> getListParameters() {
        return this.listParameters;
    }
}
