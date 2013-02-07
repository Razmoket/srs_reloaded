package fr.afnic.commons.services.contracts.mock;

import junit.framework.Test;
import fr.afnic.commons.services.contracts.request.RequestServiceContractTest;
import fr.afnic.commons.services.facade.MockAppServiceFacade;
import fr.afnic.commons.services.facade.AppServiceFacade;

/**
 * Teste l'implémentation Mock de IRequestService.
 * 
 * @author ginguene
 * 
 */
public class MockRequestServiceTest extends RequestServiceContractTest {

    /** Création de la suite de test */
    public static Test suite() {
        return new MockRequestServiceTest();
    }

    @Override
    public AppServiceFacade createServiceFacadeToTest() throws Exception {
        return new MockAppServiceFacade();
    }

}