/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.services.contracts.whoiscontact;

import fr.afnic.commons.test.ServiceFacadeContractTest;

/**
 * Classe de Test permettant de valider une implementation du IContactService.
 * Surtout pour implémentation GW.
 * 
 * @author ginguene
 * 
 */
public abstract class WhoisContactServiceContractTest extends ServiceFacadeContractTest {

    @Override
    public Class<?>[] getSubContractTests() {
        return new Class<?>[] { //ContactServiceCreateContactContractTest.class,
        //ContactServiceGetPublicSnapshotTest.class,
        WhoisContactServiceUpdateTest.class
        };
    }

}
