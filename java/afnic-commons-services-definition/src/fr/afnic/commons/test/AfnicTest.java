/*
 * $Id: AfnicTest.java,v 1.4 2010/08/17 14:49:21 ginguene Exp $
 * $Revision: 1.4 $
 * $Author: ginguene $
 */

package fr.afnic.commons.test;

import org.junit.Before;

import fr.afnic.commons.services.mock.MockLoggerService;

/**
 * Classe mère de toutes les classes de tests pour l'AFNIC.<br/>
 * Gère l'initialisation des services et la gestion du nettoyage des scénarios.
 * 
 * 
 */
public class AfnicTest {

    /** Service Mock de log où l'on peut voir si des warnings/erreurs ont été remontés durant le test */
    protected MockLoggerService mockLoggerService = null;// (MockLoggerService) ServiceFacade.getLoggerService();

    @Before
    public void clearLoggerService() {
        // mockLoggerService.clear();
    }

}