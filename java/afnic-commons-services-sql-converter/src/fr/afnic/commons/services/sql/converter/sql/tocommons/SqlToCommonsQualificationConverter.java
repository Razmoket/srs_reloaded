package fr.afnic.commons.services.sql.converter.sql.tocommons;

import java.sql.ResultSet;
import java.sql.SQLException;

import fr.afnic.commons.beans.operations.OperationId;
import fr.afnic.commons.beans.operations.qualification.Qualification;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.converter.AbstractConverter;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.sql.converter.mapping.SqlColumnQualificationMapping;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class SqlToCommonsQualificationConverter extends AbstractConverter<ResultSet, Qualification> {

    public SqlToCommonsQualificationConverter() {
        super(ResultSet.class, Qualification.class);
    }

    @Override
    public Qualification convert(ResultSet toConvert, UserId userId, TldServiceFacade tld) throws ServiceException {
        try {
            int idOperation = toConvert.getInt(SqlColumnQualificationMapping.idOperation.toString());
            return AppServiceFacade.getQualificationService().getQualification(new OperationId(idOperation), userId, tld);

        } catch (SQLException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }
}
