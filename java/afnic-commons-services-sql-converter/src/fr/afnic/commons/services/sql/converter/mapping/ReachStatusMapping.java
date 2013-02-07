package fr.afnic.commons.services.sql.converter.mapping;

import fr.afnic.commons.beans.operations.qualification.ReachStatus;
import fr.afnic.commons.services.converter.ConstantTypeMapping;

public class ReachStatusMapping extends ConstantTypeMapping<Integer, ReachStatus> {

    public ReachStatusMapping() {
        super(Integer.class, ReachStatus.class);
    }

    @Override
    protected void populateMap() {
        this.addMapping(1, ReachStatus.NotIdentified);
        this.addMapping(2, ReachStatus.PendingEmail);
        this.addMapping(3, ReachStatus.PendingEmailReminder);
        this.addMapping(4, ReachStatus.PendingPhone);
        this.addMapping(5, ReachStatus.Email);
        this.addMapping(6, ReachStatus.Phone);
        this.addMapping(7, ReachStatus.NotReachable);
    }

}
