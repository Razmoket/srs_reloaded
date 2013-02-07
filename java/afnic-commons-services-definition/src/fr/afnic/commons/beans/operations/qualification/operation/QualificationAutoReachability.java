package fr.afnic.commons.beans.operations.qualification.operation;

import fr.afnic.commons.beans.operations.CompositeOperation;
import fr.afnic.commons.beans.operations.OperationConfiguration;
import fr.afnic.commons.beans.operations.OperationType;
import fr.afnic.commons.beans.operations.qualification.Qualification;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class QualificationAutoReachability extends CompositeOperation {

    public QualificationAutoReachability(UserId userId, TldServiceFacade tld) {
        super(OperationConfiguration.create()
                                    .setType(OperationType.QualificationAutoReachability), userId, tld);
    }

    public QualificationAutoReachability(Qualification qualification, String comment, UserId userId, TldServiceFacade tld) {
        super(OperationConfiguration.create()
                                    .setType(OperationType.QualificationAutoReachability)
                                    .setCreateUserId(userId)
                                    .setComment(comment)
                                    .setParentId(qualification.getId()), userId, tld);
    }
}
