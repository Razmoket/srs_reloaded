package fr.afnic.commons.services.sql.converter.sql.tocommons;

import java.sql.ResultSet;
import java.sql.SQLException;

import fr.afnic.commons.beans.operations.qualification.AutoMailReachability;
import fr.afnic.commons.beans.operations.qualification.QualificationId;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.converter.AbstractConverter;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.sql.converter.mapping.SqlColumnAutoMailReachabilityMapping;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class SqlToAutoMailReachabilityConverter extends AbstractConverter<ResultSet, AutoMailReachability> {

    public SqlToAutoMailReachabilityConverter() {
        super(ResultSet.class, AutoMailReachability.class);
    }

    @Override
    public AutoMailReachability convert(ResultSet toConvert, UserId userId, TldServiceFacade tld) throws ServiceException {
        AutoMailReachability retour = new AutoMailReachability();
        try {
            String mailAddress = toConvert.getString(SqlColumnAutoMailReachabilityMapping.mailAddress.toString());
            int idQualification = toConvert.getInt(SqlColumnAutoMailReachabilityMapping.idQualification.toString());
            int isRelance = toConvert.getInt(SqlColumnAutoMailReachabilityMapping.isRelance.toString());
            int isValid = toConvert.getInt(SqlColumnAutoMailReachabilityMapping.isValid.toString());
            retour.setEmail(mailAddress);
            retour.setIdQualification(new QualificationId(idQualification));
            retour.setRelance(isRelance == 1 ? true : false);
            retour.setValid(isValid == 1 ? true : false);
        } catch (SQLException e) {
            throw new ServiceException(e.getMessage(), e);
        }
        return retour;
    }
}
