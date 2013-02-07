package fr.afnic.commons.services.contracts.domain;

import org.junit.Test;

import fr.afnic.commons.services.exception.NotFoundException;
import fr.afnic.commons.services.exception.invalidformat.InvalidDomainNameException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.commons.test.generator.UserGenerator;

/**
 * Valide le comportement de la méthode getDomainWithName() d'une implémentation du service IDomaintService.
 * 
 * @author ginguene
 * 
 */
public class DomainServiceGetDomainWithNameTest {

    @Test(expected = InvalidDomainNameException.class)
    public void testGetDomainWithNameWithNullName() throws Exception {
        AppServiceFacade.getDomainService().getDomainWithName(null, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
    }

    @Test(expected = InvalidDomainNameException.class)
    public void testGetDomainWithNameWithInvalidName() throws Exception {
        AppServiceFacade.getDomainService().getDomainWithName("invalid-domain-name", UserGenerator.getRootUserId(), TldServiceFacade.Fr);
    }

    @Test(expected = NotFoundException.class)
    public void testGetDomainWithNameWithUnknownName() throws Exception {
        AppServiceFacade.getDomainService().getDomainWithName("un-domain-qui-n-existe-pas.fr", UserGenerator.getRootUserId(), TldServiceFacade.Fr);
    }

}
