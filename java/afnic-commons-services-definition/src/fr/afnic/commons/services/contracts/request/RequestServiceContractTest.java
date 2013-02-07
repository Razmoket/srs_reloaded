package fr.afnic.commons.services.contracts.request;

import fr.afnic.commons.test.ServiceFacadeContractTest;

/**
 * Classe de Test permettant de valider une implementation du IRequestService.
 * 
 * @author ginguene
 * 
 */
public abstract class RequestServiceContractTest extends ServiceFacadeContractTest {

    /**
     * @see fr.afnic.commons.test.ServiceContractTest#getSubContractTests()
     */
    @Override
    public Class<?>[] getSubContractTests() {
        return new Class<?>[] { RequestServiceTest.class };
    }

}
