package fr.afnic.commons.services.sql.mapping;

import fr.afnic.commons.beans.contact.CustomerContactRole;
import fr.afnic.commons.services.converter.ConstantTypeMapping;

public final class CustomerContactTypeMapping extends ConstantTypeMapping<Integer, CustomerContactRole> {

    public CustomerContactTypeMapping() {
        super(Integer.class, CustomerContactRole.class);
    }

    @Override
    protected void populateMap() {
        this.addMapping(1, CustomerContactRole.Noc);
        this.addMapping(2, CustomerContactRole.Technical);
        this.addMapping(3, CustomerContactRole.Administrative);
        this.addMapping(4, CustomerContactRole.Business);
        this.addMapping(5, CustomerContactRole.ContactBilling);
        this.addMapping(7, CustomerContactRole.Product);
        this.addMapping(8, CustomerContactRole.NotDefined);
        this.addMapping(9, CustomerContactRole.Holder);

        this.addMapping(10, CustomerContactRole.Admin);
        this.addMapping(11, CustomerContactRole.Billing);

    }

}
