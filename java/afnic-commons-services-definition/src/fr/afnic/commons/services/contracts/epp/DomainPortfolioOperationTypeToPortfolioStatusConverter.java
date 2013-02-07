package fr.afnic.commons.services.contracts.epp;

import java.util.HashMap;

import fr.afnic.commons.beans.operations.qualification.PortfolioStatus;
import fr.afnic.commons.beans.operations.qualification.operation.DomainPortfolioOperationType;

public class DomainPortfolioOperationTypeToPortfolioStatusConverter {

    private final static DomainPortfolioOperationTypeToPortfolioStatusConverter instance = new DomainPortfolioOperationTypeToPortfolioStatusConverter();

    private final HashMap<DomainPortfolioOperationType, PortfolioStatus> map = new HashMap<DomainPortfolioOperationType, PortfolioStatus>();

    private DomainPortfolioOperationTypeToPortfolioStatusConverter() {
        this.map.put(DomainPortfolioOperationType.Suppress, PortfolioStatus.Suppressed);
        this.map.put(DomainPortfolioOperationType.Freeze, PortfolioStatus.Frozen);
        this.map.put(DomainPortfolioOperationType.Block, PortfolioStatus.Blocked);
        this.map.put(DomainPortfolioOperationType.Unblock, PortfolioStatus.Active);
        this.map.put(DomainPortfolioOperationType.Unfreeze, PortfolioStatus.Active);

    }

    public static PortfolioStatus convert(DomainPortfolioOperationType type) {
        return instance.convertImpl(type);
    }

    private PortfolioStatus convertImpl(DomainPortfolioOperationType type) {
        if (this.map.containsKey(type)) {
            return this.map.get(type);
        } else {
            throw new IllegalArgumentException("No value defined for " + type);
        }
    }
}
