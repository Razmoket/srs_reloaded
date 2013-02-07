package fr.afnic.commons.services.mock;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

import fr.afnic.commons.beans.logs.ILogger;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.beans.profiling.users.UserProfile;
import fr.afnic.commons.beans.profiling.users.UserRight;
import fr.afnic.commons.services.IProfileService;
import fr.afnic.commons.services.exception.NotImplementedException;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class MockProfileService implements IProfileService {

    protected static final ILogger LOGGER = AppServiceFacade.getLoggerService().getLogger(MockProfileService.class);

    private static final String CONFIGURATION_FILE_NAME = "usersProfiles.xml";

    private final UserProfileMapping profileMapping = new UserProfileMapping();

    private final HashMap<String, UserProfile> map = new HashMap<String, UserProfile>();

    @Override
    public UserProfile getProfileWithName(String profileName, UserId userId, TldServiceFacade tld) throws ServiceException {
        if (!this.map.containsKey(profileName)) {

            try {
                XmlBeanFactory beanFactory = new XmlBeanFactory(new ClassPathResource(MockProfileService.CONFIGURATION_FILE_NAME));
                UserProfile userProfile = (UserProfile) beanFactory.getBean(profileName);
                this.map.put(profileName, userProfile);
            } catch (Exception e) {
                throw new ServiceException("getProfileWithName() with " + profileName + " failed: " + e.getMessage(), e);
            }
        }
        return this.map.get(profileName);

    }

    @Override
    public UserProfile getProfileWithId(int profilId, UserId userId, TldServiceFacade tld) throws ServiceException {
        String profileName = this.profileMapping.getCommonsValue(profilId, userId, tld);
        return this.getProfileWithName(profileName, userId, tld);
    }

    @Override
    public List<UserRight> getProfileRight(int profilId, UserId userId, TldServiceFacade tld) throws ServiceException {
        throw new NotImplementedException();
    }

    @Override
    public List<UserProfile> getProfiles(UserId userId, TldServiceFacade tld) throws ServiceException {
        throw new NotImplementedException();
    }

}
