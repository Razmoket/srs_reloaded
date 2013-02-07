package fr.afnic.commons.services.contracts.legal;

import junit.framework.TestCase;

import org.junit.Test;

import fr.afnic.commons.services.exception.NotFoundException;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.commons.test.generator.UserGenerator;

public class LegalServiceCheckTest {

    @Test
    public void checkExistingLegalStateWithValidParam() throws ServiceException {
        AppServiceFacade.getLegalService().checkExistingLegalState(4, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
    }

    @Test
    public void checkExistingNumDossierWithNullNdd() throws ServiceException {
        try {
            AppServiceFacade.getLegalService().checkExistingNumDossier(null, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
            TestCase.fail("No Exception thrown.");
        } catch (IllegalArgumentException e) {
            TestCase.assertEquals("pNumDossier cannot be null.", e.getMessage());
        }
    }

    @Test
    public void checkExistingNumDossierWithEmptyNdd() throws ServiceException {
        try {
            AppServiceFacade.getLegalService().checkExistingNumDossier("", UserGenerator.getRootUserId(), TldServiceFacade.Fr);
            TestCase.fail("No Exception thrown.");
        } catch (IllegalArgumentException e) {
            TestCase.assertEquals("pNumDossier cannot be empty.", e.getMessage());
        }
    }

    @Test
    public void checkExistingNumDossierWithValidParam() throws ServiceException {
        AppServiceFacade.getLegalService().checkExistingNumDossier("testfr.fr", UserGenerator.getRootUserId(), TldServiceFacade.Fr);
    }

    @Test
    public void checkExistingSyreliForDomainNameWithNullNdd() throws ServiceException {
        try {
            AppServiceFacade.getLegalService().checkExistingSyreliForDomainName(null, "AF-2011-005", UserGenerator.getRootUserId(), TldServiceFacade.Fr);
            TestCase.fail("No Exception thrown.");
        } catch (IllegalArgumentException e) {
            TestCase.assertEquals("pNdd cannot be null.", e.getMessage());
        }
    }

    @Test
    public void checkExistingSyreliForDomainNameWithEmptyNdd() throws ServiceException {
        try {
            AppServiceFacade.getLegalService().checkExistingSyreliForDomainName("", "AF-2011-005", UserGenerator.getRootUserId(), TldServiceFacade.Fr);
            TestCase.fail("No Exception thrown.");
        } catch (IllegalArgumentException e) {
            TestCase.assertEquals("pNdd cannot be empty.", e.getMessage());
        }
    }

    @Test
    public void checkExistingSyreliForDomainNameWithNullNumDossier() throws ServiceException {
        try {
            AppServiceFacade.getLegalService().checkExistingSyreliForDomainName("testfr.fr", null, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
            TestCase.fail("No Exception thrown.");
        } catch (IllegalArgumentException e) {
            TestCase.assertEquals("pNumDossier cannot be null.", e.getMessage());
        }
    }

    @Test
    public void checkExistingSyreliForDomainNameWithEmptyNumDossier() throws ServiceException {
        try {
            AppServiceFacade.getLegalService().checkExistingSyreliForDomainName("testfr.fr", "", UserGenerator.getRootUserId(), TldServiceFacade.Fr);
            TestCase.fail("No Exception thrown.");
        } catch (IllegalArgumentException e) {
            TestCase.assertEquals("pNumDossier cannot be empty.", e.getMessage());
        }
    }

    @Test
    public void checkExistingSyreliForDomainNameWithIncoherentParam() throws ServiceException {
        try {
            AppServiceFacade.getLegalService().checkExistingSyreliForDomainName("testfr.fr", "AF-2011-005", UserGenerator.getRootUserId(), TldServiceFacade.Fr);
            TestCase.fail("No Exception thrown.");
        } catch (NotFoundException e) {
            TestCase.assertEquals("No complaint data found with Ndd[" + "testfr.fr" + "] and file number[" + "AF-2011-005" + "].", e.getMessage());
        }
    }
}
