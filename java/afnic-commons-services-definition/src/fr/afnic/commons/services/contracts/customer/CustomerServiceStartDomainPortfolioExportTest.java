package fr.afnic.commons.services.contracts.customer;

import org.junit.Test;

import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.commons.test.generator.UserGenerator;

public class CustomerServiceStartDomainPortfolioExportTest {

    @Test(expected = IllegalArgumentException.class)
    public void testStartDomainPortfolioExportWithBothNullParameters() throws ServiceException {
        AppServiceFacade.getDomainPortfolioService().startDomainPortfolioExport(null, null, null, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStartDomainPortfolioExportWithEmailNullParameters() throws ServiceException {
        AppServiceFacade.getDomainPortfolioService().startDomainPortfolioExport(null, "TEST", UserGenerator.generateVisitorUser().getLogin(), UserGenerator.getRootUserId(), TldServiceFacade.Fr); // Ref BE
        // Afnic
        // registry
        // code
        // TEST
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStartDomainPortfolioExportWithCustomerCodeNullParameters() throws ServiceException {
        AppServiceFacade.getDomainPortfolioService().startDomainPortfolioExport("julien.alaphilippe@afnic.fr", null, UserGenerator.generateVisitorUser().getLogin(), UserGenerator.getRootUserId(),
                                                                                TldServiceFacade.Fr);
    }

    @Test
    public void testStartDomainPortfolioExportWithCorrectParameters() throws ServiceException {
        AppServiceFacade.getDomainPortfolioService().startDomainPortfolioExport("julien.alaphilippe@afnic.fr", "TEST2", UserGenerator.generateVisitorUser().getLogin(), UserGenerator.getRootUserId(),
                                                                                TldServiceFacade.Fr);
    }
}
