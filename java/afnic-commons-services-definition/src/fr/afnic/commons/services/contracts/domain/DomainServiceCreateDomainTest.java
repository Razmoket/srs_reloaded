/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.services.contracts.domain;

import junit.framework.TestCase;

import org.junit.Test;

import fr.afnic.commons.beans.WhoisContact;
import fr.afnic.commons.beans.domain.Domain;
import fr.afnic.commons.services.exception.invalidformat.InvalidDomainNameException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.commons.test.generator.ContactGenerator;
import fr.afnic.commons.test.generator.DomainGenerator;
import fr.afnic.commons.test.generator.UserGenerator;

/**
 * Valide le comportement de la méthode createDomain() d'une implémentation du service IDomaintService.
 * 
 * @author ginguene
 * 
 */
public class DomainServiceCreateDomainTest {

    @Test(expected = InvalidDomainNameException.class)
    public void testCreateDomainWithNullDomainName() throws Exception {
        WhoisContact holder = ContactGenerator.createIndividualContact();
        WhoisContact adminContact = ContactGenerator.createCorporateEntityContact();
        WhoisContact technicalContact = ContactGenerator.createCorporateEntityContact();
        AppServiceFacade.getDomainService().createDomain(null, "authinfo", holder.getRegistrarCode(), holder, adminContact, technicalContact, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
    }

    @Test(expected = InvalidDomainNameException.class)
    public void testCreateDomainWithDomainNameWithBadFormat() throws Exception {
        WhoisContact holder = ContactGenerator.createIndividualContact();
        WhoisContact adminContact = ContactGenerator.createCorporateEntityContact();
        WhoisContact technicalContact = ContactGenerator.createCorporateEntityContact();
        AppServiceFacade.getDomainService().createDomain("pouet***", "authinfo", holder.getRegistrarCode(), holder, adminContact, technicalContact, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
    }

    @Test
    public void testCreateDomain() throws Exception {
        // TODO rajouter des controle admin est null, si tech est null, si tech est un pp
        WhoisContact holder = ContactGenerator.createIndividualContact();
        WhoisContact admin = ContactGenerator.createIndividualContact();
        WhoisContact tech = ContactGenerator.createCorporateEntityContact();
        String domainName = DomainGenerator.getUnusedDomainName();

        Domain domain = AppServiceFacade.getDomainService().createDomain(domainName, "authinfo", holder.getRegistrarCode(), holder, admin, tech, UserGenerator.getRootUserId(), TldServiceFacade.Fr);

        // TestCase.assertTrue(domain.isOnHold());
        TestCase.assertEquals("Bad domain name", domainName, domain.getName());
        TestCase.assertEquals("Bad domain registrar code", holder.getRegistrarCode(), domain.getRegistrarCode());
        TestCase.assertEquals("Bad domain holder handle", holder.getHandle(), domain.getHolderHandle());
    }
}
