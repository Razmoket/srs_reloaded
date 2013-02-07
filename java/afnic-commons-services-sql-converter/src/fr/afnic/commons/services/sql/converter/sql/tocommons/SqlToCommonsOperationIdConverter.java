package fr.afnic.commons.services.sql.converter.sql.tocommons;

import java.sql.ResultSet;
import java.sql.SQLException;

import fr.afnic.commons.beans.operations.OperationId;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.converter.AbstractConverter;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class SqlToCommonsOperationIdConverter extends AbstractConverter<ResultSet, OperationId> {

    public SqlToCommonsOperationIdConverter() {
        super(ResultSet.class, OperationId.class);
    }

    @Override
    public OperationId convert(ResultSet toConvert, UserId userId, TldServiceFacade tld) throws ServiceException {
        OperationId retour;
        try {
            retour = new OperationId(toConvert.getInt(1));
        } catch (SQLException e) {
            throw new ServiceException(e.getMessage(), e);
        }
        return retour;
    }

}
