package fr.afnic.commons.beans.operations.qualification;

import java.util.HashMap;

public class EligibilityStatusToPublicQualificationItemStatusConverter {

    private final static EligibilityStatusToPublicQualificationItemStatusConverter instance = new EligibilityStatusToPublicQualificationItemStatusConverter();

    private final HashMap<EligibilityStatus, PublicQualificationItemStatus> map = new HashMap<EligibilityStatus, PublicQualificationItemStatus>();

    private EligibilityStatusToPublicQualificationItemStatusConverter() {
        this.map.put(EligibilityStatus.Active, PublicQualificationItemStatus.Ok);
        this.map.put(EligibilityStatus.Inactive, PublicQualificationItemStatus.Ok);
        this.map.put(EligibilityStatus.NoMatch, PublicQualificationItemStatus.Ko);
        this.map.put(EligibilityStatus.NotIdentified, PublicQualificationItemStatus.NotIdentified);
    }

    public static PublicQualificationItemStatus convert(EligibilityStatus eligibilityStatus) {
        return instance.convertImpl(eligibilityStatus);
    }

    private PublicQualificationItemStatus convertImpl(EligibilityStatus eligibilityStatus) {
        if (eligibilityStatus == null) {
            return PublicQualificationItemStatus.Pending;
        }

        if (this.map.containsKey(eligibilityStatus)) {
            return this.map.get(eligibilityStatus);
        } else {
            return PublicQualificationItemStatus.Pending;
        }
    }
}
