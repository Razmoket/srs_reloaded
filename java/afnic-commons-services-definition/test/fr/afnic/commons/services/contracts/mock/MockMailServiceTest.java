/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.services.contracts.mock;

import junit.framework.Test;
import fr.afnic.commons.services.contracts.mail.EmailServiceContractTest;
import fr.afnic.commons.services.facade.MockAppServiceFacade;
import fr.afnic.commons.services.facade.AppServiceFacade;

/**
 * Teste l'implémentation Mock de IMailService.
 * 
 * @author ginguene
 * 
 */
public class MockMailServiceTest extends EmailServiceContractTest {

    /** Création de la suite de test */
    public static Test suite() {
        return new MockMailServiceTest();
    }

    @Override
    public AppServiceFacade createServiceFacadeToTest() throws Exception {
        return new MockAppServiceFacade();
    }

}
