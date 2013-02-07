package fr.afnic.commons.services.contracts.resultlist;

import fr.afnic.commons.test.ServiceFacadeContractTest;

public abstract class ResultListServiceContractTest extends ServiceFacadeContractTest {

    @Override
    public Class<?>[] getSubContractTests() {
        return new Class<?>[] { //ResultListServiceGetCountTest.class,
        ResultListServiceGetTest.class };
    }

}
