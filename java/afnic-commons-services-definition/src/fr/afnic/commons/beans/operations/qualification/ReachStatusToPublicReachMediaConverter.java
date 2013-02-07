package fr.afnic.commons.beans.operations.qualification;

public class ReachStatusToPublicReachMediaConverter {
    private final static ReachStatusToPublicReachMediaConverter instance = new ReachStatusToPublicReachMediaConverter();

    private ReachStatusToPublicReachMediaConverter() {

    }

    public static PublicReachMedia convert(ReachStatus reachStatus) {
        return instance.convertImpl(reachStatus);
    }

    private PublicReachMedia convertImpl(ReachStatus reachStatus) {
        switch (reachStatus) {
        case Email:
            return PublicReachMedia.EmailAddress;

        case Phone:
            return PublicReachMedia.Phone;

        default:
            return null;
        }
    }

}
