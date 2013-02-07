package fr.afnic.commons.services.jooq.converter.jooq.mapping;

import fr.afnic.commons.beans.domain.DomainStatus;
import fr.afnic.commons.services.converter.ConstantTypeMapping;

public class StrDomainStatusMapping extends ConstantTypeMapping<String, DomainStatus> {

    public StrDomainStatusMapping() {
        super(String.class, DomainStatus.class);
    }

    @Override
    protected void populateMap() {

        this.addMapping("BLOCKED", DomainStatus.Blocked);
        this.addMapping("NOT_OPEN", DomainStatus.Registred);
        this.addMapping("ACTIVE", DomainStatus.Active);
        this.addMapping("PARL", DomainStatus.PARL);
        this.addMapping("FROZEN", DomainStatus.Frozen);
        this.addMapping("DELETED", DomainStatus.Deleted);

    }

}
