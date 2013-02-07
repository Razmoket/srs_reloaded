package fr.afnic.commons.services.sql.converter.sql.tocommons;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import fr.afnic.commons.beans.Ticket;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.beans.validatable.OperationFormId;
import fr.afnic.commons.services.converter.AbstractConverter;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.sql.converter.mapping.SqlColumnTicketMapping;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class SqlToCommonsTicketConverter extends AbstractConverter<ResultSet, Ticket> {

    public SqlToCommonsTicketConverter() {
        super(ResultSet.class, Ticket.class);
    }

    @Override
    public Ticket convert(ResultSet toConvert, UserId userId, TldServiceFacade tld) throws ServiceException {
        Ticket ticket = null;
        try {
            String status = toConvert.getString(SqlColumnTicketMapping.status.toString());
            String requester = toConvert.getString(SqlColumnTicketMapping.requester.toString());
            Date createDate = toConvert.getTimestamp(SqlColumnTicketMapping.createDate.toString());
            String registrarCode = toConvert.getString(SqlColumnTicketMapping.registrarCode.toString());
            int formId = toConvert.getInt(SqlColumnTicketMapping.formId.toString());
            String operation = toConvert.getString(SqlColumnTicketMapping.operation.toString());
            String domainName = toConvert.getString(SqlColumnTicketMapping.domainName.toString());
            String ticketId = toConvert.getString(SqlColumnTicketMapping.ticketId.toString());

            ticket = new Ticket(userId, tld);
            ticket.setStatusFromString(status);
            ticket.setOriginalRequesterName(requester);
            ticket.setCreateDate(createDate);
            ticket.setRegistrarCode(registrarCode);
            ticket.setOperationFormId(new OperationFormId(formId));
            ticket.setOperationFromString(operation);
            ticket.setDomainName(domainName);
            ticket.setId(ticketId);

        } catch (SQLException e) {
            throw new ServiceException(e.getMessage(), e);
        }
        return ticket;
    }

}
