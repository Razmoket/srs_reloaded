package fr.afnic.commons.beans.operations.qualification.operation;

import fr.afnic.commons.beans.operations.OperationConfiguration;
import fr.afnic.commons.beans.operations.OperationStatus;
import fr.afnic.commons.beans.operations.OperationType;
import fr.afnic.commons.beans.operations.contact.SimpleContactOperation;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;

/**
 * Opération de gel du portefeuille d'un titulaire.
 * 
 *
 */
public class FreezeHolderDomainPortfolio extends SimpleContactOperation {

    public FreezeHolderDomainPortfolio(UserId userId, TldServiceFacade tld) {
        this(OperationConfiguration.create(), userId, tld);
    }

    public FreezeHolderDomainPortfolio(OperationConfiguration conf, UserId userId, TldServiceFacade tld) {
        super(conf.setType(OperationType.FreezeHolderDomainPortfolio), userId, tld);
    }

    @Override
    protected OperationStatus executeSimpleImpl() throws Exception {
        AppServiceFacade.getDomainPortfolioService().execute(DomainPortfolioOperationType.Freeze,
                                                             this.getContactNicHandle(), this.userIdCaller, this.tldCaller);
        return OperationStatus.Succed;
    }

}
