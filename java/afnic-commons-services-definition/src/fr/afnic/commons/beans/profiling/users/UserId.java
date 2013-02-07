package fr.afnic.commons.beans.profiling.users;

import fr.afnic.commons.beans.validatable.NumberId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class UserId extends NumberId<User> {

    private static final long serialVersionUID = 1L;

    public UserId(int id) {
        super(id);
    }

    @Override
    public User getObjectOwner(UserId userId, TldServiceFacade tld) throws ServiceException {
        return AppServiceFacade.getUserService().getUser(this, userId, tld);
    }
}
