/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.services.exception;

/**
 * Exception levée si une autorisation n'est pas trouvé.
 * 
 */
public class AuthorizationNotFoundException extends NotFoundException {

    private static final long serialVersionUID = 1L;

    private int authorizationId;

    public AuthorizationNotFoundException(int authorizationId) {
        super("Authorization " + authorizationId + " not found");
    }

    public int getAuthorizationId() {
        return this.authorizationId;
    }

}
