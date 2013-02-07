/*
 * $Id: $
 * $Revision: $
 */

package fr.afnic.commons.checkers;

import junit.framework.Assert;

import org.junit.Test;

import fr.afnic.commons.services.exception.ServiceException;

public class SpecialDomainCheckerTest {

    @Test
    public void testWithReservedDomainNameMotivation() throws ServiceException {
        Assert.assertEquals("Bad ReservedDomainNameMotivation", "Domaine spécial [.gouv.fr]", SpecialDomainChecker.getReservedDomainNameMotivation("toto.gouv.fr").getDescription());
        Assert.assertEquals("Bad ReservedDomainNameMotivation", "Domaine spécial [.tm.fr]", SpecialDomainChecker.getReservedDomainNameMotivation("toto.tm.fr").getDescription());
        Assert.assertEquals("Bad ReservedDomainNameMotivation", "Domaine spécial [.asso.fr]", SpecialDomainChecker.getReservedDomainNameMotivation("toto.asso.fr").getDescription());
        Assert.assertEquals("Bad ReservedDomainNameMotivation", "Domaine spécial [cc-*.fr]", SpecialDomainChecker.getReservedDomainNameMotivation("cc-toto.fr").getDescription());
        Assert.assertEquals("Bad ReservedDomainNameMotivation", "Domaine spécial [cg-*.fr]", SpecialDomainChecker.getReservedDomainNameMotivation("cg-toto.fr").getDescription());
        Assert.assertEquals("Bad ReservedDomainNameMotivation", "Domaine spécial [cr-*.fr]", SpecialDomainChecker.getReservedDomainNameMotivation("cr-toto.fr").getDescription());
        Assert.assertEquals("Bad ReservedDomainNameMotivation", "Domaine spécial [mairie-*.fr]", SpecialDomainChecker.getReservedDomainNameMotivation("mairie-toto.fr").getDescription());
        Assert.assertEquals("Bad ReservedDomainNameMotivation", "Domaine spécial [ville-*.fr]", SpecialDomainChecker.getReservedDomainNameMotivation("ville-toto.fr").getDescription());
        Assert.assertEquals("Bad ReservedDomainNameMotivation", "Domaine spécial [agglo-*.fr]", SpecialDomainChecker.getReservedDomainNameMotivation("agglo-toto.fr").getDescription());

    }

    @Test
    public void testWithNonReservedDomainNameMotivation() {
        Assert.assertNull("Bad ReservedDomainNameMotivation", SpecialDomainChecker.getReservedDomainNameMotivation("toto.fr"));
        Assert.assertNull("Bad ReservedDomainNameMotivation", SpecialDomainChecker.getReservedDomainNameMotivation("acc-toto.fr"));
        Assert.assertNull("Bad ReservedDomainNameMotivation", SpecialDomainChecker.getReservedDomainNameMotivation("bmairie-toto.fr"));
        Assert.assertNull("Bad ReservedDomainNameMotivation", SpecialDomainChecker.getReservedDomainNameMotivation("toto-gouv.fr"));
    }

}
