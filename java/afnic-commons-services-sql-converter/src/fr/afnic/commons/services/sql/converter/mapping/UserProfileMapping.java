package fr.afnic.commons.services.sql.converter.mapping;

import fr.afnic.commons.services.converter.ConstantTypeMapping;

public class UserProfileMapping extends ConstantTypeMapping<Integer, String> {

    public UserProfileMapping() {
        super(Integer.class, String.class);
    }

    @Override
    protected void populateMap() {
        this.addMapping(1, "Viewer");
        this.addMapping(2, "Support");
        this.addMapping(3, "AccountManager");
        this.addMapping(4, "AccountManagerAdmin");
        this.addMapping(5, "Root");
        this.addMapping(6, "Process");
        this.addMapping(7, "Ajpr");
        this.addMapping(8, "Marketing");
    }
}
