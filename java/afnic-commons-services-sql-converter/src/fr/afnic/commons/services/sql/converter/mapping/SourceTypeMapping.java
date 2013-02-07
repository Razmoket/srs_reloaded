package fr.afnic.commons.services.sql.converter.mapping;

import fr.afnic.commons.beans.operations.qualification.QualificationSource;
import fr.afnic.commons.services.converter.ConstantTypeMapping;

public class SourceTypeMapping extends ConstantTypeMapping<Integer, QualificationSource> {

    public SourceTypeMapping() {
        super(Integer.class, QualificationSource.class);
    }

    @Override
    protected void populateMap() {
        this.addMapping(1, QualificationSource.Auto);
        this.addMapping(2, QualificationSource.Control);
        this.addMapping(3, QualificationSource.Reporting);
        this.addMapping(4, QualificationSource.Plaint);
        this.addMapping(5, QualificationSource.Survey);
    }

}
