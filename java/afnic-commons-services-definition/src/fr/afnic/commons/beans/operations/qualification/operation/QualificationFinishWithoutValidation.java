package fr.afnic.commons.beans.operations.qualification.operation;

import fr.afnic.commons.beans.operations.CompositeOperation;
import fr.afnic.commons.beans.operations.OperationConfiguration;
import fr.afnic.commons.beans.operations.OperationType;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class QualificationFinishWithoutValidation extends CompositeOperation {

    public QualificationFinishWithoutValidation(UserId userId, TldServiceFacade tld) {
        this(OperationConfiguration.create(), userId, tld);
    }

    public QualificationFinishWithoutValidation(OperationConfiguration conf, UserId userId, TldServiceFacade tld) {
        super(conf.setType(OperationType.QualificationFinishWithoutValidation), userId, tld);
    }

}
