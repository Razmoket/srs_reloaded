package fr.afnic.commons.services.sql;

import junit.framework.Test;
import fr.afnic.commons.services.contracts.resultlist.ResultListServiceContractTest;
import fr.afnic.commons.services.facade.MockAppServiceFacade;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.utils.sql.SqlFacade;

public class SqlResultListServiceTest extends ResultListServiceContractTest {

    public static Test suite() {
        return new SqlResultListServiceTest();
    }

    @Override
    public AppServiceFacade createServiceFacadeToTest() throws Exception {
        MockAppServiceFacade facade = new MockAppServiceFacade();
        facade.setResultListService(new SqlResultListService(SqlFacade.getBoaFactory()));
        facade.setQualificationService(new SqlQualificationService(SqlFacade.getBoaFactory()));
        facade.setOperationService(new SqlOperationService(SqlFacade.getBoaFactory()));
        facade.setWhoisContactService(new SqlWhoisContactService(SqlFacade.getNicopeFactory()));
        return facade;
    }
}
