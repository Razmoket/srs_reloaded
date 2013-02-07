package fr.afnic.commons.services.contracts.mock;

import junit.framework.Test;
import fr.afnic.commons.services.contracts.operation.OperationServiceContractTest;
import fr.afnic.commons.services.facade.MockAppServiceFacade;
import fr.afnic.commons.services.facade.AppServiceFacade;

public class MockOperationTest extends OperationServiceContractTest {

    /** Création de la suite de test */
    public static Test suite() {
        return new MockOperationTest();
    }

    @Override
    public AppServiceFacade createServiceFacadeToTest() throws Exception {
        return new MockAppServiceFacade();
    }

}
