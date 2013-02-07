package fr.afnic.commons.beans.operations.qualification.operation;

import java.util.Arrays;
import java.util.List;

import fr.afnic.commons.beans.domain.DomainStatus;

public enum DomainPortfolioOperationType {

    Freeze(DomainStatus.Active, DomainStatus.Blocked),
    Unfreeze(DomainStatus.Frozen),
    Block(DomainStatus.Active, DomainStatus.Frozen),
    Unblock(DomainStatus.Blocked),
    Suppress(DomainStatus.Active, DomainStatus.Frozen, DomainStatus.Blocked);

    private final DomainStatus[] listStatusAllowedForOperation;

    private DomainPortfolioOperationType(DomainStatus... listStatusAllowedForOperation) {
        this.listStatusAllowedForOperation = listStatusAllowedForOperation;
    }

    public static DomainStatus[] listStatusAllowedForOperation(DomainPortfolioOperationType opType) {
        return opType.listStatusAllowedForOperation;
    }

    public static DomainStatus[] listStatusForbiddenForOperation(DomainPortfolioOperationType opType) {
        List<DomainStatus> arrayAllowed = Arrays.asList(listStatusAllowedForOperation(opType));
        DomainStatus[] arrayAll = DomainStatus.values();
        DomainStatus[] arrayForbidden = new DomainStatus[arrayAll.length - arrayAllowed.size()];
        for (int i = 0, j = 0; i < arrayAll.length; i++) {
            if (!(arrayAllowed.contains(arrayAll[i]))) {
                arrayForbidden[j++] = arrayAll[i];
            }
        }
        return arrayForbidden;
    }

}
