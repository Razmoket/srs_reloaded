package fr.afnic.commons.services.contracts.legal;

import junit.framework.TestCase;

import org.junit.Test;

import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.commons.test.generator.UserGenerator;

public class LegalServiceCreateOrUpdateAgtfTest {

    @Test
    public void createOrUpdateAgtfWithNullNdd() throws ServiceException {
        try {
            AppServiceFacade.getLegalService().createOrUpdateAgtfState(null, 5, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
            TestCase.fail("No Exception thrown.");
        } catch (IllegalArgumentException e) {
            TestCase.assertEquals("pNdd cannot be null.", e.getMessage());
        }
    }

    @Test
    public void createOrUpdateAgtfWithEmptyNdd() throws ServiceException {
        try {
            AppServiceFacade.getLegalService().createOrUpdateAgtfState("", 5, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
            TestCase.fail("No Exception thrown.");
        } catch (IllegalArgumentException e) {
            TestCase.assertEquals("pNdd cannot be empty.", e.getMessage());
        }
    }

    @Test
    public void createOrUpdateAgtfWithInvalidState() throws ServiceException {
        try {
            AppServiceFacade.getLegalService().createOrUpdateAgtfState("testfr.fr", 5, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
            TestCase.fail("No Exception thrown.");
        } catch (IllegalArgumentException e) {
            TestCase.assertEquals("pState must be a defined value in legal database.", e.getMessage());
        }
    }

    @Test
    public void createOrUpdateAgtfWithInvalidState2() throws ServiceException {
        try {
            AppServiceFacade.getLegalService().createOrUpdateAgtfState("testfr.fr", -1, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
            TestCase.fail("No Exception thrown.");
        } catch (IllegalArgumentException e) {
            TestCase.assertEquals("pState must be a defined value in legal database.", e.getMessage());
        }
    }

    @Test
    public void createOrUpdateAgtfaWithInvalidNdd() throws ServiceException {
        try {
            AppServiceFacade.getLegalService().createOrUpdateAgtfState("toto2", 5, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
            TestCase.fail("No Exception thrown.");
        } catch (IllegalArgumentException e) {
            TestCase.assertEquals("pNdd must be a well formed domain name.", e.getMessage());
        }
    }

    @Test
    public void createOrUpdateAgtfWithValidParam() throws ServiceException {
        AppServiceFacade.getLegalService().createOrUpdateAgtfState("toto2.fr", 1, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        AppServiceFacade.getLegalService().createOrUpdateAgtfState("toto2.fr", 0, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
    }
}
