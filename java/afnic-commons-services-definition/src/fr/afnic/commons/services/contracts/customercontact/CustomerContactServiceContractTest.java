package fr.afnic.commons.services.contracts.customercontact;

import fr.afnic.commons.test.ServiceFacadeContractTest;

public abstract class CustomerContactServiceContractTest extends ServiceFacadeContractTest {

    @Override
    public Class<?>[] getSubContractTests() {
        return new Class<?>[] {
                               CustomerContactServiceCreateTest.class,
                               CustomerContactServiceUpdateTest.class };
    }

}
