/*
 * $Id: $
 * $Revision: $
 */

package fr.afnic.commons.services.contracts.domain;

import junit.framework.Assert;

import org.junit.Ignore;
import org.junit.Test;

import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.commons.test.generator.UserGenerator;

public class DomainServiceReservedDomains {

    @Test
    public void testGetReservedDomainNameMotivationWithReserved() throws Exception {
        Assert.assertEquals("Bad reservation description", "Terme fondamental [Etat]",
                            AppServiceFacade.getDomainService().getReservedDomainNameMotivation("pompier.fr", UserGenerator.getRootUserId(), TldServiceFacade.Fr).getDescription());
        Assert.assertEquals("Bad reservation description", "Terme fondamental [Commune -Nom Reserve-]",
                            AppServiceFacade.getDomainService().getReservedDomainNameMotivation("paris.fr", UserGenerator.getRootUserId(), TldServiceFacade.Fr).getDescription());
        Assert.assertEquals("Bad reservation description", "Terme fondamental [Infractions]",
                            AppServiceFacade.getDomainService().getReservedDomainNameMotivation("meurtre.fr", UserGenerator.getRootUserId(), TldServiceFacade.Fr).getDescription());
        Assert.assertEquals("Bad reservation description", "Domaine spécial [.gouv.fr]",
                            AppServiceFacade.getDomainService().getReservedDomainNameMotivation("toto.gouv.fr", UserGenerator.getRootUserId(), TldServiceFacade.Fr).getDescription());
    }

    @Test
    @Ignore("ne retourne pas null")
    public void testGetReservedDomainNameMotivationWithNonReserved() throws Exception {
        System.out.println(AppServiceFacade.getDomainService().getReservedDomainNameMotivation("toto.fr", UserGenerator.getRootUserId(), TldServiceFacade.Fr));
        Assert.assertNull("Bad reservation description", AppServiceFacade.getDomainService().getReservedDomainNameMotivation("toto.fr", UserGenerator.getRootUserId(), TldServiceFacade.Fr));
    }
}
