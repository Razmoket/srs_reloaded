package fr.afnic.commons.services.jooq.converter.jooq.mapping;

import fr.afnic.commons.beans.customer.status.CustomerStatus;
import fr.afnic.commons.services.converter.ConstantTypeMapping;

public class CustomerStatusMapping extends ConstantTypeMapping<Integer, CustomerStatus> {

    public CustomerStatusMapping() {
        super(Integer.class, CustomerStatus.class);
    }

    @Override
    protected void populateMap() {
        this.addMapping(Integer.valueOf(1), CustomerStatus.ACTIVE);
        this.addMapping(Integer.valueOf(2), CustomerStatus.BLOCKED);
        this.addMapping(Integer.valueOf(3), CustomerStatus.DELETED);
        this.addMapping(Integer.valueOf(4), CustomerStatus.INACTIVE);
        this.addMapping(Integer.valueOf(5), CustomerStatus.ARCHIVED);

    }

}
