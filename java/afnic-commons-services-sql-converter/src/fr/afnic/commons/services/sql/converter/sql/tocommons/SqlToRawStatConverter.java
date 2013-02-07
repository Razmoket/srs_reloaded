package fr.afnic.commons.services.sql.converter.sql.tocommons;

import java.sql.ResultSet;
import java.sql.SQLException;

import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.beans.statistics.RawStat;
import fr.afnic.commons.services.converter.AbstractConverter;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class SqlToRawStatConverter extends AbstractConverter<ResultSet, RawStat> {

    public SqlToRawStatConverter() {
        super(ResultSet.class, RawStat.class);
    }

    @Override
    public RawStat convert(ResultSet toConvert, UserId userId, TldServiceFacade tld) throws ServiceException {
        RawStat retour = new RawStat();
        try {
            int val = toConvert.getInt(1);
            String txt = toConvert.getString(2);
            retour.setLimit(txt);
            retour.setNb(val);
        } catch (SQLException e) {
            throw new ServiceException(e.getMessage(), e);
        }
        return retour;
    }
}
