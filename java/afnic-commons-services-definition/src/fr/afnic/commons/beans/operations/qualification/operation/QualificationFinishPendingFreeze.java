package fr.afnic.commons.beans.operations.qualification.operation;

import fr.afnic.commons.beans.operations.CompositeOperation;
import fr.afnic.commons.beans.operations.OperationConfiguration;
import fr.afnic.commons.beans.operations.OperationType;
import fr.afnic.commons.beans.operations.qualification.Qualification;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class QualificationFinishPendingFreeze extends CompositeOperation {

    public QualificationFinishPendingFreeze(UserId userId, TldServiceFacade tld) {
        super(OperationConfiguration.create()
                                    .setType(OperationType.QualificationFinishPendingFreeze), userId, tld);
    }

    public QualificationFinishPendingFreeze(Qualification qualification, UserId userId, TldServiceFacade tld) {
        super(OperationConfiguration.create()
                                    .setType(OperationType.QualificationFinishPendingFreeze)
                                    .setCreateUserId(userId)
                                    .setParentId(qualification.getId()), userId, tld);
    }
}
