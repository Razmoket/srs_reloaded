package fr.afnic.commons.services.sql.converter.mapping;

import fr.afnic.commons.beans.operations.qualification.IncoherentStatus;
import fr.afnic.commons.services.converter.ConstantTypeMapping;

public class IncoherentStatusMapping extends ConstantTypeMapping<Integer, IncoherentStatus> {

    public IncoherentStatusMapping() {
        super(Integer.class, IncoherentStatus.class);
    }

    @Override
    protected void populateMap() {
        this.addMapping(1, IncoherentStatus.Incoherent);
        this.addMapping(0, IncoherentStatus.Coherent);
    }

}
