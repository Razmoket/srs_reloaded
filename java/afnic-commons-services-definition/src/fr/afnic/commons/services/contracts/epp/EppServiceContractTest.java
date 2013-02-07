package fr.afnic.commons.services.contracts.epp;

import fr.afnic.commons.test.ServiceFacadeContractTest;

public abstract class EppServiceContractTest extends ServiceFacadeContractTest {

    @Override
    public Class<?>[] getSubContractTests() {
        return new Class<?>[] { DomainPortfolioOperationNotificationTest.class,
                               QualificationNotificationTest.class
        };
    }

}
