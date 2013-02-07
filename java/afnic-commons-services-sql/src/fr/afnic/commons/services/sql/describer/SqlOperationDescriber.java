/*
 * $Id: $ $Revision: $ $Author: $
 */

package fr.afnic.commons.services.sql.describer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;

import fr.afnic.commons.beans.TicketOperation;
import fr.afnic.commons.beans.description.IDescriber;
import fr.afnic.commons.beans.logs.ILogger;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.utils.sql.ISqlConnectionFactory;

public class SqlOperationDescriber implements IDescriber<TicketOperation> {

    private static final ILogger LOGGER = AppServiceFacade.getLoggerService().getLogger(SqlOperationDescriber.class);

    private HashMap<TicketOperation, String> operationMap = null;

    private final ISqlConnectionFactory sqlConnectionFactory;

    public SqlOperationDescriber(final ISqlConnectionFactory sqlConnectionFactory) {
        this.sqlConnectionFactory = sqlConnectionFactory;
    }

    @Override
    public String getDescription(final TicketOperation operation, Locale locale, UserId userId, TldServiceFacade tld) throws ServiceException {
        if (this.operationMap == null) {
            this.populateOperationMap();
        }
        final String description = this.operationMap.get(operation);
        if (description != null) {
            return description;
        } else {
            return operation.toString();
        }
    }

    private void populateOperationMap() throws ServiceException {
        this.operationMap = new HashMap<TicketOperation, String>();

        final String sql = "select GW_OPERATION_NAME,OPERATION from nicope.ticketdescoper";
        final Connection connection = this.sqlConnectionFactory.createConnection();
        try {
            final PreparedStatement preparedStatement = connection.prepareStatement(sql);
            final ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                final String operationString = resultSet.getString(1);
                try {
                    final TicketOperation operation = TicketOperation.valueOf(operationString);
                    String description = resultSet.getString(2);
                    if (StringUtils.isBlank(description)) {
                        description = operationString;
                    }
                    this.operationMap.put(operation, description);
                } catch (final Exception e) {
                    throw new ServiceException("No value in Operation corresponding to nicope.ticketdescoper.GW_OPERATION_NAME '" + operationString
                                               + "'");
                }

            }
            resultSet.close();
            preparedStatement.close();
        } catch (final SQLException e) {
            SqlOperationDescriber.LOGGER.error(e.getMessage(), e);
        } finally {
            this.sqlConnectionFactory.closeConnection(connection);
        }
    }

}
