package fr.afnic.commons.services.sql.converter.sql.tocommons;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.converter.AbstractConverter;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class SqlToDateConverter extends AbstractConverter<ResultSet, Date> {

    public SqlToDateConverter() {
        super(ResultSet.class, Date.class);
    }

    @Override
    public Date convert(ResultSet toConvert, UserId userId, TldServiceFacade tld) throws ServiceException {
        Date date = null;
        try {
            java.sql.Date tmp = toConvert.getDate(1);
            date = new Date(tmp.getTime());
        } catch (SQLException e) {
            throw new ServiceException(e.getMessage(), e);
        }
        return date;
    }

}
