/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.services.contracts.smtp;

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
public class SmtpEmailServiceTest extends EmailServiceContractTest {

    /** Création de la suite de test */
    public static Test suite() {
        return new SmtpEmailServiceTest();
    }

    @Override
    public AppServiceFacade createServiceFacadeToTest() throws Exception {
        return new MockAppServiceFacade();
    }

}
