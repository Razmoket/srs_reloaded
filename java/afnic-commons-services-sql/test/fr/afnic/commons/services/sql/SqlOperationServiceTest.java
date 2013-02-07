package fr.afnic.commons.services.sql;

import junit.framework.Test;
import fr.afnic.commons.services.contracts.operation.OperationServiceContractTest;
import fr.afnic.commons.services.facade.MockAppServiceFacade;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.mock.MockDocumentService;
import fr.afnic.utils.sql.SqlFacade;

public class SqlOperationServiceTest extends OperationServiceContractTest {

    public static Test suite() {
        return new SqlOperationServiceTest();
    }

    @Override
    public AppServiceFacade createServiceFacadeToTest() throws Exception {
        MockAppServiceFacade facade = new MockAppServiceFacade();
        facade.setOperationService(new SqlOperationService(SqlFacade.getBoaFactory()));
        facade.setUserService(new SqlUserService(SqlFacade.getBoaFactory()));
        facade.setDocumentService(new MockDocumentService());
        return facade;
    }

}
