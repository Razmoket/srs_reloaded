/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.beans.customer.status;

import java.util.List;

import fr.afnic.commons.beans.customer.Customer;

public class Active extends CustomerStatus {

    private static final long serialVersionUID = 2067495291708329322L;

    @SuppressWarnings("unchecked")
    @Override
    public <S extends CustomerStatus> void populateNextAllowedStatus(List<Class<S>> list, Customer customer) {
        list.add((Class<S>) Blocked.class);

        if (customer.hasNotAcivityTransfert()) {
            list.add((Class<S>) CustomerStatus.INACTIVE.getClass());
        }
    }

}
