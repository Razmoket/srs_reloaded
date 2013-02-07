package fr.afnic.commons.services.sql.converter.sql.tocommons;

import java.sql.ResultSet;

import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.beans.profiling.users.UserProfile;
import fr.afnic.commons.services.converter.AbstractConverter;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class SqlToCommonsProfileConverter extends AbstractConverter<ResultSet, UserProfile> {

    public SqlToCommonsProfileConverter() {
        super(ResultSet.class, UserProfile.class);
    }

    @Override
    public UserProfile convert(ResultSet toConvert, UserId userId, TldServiceFacade tld) throws ServiceException {
        UserProfile profile = null;
        try {
            int idProfile = toConvert.getInt(SqlColumnProfileMapping.idProfile.toString());
            String name = toConvert.getString(SqlColumnProfileMapping.name.toString());
            String desc = toConvert.getString(SqlColumnProfileMapping.descFr.toString());

            profile = new UserProfile(idProfile, name);
            profile.setDescription(desc);
            profile.setRights(AppServiceFacade.getProfileService().getProfileRight(idProfile, userId, tld));

        } catch (Exception e) {
            throw new ServiceException(e.getMessage(), e);
        }
        return profile;
    }
}
