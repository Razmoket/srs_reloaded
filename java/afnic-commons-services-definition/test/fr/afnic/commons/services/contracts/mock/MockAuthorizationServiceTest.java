/*
 * $Id: $
 * $Revision: $
 */

package fr.afnic.commons.services.contracts.mock;

import junit.framework.Test;
import fr.afnic.commons.services.contracts.authorization.AuthorizationServiceContractTest;
import fr.afnic.commons.services.facade.MockAppServiceFacade;
import fr.afnic.commons.services.facade.AppServiceFacade;

public class MockAuthorizationServiceTest extends AuthorizationServiceContractTest {

    /** Création de la suite de test */
    public static Test suite() {
        return new MockAuthorizationServiceTest();
    }

    @Override
    public AppServiceFacade createServiceFacadeToTest() throws Exception {
        return new MockAppServiceFacade();
    }

}
