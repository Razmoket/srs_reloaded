package fr.afnic.commons.beans.operations.qualification.operation;

import fr.afnic.commons.beans.operations.CompositeOperation;
import fr.afnic.commons.beans.operations.OperationConfiguration;
import fr.afnic.commons.beans.operations.OperationType;
import fr.afnic.commons.beans.operations.qualification.Qualification;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class QualificationFinishJustification extends CompositeOperation {

    public QualificationFinishJustification(UserId userId, TldServiceFacade tld) {
        super(OperationConfiguration.create()
                                    .setType(OperationType.QualificationFinishJustification), userId, tld);
    }

    public QualificationFinishJustification(Qualification qualification, UserId userId, TldServiceFacade tld) {
        super(OperationConfiguration.create()
                                    .setType(OperationType.QualificationFinishJustification)
                                    .setCreateUserId(userId)
                                    .setParentId(qualification.getId()), userId, tld);
    }
}
