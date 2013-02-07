package fr.afnic.commons.services.contracts.whoiscontact;

import fr.afnic.commons.test.ServiceFacadeContractTest;

public abstract class ReadOnlyWhoisContactServiceContractTest extends ServiceFacadeContractTest {

    @Override
    public Class<?>[] getSubContractTests() {
        return new Class<?>[] {};
    }

}
