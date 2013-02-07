package fr.afnic.commons.services.contracts.qualification;

import fr.afnic.commons.test.ServiceFacadeContractTest;

public abstract class QualificationServiceContractTest extends ServiceFacadeContractTest {

    @Override
    public Class<?>[] getSubContractTests() {
        return new Class<?>[] { QualificationServiceCreateAndGetTest.class,
                               QualificationServiceUpdateTest.class,
                               QualificationServiceSnapshotTest.class,
                               QualificationServiceGetViewsTest.class
        };
    }
}
