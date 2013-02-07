/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.services.contracts.user;

import fr.afnic.commons.test.ServiceFacadeContractTest;

/**
 * Classe de Test permettant de valider une implementation du IUserService.
 * 
 * @author ginguene
 * 
 */
public abstract class UserServiceContractTest extends ServiceFacadeContractTest {


    @Override
    public Class<?>[] getSubContractTests() {
        return new Class<?>[] { UserServiceTest.class, };

    }
}
