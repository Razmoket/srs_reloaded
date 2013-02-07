/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/services/exception/MissingUserRightException.java#3 $
 * $Revision: #3 $
 * $Author: ginguene $
 */

package fr.afnic.commons.services.exception;

import fr.afnic.commons.beans.profiling.users.User;
import fr.afnic.commons.beans.profiling.users.UserRight;

public class MissingUserRightException extends ServiceException {

    private static final long serialVersionUID = 1L;

    private User user;
    private UserRight missingRight;

    public MissingUserRightException(User user, UserRight missingRight) {
        super("user " + user.getLogin() + " must have right " + missingRight);
        this.user = user;
        this.missingRight = missingRight;
    }

    public User getUser() {
        return user;
    }

    public UserRight getMissingRight() {
        return missingRight;
    }

}
