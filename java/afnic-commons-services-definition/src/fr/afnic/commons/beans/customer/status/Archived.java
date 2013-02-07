/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.beans.customer.status;

import java.util.List;

import fr.afnic.commons.beans.customer.Customer;

public class Archived extends CustomerStatus {

    private static final long serialVersionUID = 8730663183204694745L;

    protected Archived() {

    }

    @Override
    public <S extends CustomerStatus> void populateNextAllowedStatus(List<Class<S>> list, Customer customer) {
        // Status final
    }

}
