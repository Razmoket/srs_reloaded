package fr.afnic.commons.services.contracts.mock;

import junit.framework.Test;
import fr.afnic.commons.services.contracts.qualification.QualificationServiceContractTest;
import fr.afnic.commons.services.facade.MockAppServiceFacade;
import fr.afnic.commons.services.facade.AppServiceFacade;

public class MockQualificationServiceTest extends QualificationServiceContractTest {

    /**
     * Création de la suite de test.
     */
    public static Test suite() {
        return new MockQualificationServiceTest();
    }

    @Override
    public AppServiceFacade createServiceFacadeToTest() throws Exception {
        return new MockAppServiceFacade();
    }

}
