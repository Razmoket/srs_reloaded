package fr.afnic.commons.services.sql.converter.mapping;

import fr.afnic.commons.legal.AskedAction;
import fr.afnic.commons.services.converter.ConstantTypeMapping;

public class AskedActionTypeMapping extends ConstantTypeMapping<String, AskedAction> {

    public AskedActionTypeMapping() {
        super(String.class, AskedAction.class);
    }

    @Override
    protected void populateMap() {
        this.addMapping("suppression", AskedAction.Delete);
        this.addMapping("transmission", AskedAction.Transmission);
    }
}
