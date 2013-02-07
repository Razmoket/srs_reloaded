package fr.afnic.commons.services.sql.converter.sql.tocommons;

import java.sql.ResultSet;
import java.sql.SQLException;

import fr.afnic.commons.beans.operations.qualification.QualificationId;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.converter.AbstractConverter;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class SqlToCommonsQualificationIdConverter extends AbstractConverter<ResultSet, QualificationId> {

    public SqlToCommonsQualificationIdConverter() {
        super(ResultSet.class, QualificationId.class);
    }

    @Override
    public QualificationId convert(ResultSet toConvert, UserId userId, TldServiceFacade tld) throws ServiceException {
        QualificationId retour;
        try {
            retour = new QualificationId(toConvert.getInt(1));
        } catch (SQLException e) {
            throw new ServiceException(e.getMessage(), e);
        }
        return retour;
    }

}
