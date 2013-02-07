package fr.afnic.commons.services.sql.converter.mapping;

import fr.afnic.commons.beans.operations.OperationStatus;
import fr.afnic.commons.services.converter.ConstantTypeMapping;

public class OperationStatusMapping extends ConstantTypeMapping<Integer, OperationStatus> {

    public OperationStatusMapping() {
        super(Integer.class, OperationStatus.class);
    }

    @Override
    protected void populateMap() {
        this.addMapping(1, OperationStatus.Failed);
        this.addMapping(2, OperationStatus.Pending);
        this.addMapping(3, OperationStatus.Warn);
        this.addMapping(4, OperationStatus.Checked);
        this.addMapping(5, OperationStatus.Succed);
    }

}
