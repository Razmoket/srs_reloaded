package fr.afnic.commons.services.jooq.converter.jooq.mapping;

import fr.afnic.commons.beans.contact.CustomerContactStatus;
import fr.afnic.commons.services.converter.ConstantTypeMapping;

public class CustomerContactStatusMapping extends ConstantTypeMapping<Integer, CustomerContactStatus> {

    public CustomerContactStatusMapping() {
        super(Integer.class, CustomerContactStatus.class);
    }

    @Override
    protected void populateMap() {
        this.addMapping(Integer.valueOf(1), CustomerContactStatus.Active);
        this.addMapping(Integer.valueOf(2), CustomerContactStatus.Deleted);
    }

}
