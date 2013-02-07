/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.test;

import junit.framework.TestSuite;

import org.junit.runners.model.InitializationError;

import fr.afnic.commons.services.facade.AppServiceFacade;

/**
 * Classe mère de toutes les classes de contrat de service.
 * 
 * @author ginguene
 */
public abstract class ServiceFacadeContractTest extends TestSuite {

    /**
     * Constructeur chargeant toutes les sous classes de tests.
     * 
     * @throws Exception
     */
    public ServiceFacadeContractTest() {

        Class<?>[] subContractTests = this.getSubContractTests();
        if (subContractTests == null) {
            throw new IllegalArgumentException("getSubContractTests() cannot return null");
        }

        for (Class<?> testClass : subContractTests) {
            try {
                this.addTest(new ServiceFacadeTestAdapter(testClass, this));
            } catch (InitializationError e) {
                throw new RuntimeException("Failed to add test " + testClass.getSimpleName() + " to suite " + this.getClass().getSimpleName());
            }
        }
    }

    protected void createAndUseNewServiceFacade() {
        try {
            AppServiceFacade serviceFacade = this.createServiceFacadeToTest();
            if (serviceFacade == null) {
                throw new RuntimeException(this.getClass().getSimpleName() + ".createServiceFacadeToTest() return null");
            }
            AppServiceFacade.use(serviceFacade);
        } catch (Exception e) {
            throw new RuntimeException(this.getClass().getSimpleName() + ".createServiceFacadeToTest() failed", e);
        }

    }

    /**
     * Méthode permettant d'appeler des setter de customServiceFacade.
     */
    public abstract AppServiceFacade createServiceFacadeToTest() throws Exception;

    /**
     * Méthode permettant de retourner l'ensemble des classe de tests à appelé par le ServiceContractTest
     * 
     * @return
     */
    public abstract Class<?>[] getSubContractTests();

}
