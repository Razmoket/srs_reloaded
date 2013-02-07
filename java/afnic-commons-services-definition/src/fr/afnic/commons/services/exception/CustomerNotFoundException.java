/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.services.exception;

/**
 * Exception levée si un domaine n'est pas trouvé.
 * 
 */
public class CustomerNotFoundException extends NotFoundException {

    private static final long serialVersionUID = 1L;

    private final String customerId;

    public CustomerNotFoundException(long customerId) {
        super("Customer '" + customerId + "' not found");
        this.customerId = Long.toString(customerId);
    }

    public CustomerNotFoundException(String customerId) {
        super("Customer '" + customerId + "' not found");
        this.customerId = customerId;
    }

    public String getCustomerId() {
        return this.customerId;
    }

}
