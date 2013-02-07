/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.beans.customer.status;

import java.util.List;

import fr.afnic.commons.beans.customer.Customer;

public class Inactive extends CustomerStatus {

    private static final long serialVersionUID = 1L;

    @SuppressWarnings("unchecked")
    @Override
    public <S extends CustomerStatus> void populateNextAllowedStatus(List<Class<S>> list, Customer customer) {
        if (customer.hasEmptyPortfolio()) {
            list.add((Class<S>) CustomerStatus.ARCHIVED.getClass());
        }

        list.add((Class<S>) CustomerStatus.ACTIVE.getClass());

    }

}
