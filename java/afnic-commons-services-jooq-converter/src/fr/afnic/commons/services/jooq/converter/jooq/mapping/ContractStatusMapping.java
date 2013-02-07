package fr.afnic.commons.services.jooq.converter.jooq.mapping;

import fr.afnic.commons.beans.contract.ContractStatus;
import fr.afnic.commons.services.converter.ConstantTypeMapping;

public class ContractStatusMapping extends ConstantTypeMapping<Integer, ContractStatus> {

    public ContractStatusMapping() {
        super(Integer.class, ContractStatus.class);
    }

    @Override
    protected void populateMap() {
        this.addMapping(Integer.valueOf(1), ContractStatus.Active);
        this.addMapping(Integer.valueOf(2), ContractStatus.Deleted);
        this.addMapping(Integer.valueOf(3), ContractStatus.Blocked);
    }

}
