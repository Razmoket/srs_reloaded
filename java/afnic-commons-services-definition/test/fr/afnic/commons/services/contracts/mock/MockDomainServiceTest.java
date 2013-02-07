package fr.afnic.commons.services.contracts.mock;

import junit.framework.Test;
import fr.afnic.commons.services.contracts.domain.DomainServiceContractTest;
import fr.afnic.commons.services.facade.MockAppServiceFacade;
import fr.afnic.commons.services.facade.AppServiceFacade;

/**
 * Test de l'implémentation mock du IDomainService.
 * 
 * @author ginguene
 * 
 */
public class MockDomainServiceTest extends DomainServiceContractTest {

    /**
     * Création de la suite de test.
     */
    public static Test suite() {
        return new MockDomainServiceTest();
    }

    @Override
    public AppServiceFacade createServiceFacadeToTest() throws Exception {
        return new MockAppServiceFacade();
    }
}
