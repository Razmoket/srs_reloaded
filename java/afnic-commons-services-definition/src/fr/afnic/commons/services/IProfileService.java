package fr.afnic.commons.services;

import java.util.List;

import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.beans.profiling.users.UserProfile;
import fr.afnic.commons.beans.profiling.users.UserRight;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

public interface IProfileService {

    @Deprecated
    public UserProfile getProfileWithName(String profileName, UserId userId, TldServiceFacade tld) throws ServiceException;

    public UserProfile getProfileWithId(int profilId, UserId userId, TldServiceFacade tld) throws ServiceException;

    public List<UserRight> getProfileRight(int profilId, UserId userId, TldServiceFacade tld) throws ServiceException;

    public List<UserProfile> getProfiles(UserId userId, TldServiceFacade tld) throws ServiceException;
}
