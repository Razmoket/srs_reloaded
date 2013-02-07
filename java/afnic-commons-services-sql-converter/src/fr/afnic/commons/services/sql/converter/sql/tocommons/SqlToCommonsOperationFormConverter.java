package fr.afnic.commons.services.sql.converter.sql.tocommons;

import java.sql.ResultSet;
import java.sql.SQLException;

import fr.afnic.commons.beans.OperationForm;
import fr.afnic.commons.beans.TicketOperation;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.beans.validatable.OperationFormId;
import fr.afnic.commons.services.converter.AbstractConverter;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.sql.converter.mapping.SqlColumnOperationFormMapping;
import fr.afnic.commons.services.sql.converter.mapping.UserProfileMapping;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class SqlToCommonsOperationFormConverter extends AbstractConverter<ResultSet, OperationForm> {

    private final UserProfileMapping profileMapping = new UserProfileMapping();

    public SqlToCommonsOperationFormConverter() {
        super(ResultSet.class, OperationForm.class);
    }

    @Override
    public OperationForm convert(ResultSet toConvert, UserId userId, TldServiceFacade tld) throws ServiceException {
        OperationForm operationForm = null;
        try {
            int formId = toConvert.getInt(SqlColumnOperationFormMapping.formId.toString());
            String domainName = toConvert.getString(SqlColumnOperationFormMapping.domainName.toString());
            String ticketType = toConvert.getString(SqlColumnOperationFormMapping.ticketType.toString());

            operationForm = new OperationForm(userId, tld);
            operationForm.setOperationFormId(new OperationFormId(formId));
            operationForm.setDomainName(domainName);
            operationForm.setOperation(TicketOperation.valueOf(ticketType));

        } catch (SQLException e) {
            throw new ServiceException(e.getMessage(), e);
        }
        return operationForm;
    }

}
