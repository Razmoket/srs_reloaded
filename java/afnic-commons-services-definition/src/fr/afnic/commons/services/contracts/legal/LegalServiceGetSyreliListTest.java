package fr.afnic.commons.services.contracts.legal;

import junit.framework.TestCase;

import org.junit.Test;

import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.commons.test.generator.UserGenerator;

public class LegalServiceGetSyreliListTest {

    @Test
    public void getSyreliListWithNullNdd() throws ServiceException {
        try {
            AppServiceFacade.getLegalService().getListSyreliForDomainName(null, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
            TestCase.fail("No Exception thrown.");
        } catch (IllegalArgumentException e) {
            TestCase.assertEquals("pNdd cannot be null.", e.getMessage());
        }
    }

    @Test
    public void getSyreliListWithEmptyNdd() throws ServiceException {
        try {
            AppServiceFacade.getLegalService().getListSyreliForDomainName("", UserGenerator.getRootUserId(), TldServiceFacade.Fr);
            TestCase.fail("No Exception thrown.");
        } catch (IllegalArgumentException e) {
            TestCase.assertEquals("pNdd cannot be empty.", e.getMessage());
        }
    }

    @Test
    public void getSyreliListWithValidParam() throws ServiceException {
        AppServiceFacade.getLegalService().getListSyreliForDomainName("testfr.fr", UserGenerator.getRootUserId(), TldServiceFacade.Fr);
    }
}
