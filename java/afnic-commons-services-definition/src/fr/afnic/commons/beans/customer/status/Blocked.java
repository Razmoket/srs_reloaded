/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.beans.customer.status;

import java.util.List;

import fr.afnic.commons.beans.customer.Customer;
import fr.afnic.commons.beans.customer.block.BlockingType;

public class Blocked extends CustomerStatus {

    private static final long serialVersionUID = 2411506184512269354L;

    private BlockingType type;

    @SuppressWarnings("unchecked")
    @Override
    public <S extends CustomerStatus> void populateNextAllowedStatus(List<Class<S>> list, Customer customer) {
        if (customer.hasNotAcivityTransfert()) {
            list.add((Class<S>) CustomerStatus.ACTIVE.getClass());
        }
        list.add((Class<S>) CustomerStatus.INACTIVE.getClass());

    }

    public BlockingType getType() {
        return this.type;
    }

    public void setType(BlockingType type) {
        this.type = type;
    }

}
