package fr.afnic.commons.services.sql.converter.mapping;

import fr.afnic.commons.beans.operations.qualification.PortfolioStatus;
import fr.afnic.commons.services.converter.ConstantTypeMapping;

public class PortfolioStatusMapping extends ConstantTypeMapping<Integer, PortfolioStatus> {

    public PortfolioStatusMapping() {
        super(Integer.class, PortfolioStatus.class);
    }

    @Override
    protected void populateMap() {
        this.addMapping(1, PortfolioStatus.Active);
        this.addMapping(2, PortfolioStatus.PendingFreeze);
        this.addMapping(3, PortfolioStatus.Frozen);
        this.addMapping(4, PortfolioStatus.PendingBlock);
        this.addMapping(5, PortfolioStatus.Blocked);
        this.addMapping(6, PortfolioStatus.PendingSuppress);
        this.addMapping(7, PortfolioStatus.Suppressed);
    }

}
