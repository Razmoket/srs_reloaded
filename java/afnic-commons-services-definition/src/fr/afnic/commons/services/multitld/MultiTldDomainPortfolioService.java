package fr.afnic.commons.services.multitld;

import fr.afnic.commons.beans.domain.Domain;
import fr.afnic.commons.beans.domain.DomainPortfolio;
import fr.afnic.commons.beans.operations.qualification.operation.DomainPortfolioOperationType;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.IDomainPortfolioService;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class MultiTldDomainPortfolioService implements IDomainPortfolioService {

    protected MultiTldDomainPortfolioService() {
        super();
    }

    @Override
    public boolean startDomainPortfolioExport(String email, String customerCode, String userLogin, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getDomainPortfolioService().startDomainPortfolioExport(email, customerCode, userLogin, userId, tld);
    }

    @Override
    public DomainPortfolio getPortfolioSize(String customerCode, String userLogin, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getDomainPortfolioService().getPortfolioSize(customerCode, userLogin, userId, tld);
    }

    @Override
    public void execute(DomainPortfolioOperationType operation, String holderNicHandle, UserId userId, TldServiceFacade tld) throws ServiceException {
        tld.getServiceProvider().getDomainPortfolioService().execute(operation, holderNicHandle, userId, tld);
    }

    @Override
    public void execute(DomainPortfolioOperationType operation, Domain domain, UserId userId, TldServiceFacade tld) throws ServiceException {
        tld.getServiceProvider().getDomainPortfolioService().execute(operation, domain, userId, tld);
    }
}
