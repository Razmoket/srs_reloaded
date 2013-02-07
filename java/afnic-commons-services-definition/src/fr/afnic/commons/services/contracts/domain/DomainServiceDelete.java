/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.services.contracts.domain;

import junit.framework.TestCase;

import org.junit.Test;

import fr.afnic.commons.beans.domain.Domain;
import fr.afnic.commons.beans.domain.DomainStatus;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.commons.test.generator.DomainGenerator;
import fr.afnic.commons.test.generator.UserGenerator;

public class DomainServiceDelete {

    @Test
    public void testDelete() throws Exception {
        String domainName = DomainGenerator.createNewDomain("test-delete");

        AppServiceFacade.getDomainService().deleteDomain(domainName, UserGenerator.ROOT_LOGIN, UserGenerator.getRootUserId(), TldServiceFacade.Fr);

        Domain domain = AppServiceFacade.getDomainService().getDomainWithName(domainName, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        TestCase.assertEquals(DomainStatus.Deleted, domain.getStatus());

    }
}
