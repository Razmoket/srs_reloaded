package fr.afnic.commons.beans.operations.qualification.operation;

import fr.afnic.commons.beans.operations.CompositeOperation;
import fr.afnic.commons.beans.operations.OperationConfiguration;
import fr.afnic.commons.beans.operations.OperationType;
import fr.afnic.commons.beans.operations.domainportfolio.IDomainPortfolioOperation;
import fr.afnic.commons.beans.operations.qualification.Qualification;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.tld.TldServiceFacade;

/**
 * Operation de qualification qui change le statut du titulaire.
 * 
 * @author ginguene
 *
 */
public abstract class QualificationDomainPortfolioOperation extends CompositeOperation implements IDomainPortfolioOperation {

    private static OperationConfiguration createConfiguration(Qualification qualification, UserId userId, OperationType type) {
        OperationConfiguration conf = OperationConfiguration.create()
                                                            .setType(type)
                                                            .setCreateUserId(userId);

        if (qualification != null) {
            conf = conf.setParentId(qualification.getId());
        }
        return conf;
    }

    protected QualificationDomainPortfolioOperation(Qualification qualification, OperationType type, UserId userId, TldServiceFacade tld) {
        super(QualificationDomainPortfolioOperation.createConfiguration(qualification, userId, type), userId, tld);
    }

    protected QualificationDomainPortfolioOperation(OperationConfiguration conf, UserId userId, TldServiceFacade tld) {
        super(conf, userId, tld);
    }

}
