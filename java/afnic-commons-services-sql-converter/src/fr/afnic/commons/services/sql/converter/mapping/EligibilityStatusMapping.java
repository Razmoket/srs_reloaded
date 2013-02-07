package fr.afnic.commons.services.sql.converter.mapping;

import fr.afnic.commons.beans.operations.qualification.EligibilityStatus;
import fr.afnic.commons.services.converter.ConstantTypeMapping;

public class EligibilityStatusMapping extends ConstantTypeMapping<Integer, EligibilityStatus> {

    public EligibilityStatusMapping() {
        super(Integer.class, EligibilityStatus.class);
    }

    @Override
    protected void populateMap() {
        this.addMapping(1, EligibilityStatus.NotIdentified);
        this.addMapping(2, EligibilityStatus.Active);
        this.addMapping(3, EligibilityStatus.Inactive);
        this.addMapping(4, EligibilityStatus.NoMatch);
    }

}
