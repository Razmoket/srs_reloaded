/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.services.contracts.mock;

import junit.framework.Test;
import fr.afnic.commons.services.contracts.customer.CustomerServiceContractTest;
import fr.afnic.commons.services.facade.MockAppServiceFacade;
import fr.afnic.commons.services.facade.AppServiceFacade;

public class MockCustomerServiceTest extends CustomerServiceContractTest {

    public static Test suite() {
        return new MockCustomerServiceTest();
    }

    @Override
    public AppServiceFacade createServiceFacadeToTest() throws Exception {
        return new MockAppServiceFacade();
    }

}
