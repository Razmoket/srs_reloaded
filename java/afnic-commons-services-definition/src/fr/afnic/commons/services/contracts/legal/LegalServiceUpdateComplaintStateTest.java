package fr.afnic.commons.services.contracts.legal;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Test;

import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.commons.test.generator.UserGenerator;

public class LegalServiceUpdateComplaintStateTest {

    @Test
    public void updateComplaintStateWithNullNumDossier() throws ServiceException {
        try {
            AppServiceFacade.getLegalService().updateComplaintState(null, 5, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
            TestCase.fail("No Exception thrown.");
        } catch (IllegalArgumentException e) {
            TestCase.assertEquals("pNumDossier cannot be null.", e.getMessage());
        }
    }

    @Test
    public void updateComplaintStateWithEmptyNdd() throws ServiceException {
        try {
            AppServiceFacade.getLegalService().updateComplaintState("", 5, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
            TestCase.fail("No Exception thrown.");
        } catch (IllegalArgumentException e) {
            TestCase.assertEquals("pNumDossier cannot be empty.", e.getMessage());
        }
    }

    @Test
    public void updateComplaintStateWithInvalidState() throws ServiceException {
        try {
            AppServiceFacade.getLegalService().updateComplaintState("FR-2011-000", 25, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
            TestCase.fail("No Exception thrown.");
        } catch (IllegalArgumentException e) {
            TestCase.assertEquals("pState must be a defined value in legal database.", e.getMessage());
        }
    }

    @Test
    public void updateComplaintStateWithInvalidNumDossier() throws ServiceException {
        try {
            AppServiceFacade.getLegalService().updateComplaintState("UK-2011-000", 4, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
            TestCase.fail("No Exception thrown.");
        } catch (IllegalArgumentException e) {
            TestCase.assertEquals("pNumDossier must be a defined value in legal database.", e.getMessage());
        }
    }

    @Test
    public void updateComplaintStateWithValidData() throws ServiceException {
        String vNumDossier = "FR00001";
        //String vNumDossier = "FR-2011-000";

        try {
            AppServiceFacade.getLegalService().updateComplaintState(vNumDossier, 4, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        } catch (ServiceException e) {
            Assert.fail("exception raised : " + e.getMessage());
        }
    }
}
