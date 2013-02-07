/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.services.contracts.postaladdress;

import fr.afnic.commons.test.ServiceFacadeContractTest;

/**
 * Classe de Test permettant de valider une implementation du IContactService.
 * 
 * @author ginguene
 * 
 */
public abstract class PostalAddressServiceContractTest extends ServiceFacadeContractTest {

    @Override
    public Class<?>[] getSubContractTests() {
        return new Class<?>[] { PostalAddressAllTest.class
        };
    }

}
