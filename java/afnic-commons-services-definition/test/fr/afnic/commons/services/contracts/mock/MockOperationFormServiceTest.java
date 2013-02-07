/**
 * 
 */
package fr.afnic.commons.services.contracts.mock;

import junit.framework.Test;
import fr.afnic.commons.services.contracts.operationform.OperationFormServiceContractTest;
import fr.afnic.commons.services.facade.MockAppServiceFacade;
import fr.afnic.commons.services.facade.AppServiceFacade;

/**
 * Test de l'implémentation moc k du IOperationFormService
 * 
 * @author alaphilippe
 * 
 */
public class MockOperationFormServiceTest extends OperationFormServiceContractTest {

    /**
     * Création de la suite de test.
     */
    public static Test suite() {
        return new MockOperationFormServiceTest();
    }

    @Override
    public AppServiceFacade createServiceFacadeToTest() throws Exception {
        return new MockAppServiceFacade();
    }

}
