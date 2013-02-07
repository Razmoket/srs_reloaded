package fr.afnic.commons.services.contracts.domain;

import fr.afnic.commons.test.ServiceFacadeContractTest;

public abstract class DomainServiceContractTest extends ServiceFacadeContractTest {

    @Override
    public Class<?>[] getSubContractTests() {
        return new Class<?>[] { DomainServiceGetDomainWithNameTest.class,
                               DomainServiceCreateDomainTest.class,
                               DomainServiceDelete.class,
                               DomainServiceReservedDomains.class };
    }

}
