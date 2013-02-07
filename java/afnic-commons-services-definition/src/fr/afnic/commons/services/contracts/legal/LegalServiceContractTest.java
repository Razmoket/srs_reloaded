package fr.afnic.commons.services.contracts.legal;

import fr.afnic.commons.test.ServiceFacadeContractTest;

public abstract class LegalServiceContractTest extends ServiceFacadeContractTest {

    @Override
    public Class<?>[] getSubContractTests() {
        return new Class<?>[] { LegalServiceCheckTest.class,
                               LegalServiceCreateOrUpdateAgtfTest.class,
                               LegalServiceGetComplainTest.class,
                               LegalServiceGetSyreliListTest.class,
                               LegalServiceUpdateComplaintStateTest.class,
                               LegalServiceGetSyreliListTest.class
        };
    }
}
