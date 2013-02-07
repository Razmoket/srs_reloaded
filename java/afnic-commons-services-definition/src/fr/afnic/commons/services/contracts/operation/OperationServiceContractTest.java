package fr.afnic.commons.services.contracts.operation;

import fr.afnic.commons.test.ServiceFacadeContractTest;

public abstract class OperationServiceContractTest extends ServiceFacadeContractTest {

    @Override
    public Class<?>[] getSubContractTests() {
        return new Class<?>[] { OperationServiceCreateAndGetTest.class,
                                OperationServiceLockTest.class,
                                OperationServiceUpdateTest.class,
                                OperationServiceAttachTest.class,
                                OperationServiceCreateStatusUpdateTest.class };
    }

}
