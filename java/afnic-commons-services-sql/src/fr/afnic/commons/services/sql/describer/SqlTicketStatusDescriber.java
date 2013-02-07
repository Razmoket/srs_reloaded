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

import fr.afnic.commons.beans.TicketStatus;
import fr.afnic.commons.beans.description.IDescriber;
import fr.afnic.commons.beans.logs.ILogger;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.utils.closer.CloseException;
import fr.afnic.utils.closer.Closer;
import fr.afnic.utils.sql.ISqlConnectionFactory;

public class SqlTicketStatusDescriber implements IDescriber<TicketStatus> {

    private static final ILogger LOGGER = AppServiceFacade.getLoggerService().getLogger(SqlTicketStatusDescriber.class);

    private HashMap<TicketStatus, String> ticketStatusMap;

    private final ISqlConnectionFactory sqlConnectionFactory;

    public SqlTicketStatusDescriber(final ISqlConnectionFactory sqlConnectionFactory) {
        this.sqlConnectionFactory = sqlConnectionFactory;
    }

    @Override
    public String getDescription(final TicketStatus ticketStatus, Locale locale, UserId userId, TldServiceFacade tld) throws ServiceException {
        if (this.ticketStatusMap == null) {
            this.populateTicketStatusMap();
        }

        /* Pour une raison inconnue de ma part, les status suivant ne sont pas répertoirié dans la table nicope.ticketdescetat */
        switch (ticketStatus) {
        case PendingHolderFax:
            return "Attente Fax titulaire";

        case PendingHolderEMail:
            return "Attente Mail titulaire";

        default:
            // Pour les autres on retourne la description contenue dans la base de données.
            final String description = this.ticketStatusMap.get(ticketStatus);
            if (description != null) {
                return description;
            } else {
                return ticketStatus.toString();
            }
        }

    }

    private void populateTicketStatusMap() throws ServiceException {
        this.ticketStatusMap = new HashMap<TicketStatus, String>();

        final String sql = "select GW_status,ETAT from nicope.ticketdescetat";
        final Connection connection = this.sqlConnectionFactory.createConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                final String statusString = resultSet.getString(1);
                try {
                    final TicketStatus status = TicketStatus.valueOf(statusString);
                    String description = resultSet.getString(2);
                    if (StringUtils.isBlank(description)) {
                        description = statusString;
                    }
                    this.ticketStatusMap.put(status, description);
                } catch (final Exception e) {
                    throw new ServiceException("No value in ticketStatus corresponding to nicope.ticketdescetat.GW_status '" + statusString + "'");
                }

            }
            resultSet.close();
            preparedStatement.close();
        } catch (final SQLException e) {
            SqlTicketStatusDescriber.LOGGER.error(e.getMessage(), e);
        } finally {
            try {
                Closer.close(preparedStatement, resultSet);
            } catch (final CloseException e) {
                throw new ServiceException("populateTicketStatusMap() failed", e);
            }
            this.sqlConnectionFactory.closeConnection(connection);
        }
    }
}
