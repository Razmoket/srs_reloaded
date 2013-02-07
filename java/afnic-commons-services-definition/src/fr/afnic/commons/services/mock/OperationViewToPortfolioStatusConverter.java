package fr.afnic.commons.services.mock;

import java.util.HashMap;

import fr.afnic.commons.beans.operations.OperationView;
import fr.afnic.commons.beans.operations.qualification.PortfolioStatus;

public class OperationViewToPortfolioStatusConverter {

    private final static OperationViewToPortfolioStatusConverter instance = new OperationViewToPortfolioStatusConverter();

    private final HashMap<OperationView, PortfolioStatus> map = new HashMap<OperationView, PortfolioStatus>();

    private OperationViewToPortfolioStatusConverter() {
        this.map.put(OperationView.QualificationsToUpdateInPendingFreeze, PortfolioStatus.Active);
        this.map.put(OperationView.QualificationsToUpdateInFreeze, PortfolioStatus.PendingFreeze);
        this.map.put(OperationView.QualificationsToUpdateInPendingBlock, PortfolioStatus.Frozen);
        this.map.put(OperationView.QualificationsToUpdateInBlock, PortfolioStatus.PendingBlock);
        this.map.put(OperationView.QualificationsToUpdateInPendingSuppress, PortfolioStatus.Blocked);
        this.map.put(OperationView.QualificationsToUpdateInSuppress, PortfolioStatus.PendingSuppress);
    }

    public static PortfolioStatus convert(OperationView view) {
        return instance.convertImpl(view);
    }

    private PortfolioStatus convertImpl(OperationView view) {

        if (this.map.containsKey(view)) {
            return this.map.get(view);
        } else {
            throw new IllegalArgumentException("No PortfolioStatus defined for view " + view);
        }

    }

}
