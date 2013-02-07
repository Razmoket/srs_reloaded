package fr.afnic.commons.services.sql.converter.mapping;

import fr.afnic.commons.beans.boarequest.TopLevelOperationStatus;
import fr.afnic.commons.services.converter.ConstantTypeMapping;

public class TopLevelOperationStatusMapping extends ConstantTypeMapping<Integer, TopLevelOperationStatus> {

    public TopLevelOperationStatusMapping() {
        super(Integer.class, TopLevelOperationStatus.class);
    }

    @Override
    protected void populateMap() {
        this.addMapping(1, TopLevelOperationStatus.Initializing);
        this.addMapping(2, TopLevelOperationStatus.Pending);
        this.addMapping(3, TopLevelOperationStatus.Running);
        this.addMapping(4, TopLevelOperationStatus.PendingResponse);
        this.addMapping(5, TopLevelOperationStatus.ReceivedResponse);
        this.addMapping(6, TopLevelOperationStatus.Finished);
    }

}
