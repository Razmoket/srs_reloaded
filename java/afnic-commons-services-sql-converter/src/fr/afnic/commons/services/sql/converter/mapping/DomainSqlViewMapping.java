package fr.afnic.commons.services.sql.converter.mapping;

import fr.afnic.commons.beans.operations.OperationView;
import fr.afnic.commons.services.converter.ConstantTypeMapping;

public class DomainSqlViewMapping extends ConstantTypeMapping<String, OperationView> {

    public DomainSqlViewMapping() {
        super(String.class, OperationView.class);
    }

    @Override
    protected void populateMap() {
        this.addMapping("v_domain where ", OperationView.HolderPortfolio);

    }

}
