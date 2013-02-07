package fr.afnic.commons.services.sql.converter.sql.tocommons;

import java.sql.ResultSet;
import java.sql.SQLException;

import fr.afnic.commons.beans.contactdetails.PostalAddressId;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.converter.AbstractConverter;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class SqlToCommonsPostalAddressIdConverter extends AbstractConverter<ResultSet, PostalAddressId> {

    public SqlToCommonsPostalAddressIdConverter() {
        super(ResultSet.class, PostalAddressId.class);
    }

    @Override
    public PostalAddressId convert(ResultSet toConvert, UserId userId, TldServiceFacade tld) throws ServiceException {
        PostalAddressId ret;
        try {
            ret = new PostalAddressId(toConvert.getInt(1));
        } catch (SQLException e) {
            throw new ServiceException(e.getMessage(), e);
        }
        return ret;
    }

}
