package fr.afnic.commons.services.contracts.mock;

import junit.framework.Test;
import fr.afnic.commons.services.contracts.whoiscontact.WhoisContactServiceContractTest;
import fr.afnic.commons.services.facade.MockAppServiceFacade;
import fr.afnic.commons.services.facade.AppServiceFacade;

/**
 * Test de l'implémentation mock du IContactService.
 * 
 * @author ginguene
 * 
 */
public class MockContactServiceTest extends WhoisContactServiceContractTest {

    /** Création de la suite de test */
    public static Test suite() {
        return new MockContactServiceTest();
    }

    @Override
    public AppServiceFacade createServiceFacadeToTest() throws Exception {
        return new MockAppServiceFacade();
    }

}
