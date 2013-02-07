package fr.afnic.commons.services.multitld;

import java.util.List;

import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.beans.profiling.users.UserProfile;
import fr.afnic.commons.beans.profiling.users.UserRight;
import fr.afnic.commons.services.IProfileService;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class MultiTldProfileService implements IProfileService {

    protected MultiTldProfileService() {
        super();
    }

    @Override
    public UserProfile getProfileWithName(String profileName, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getProfileService().getProfileWithName(profileName, userId, tld);
    }

    @Override
    public UserProfile getProfileWithId(int profilId, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getProfileService().getProfileWithId(profilId, userId, tld);
    }

    @Override
    public List<UserRight> getProfileRight(int profilId, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getProfileService().getProfileRight(profilId, userId, tld);
    }

    @Override
    public List<UserProfile> getProfiles(UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getProfileService().getProfiles(userId, tld);
    }
}
