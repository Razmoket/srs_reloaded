package fr.afnic.commons.beans.operations.qualification;

import java.util.HashMap;

public class ReachStatusToPublicQualificationItemStatusConverter {
    private final static ReachStatusToPublicQualificationItemStatusConverter instance = new ReachStatusToPublicQualificationItemStatusConverter();

    private final HashMap<ReachStatus, PublicQualificationItemStatus> map = new HashMap<ReachStatus, PublicQualificationItemStatus>();

    private ReachStatusToPublicQualificationItemStatusConverter() {
        this.map.put(ReachStatus.Email, PublicQualificationItemStatus.Ok);
        this.map.put(ReachStatus.Phone, PublicQualificationItemStatus.Ok);
        this.map.put(ReachStatus.NotReachable, PublicQualificationItemStatus.Ko);
        this.map.put(ReachStatus.NotIdentified, PublicQualificationItemStatus.NotIdentified);

    }

    public static PublicQualificationItemStatus convert(ReachStatus reachStatus) {
        return instance.convertImpl(reachStatus);
    }

    private PublicQualificationItemStatus convertImpl(ReachStatus reachStatus) {
        if (reachStatus == null) {
            return PublicQualificationItemStatus.Pending;
        }

        if (this.map.containsKey(reachStatus)) {
            return this.map.get(reachStatus);
        } else {
            return PublicQualificationItemStatus.Pending;
        }

    }

}
