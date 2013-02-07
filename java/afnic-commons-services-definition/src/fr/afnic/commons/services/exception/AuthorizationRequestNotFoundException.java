/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.services.exception;

/**
 * Exception levée si une requete d'autorisation n'est pas trouvé.
 * 
 */
public class AuthorizationRequestNotFoundException extends NotFoundException {

    private static final long serialVersionUID = 1L;

    private int authorizationRequestId;

    public AuthorizationRequestNotFoundException(int authorizationRequestId) {
        super("Authorization request " + authorizationRequestId + " not found");
    }

    public int getAuthorizationRequestId() {
        return this.authorizationRequestId;
    }

}
