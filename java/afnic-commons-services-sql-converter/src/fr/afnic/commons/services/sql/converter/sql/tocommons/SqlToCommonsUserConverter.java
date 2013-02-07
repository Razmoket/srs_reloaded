package fr.afnic.commons.services.sql.converter.sql.tocommons;

import java.sql.ResultSet;
import java.sql.SQLException;

import fr.afnic.commons.beans.profiling.users.User;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.beans.profiling.users.UserProfile;
import fr.afnic.commons.services.converter.AbstractConverter;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.sql.converter.mapping.SqlColumnUserMapping;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class SqlToCommonsUserConverter extends AbstractConverter<ResultSet, User> {

    public SqlToCommonsUserConverter() {
        super(ResultSet.class, User.class);
    }

    @Override
    public User convert(ResultSet toConvert, UserId userId, TldServiceFacade tld) throws ServiceException {
        User user = null;
        try {
            int idUser = toConvert.getInt(SqlColumnUserMapping.idUser.toString());
            int idProfile = toConvert.getInt(SqlColumnUserMapping.idProfile.toString());
            int idBackupUser = toConvert.getInt(SqlColumnUserMapping.idBackupUser.toString());
            String idNicPers = toConvert.getString(SqlColumnUserMapping.iDNicPers.toString());
            String firstName = toConvert.getString(SqlColumnUserMapping.firstname.toString());
            String lastName = toConvert.getString(SqlColumnUserMapping.lastName.toString());
            String email = toConvert.getString(SqlColumnUserMapping.email.toString());
            String password = toConvert.getString(SqlColumnUserMapping.password.toString());
            int objectVersion = toConvert.getInt(SqlColumnUserMapping.objectVersion.toString());

            String nicpersLogin = toConvert.getString(SqlColumnUserMapping.nicpersLogin.toString());

            //TODO a implementer
            int idUserStatus = toConvert.getInt(SqlColumnUserMapping.idUserStatus.toString());

            user = new User(userId, tld);
            user.setBackupUserId(new UserId(idBackupUser));
            user.setEmail(email);
            user.setId(new UserId(idUser));
            user.setLogin(email);
            user.setPassword(password);
            user.setObjectVersion(objectVersion);
            user.setNicpersId(idNicPers);
            user.setNicpersLogin(nicpersLogin);
            user.setFirstName(firstName);
            user.setLastName(lastName);

            UserProfile profile = AppServiceFacade.getProfileService().getProfileWithId(idProfile, userId, tld);
            user.setProfile(profile);

        } catch (SQLException e) {
            throw new ServiceException(e.getMessage(), e);
        }
        return user;
    }

}
