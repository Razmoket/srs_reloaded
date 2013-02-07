package fr.afnic.commons.services.contracts.legal;

import junit.framework.TestCase;

import org.junit.Test;

import fr.afnic.commons.beans.IndividualWhoisContact;
import fr.afnic.commons.beans.WhoisContact;
import fr.afnic.commons.beans.domain.Domain;
import fr.afnic.commons.services.exception.NotFoundException;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.commons.test.generator.DomainGenerator;
import fr.afnic.commons.test.generator.UserGenerator;
import fr.afnic.commons.test.generator.exception.GeneratorException;

public class LegalServiceGetDomainAgtfFrozenListTest {

    @Test
    public void getDomainAgtfFrozenListWithNullNdd() throws ServiceException {
        try {
            AppServiceFacade.getLegalService().getDomainAgtfFrozenList(null, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
            TestCase.fail("No Exception thrown.");
        } catch (IllegalArgumentException e) {
            TestCase.assertEquals("pWhoisContact cannot be null.", e.getMessage());
        }
    }

    @Test
    public void getDomainAgtfFrozenListWithEmptyNdd() throws ServiceException {
        try {
            AppServiceFacade.getLegalService().getDomainAgtfFrozenList(new IndividualWhoisContact(UserGenerator.getRootUserId(), TldServiceFacade.Fr), UserGenerator.getRootUserId(),
                                                                       TldServiceFacade.Fr);
            TestCase.fail("No Exception thrown.");
        } catch (IllegalArgumentException e) {
            TestCase.assertEquals("pWhoisContact must have a valid handle.", e.getMessage());
        }
    }

    @Test
    public void getDomainAgtfFrozenListWithValidParam() throws ServiceException {
        String vNdd = null;
        WhoisContact vWhoisContact = new IndividualWhoisContact(UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        try {
            vNdd = DomainGenerator.createNewDomain("testfr");
        } catch (GeneratorException e) {
            e.printStackTrace();
            TestCase.fail(e.getMessage());
        }
        try {
            Domain d = AppServiceFacade.getDomainService().getDomainWithName(vNdd, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
            vWhoisContact.setHandle(d.getHolderHandle());
            DomainGenerator.createNewDomainWithHolder("testfr", vWhoisContact);
        } catch (NotFoundException e) {
            e.printStackTrace();
            TestCase.fail(e.getMessage());
        } catch (GeneratorException e) {
            e.printStackTrace();
            TestCase.fail(e.getMessage());
        }
        AppServiceFacade.getLegalService().getDomainAgtfFrozenList(vWhoisContact, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
    }
}
