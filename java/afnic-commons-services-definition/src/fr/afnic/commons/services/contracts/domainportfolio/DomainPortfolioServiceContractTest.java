package fr.afnic.commons.services.contracts.domainportfolio;

import fr.afnic.commons.test.ServiceFacadeContractTest;

public abstract class DomainPortfolioServiceContractTest extends ServiceFacadeContractTest {

    @Override
    public Class<?>[] getSubContractTests() {
        return new Class<?>[] { ExecuteOperationTest.class };
    }

}
