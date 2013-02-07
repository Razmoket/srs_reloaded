package fr.afnic.commons.services.sql;

import junit.framework.Test;
import fr.afnic.commons.services.contracts.epp.EppServiceContractTest;
import fr.afnic.commons.services.facade.MockAppServiceFacade;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.utils.sql.SqlFacade;

public class SqlEppServiceTest extends EppServiceContractTest {

    public static Test suite() {
        return new SqlEppServiceTest();
    }

    @Override
    public AppServiceFacade createServiceFacadeToTest() throws Exception {
        MockAppServiceFacade facade = new MockAppServiceFacade();
        facade.setQualificationService(new SqlQualificationService(SqlFacade.getBoaFactory()));
        facade.setOperationService(new SqlOperationService(SqlFacade.getBoaFactory()));
        //facade.setDomainPortfolioService(new SoapDomainPortfolioService());
        facade.setDomainService(new SqlDomainService(SqlFacade.getNicopeFactory()));
        facade.setWhoisContactService(new SqlWhoisContactService(SqlFacade.getNicopeFactory()));
        //facade.setEppService(new SqlEppService(SqlFacade.getNicopeFactory(), new SoapEppService()));
        facade.setUserService(new SqlUserService(SqlFacade.getBoaFactory()));
        return facade;
    }
}
