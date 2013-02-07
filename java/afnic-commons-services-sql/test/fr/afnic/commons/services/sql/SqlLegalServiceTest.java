package fr.afnic.commons.services.sql;

import junit.framework.Test;
import fr.afnic.commons.services.contracts.legal.LegalServiceContractTest;
import fr.afnic.commons.services.facade.MockAppServiceFacade;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.utils.sql.SqlFacade;

public class SqlLegalServiceTest extends LegalServiceContractTest {

    public static Test suite() {
        return new SqlLegalServiceTest();
    }

    @Override
    public AppServiceFacade createServiceFacadeToTest() throws Exception {
        MockAppServiceFacade facade = new MockAppServiceFacade();
        facade.setLegalService(new SqlLegalService(SqlFacade.getLegalFactory(), SqlFacade.getAgtfFactory()));
        //facade.setDomainService(new SqlDomainService(SqlFacade.getNicopeFactory(), new SoapDomainService()));
        facade.setWhoisContactService(new SqlWhoisContactService(SqlFacade.getNicopeFactory()));
        return facade;
    }
}
