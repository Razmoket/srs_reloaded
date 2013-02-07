/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.services.mock;

import java.util.List;

import fr.afnic.commons.beans.profiling.users.User;
import fr.afnic.commons.services.exception.ServiceException;

public interface IUsersLoader {

    public List<User> getUsers() throws ServiceException;

}
