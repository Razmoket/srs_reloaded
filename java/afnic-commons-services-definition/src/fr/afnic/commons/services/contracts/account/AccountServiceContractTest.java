package fr.afnic.commons.services.contracts.account;

import fr.afnic.commons.test.ServiceFacadeContractTest;

public abstract class AccountServiceContractTest extends ServiceFacadeContractTest {

    @Override
    public Class<?>[] getSubContractTests() {
        return new Class<?>[] { AccountServiceGetUserContractTest.class, AccountServiceAddUserContractTest.class,
                AccountServiceRemoveUserContractTest.class, AccountServiceKnownUserContractTest.class, };
    }

}
