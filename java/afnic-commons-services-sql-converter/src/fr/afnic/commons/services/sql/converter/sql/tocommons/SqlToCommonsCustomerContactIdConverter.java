package fr.afnic.commons.services.sql.converter.sql.tocommons;

import java.sql.ResultSet;
import java.sql.SQLException;

import fr.afnic.commons.beans.contact.CustomerContactId;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.converter.AbstractConverter;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class SqlToCommonsCustomerContactIdConverter extends AbstractConverter<ResultSet, CustomerContactId> {

    public SqlToCommonsCustomerContactIdConverter() {
        super(ResultSet.class, CustomerContactId.class);
    }

    @Override
    public CustomerContactId convert(ResultSet toConvert, UserId userId, TldServiceFacade tld) throws ServiceException {
        CustomerContactId ret;
        try {
            ret = new CustomerContactId(toConvert.getInt(1));
        } catch (SQLException e) {
            throw new ServiceException(e.getMessage(), e);
        }
        return ret;
    }

}
