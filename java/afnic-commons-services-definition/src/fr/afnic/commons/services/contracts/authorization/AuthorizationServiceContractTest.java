/*
 * $Id: $
 * $Revision: $
 */

package fr.afnic.commons.services.contracts.authorization;

import fr.afnic.commons.test.ServiceFacadeContractTest;

public abstract class AuthorizationServiceContractTest extends ServiceFacadeContractTest {

    @Override
    public Class<?>[] getSubContractTests() {
        return new Class<?>[] { AuthorizationServicePreliminaryExamContractTest.class, };
    }

}
