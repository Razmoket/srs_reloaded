package fr.afnic.commons.services.mock;

import fr.afnic.commons.beans.domain.Domain;
import fr.afnic.commons.beans.domain.DomainPortfolio;
import fr.afnic.commons.beans.logs.ILogger;
import fr.afnic.commons.beans.operations.qualification.operation.DomainPortfolioOperationType;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.IDomainPortfolioService;
import fr.afnic.commons.services.exception.NotImplementedException;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class MockDomainPortfolioService implements IDomainPortfolioService {

    private static final ILogger LOGGER = AppServiceFacade.getLoggerService().getLogger(MockDomainPortfolioService.class);

    @Override
    public boolean startDomainPortfolioExport(String email, String customerCode, String userLogin, UserId userId, TldServiceFacade tld) throws ServiceException {
        throw new NotImplementedException();
    }

    @Override
    public DomainPortfolio getPortfolioSize(String customerCode, String userLogin, UserId userId, TldServiceFacade tld) throws ServiceException {
        throw new NotImplementedException();
    }

    @Override
    public void execute(DomainPortfolioOperationType operation, String holderNicHandle, UserId userId, TldServiceFacade tld) throws ServiceException {
        LOGGER.debug("execute( " + operation + ",  " + holderNicHandle + "," + userId + ")");
    }

    @Override
    public void execute(DomainPortfolioOperationType operation, Domain domain, UserId userId, TldServiceFacade tld) throws ServiceException {
        throw new NotImplementedException();
    }
}
