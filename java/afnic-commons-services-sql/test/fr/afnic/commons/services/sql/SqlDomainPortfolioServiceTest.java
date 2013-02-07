package fr.afnic.commons.services.sql;

import junit.framework.Test;
import fr.afnic.commons.services.contracts.domainportfolio.DomainPortfolioServiceContractTest;
import fr.afnic.commons.services.facade.MockAppServiceFacade;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.utils.sql.SqlFacade;

public class SqlDomainPortfolioServiceTest extends DomainPortfolioServiceContractTest {

    public static Test suite() {
        return new SqlDomainPortfolioServiceTest();
    }

    @Override
    public AppServiceFacade createServiceFacadeToTest() throws Exception {
        MockAppServiceFacade facade = new MockAppServiceFacade();
        facade.setQualificationService(new SqlQualificationService(SqlFacade.getBoaFactory()));
        facade.setOperationService(new SqlOperationService(SqlFacade.getBoaFactory()));
        facade.setDomainService(new SqlDomainService(SqlFacade.getNicopeFactory()));
        facade.setWhoisContactService(new SqlWhoisContactService(SqlFacade.getNicopeFactory()));
        //facade.setDomainPortfolioService(new SoapDomainPortfolioService());
        return facade;
    }
}
