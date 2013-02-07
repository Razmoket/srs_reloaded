package fr.afnic.commons.services.contracts.legal;

import junit.framework.TestCase;

import org.junit.Test;

import fr.afnic.commons.legal.SyreliLegalData;
import fr.afnic.commons.services.exception.NotFoundException;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.commons.test.generator.DomainGenerator;
import fr.afnic.commons.test.generator.UserGenerator;
import fr.afnic.commons.test.generator.exception.GeneratorException;

public class LegalServiceGetComplainTest {

    @Test
    public void getComplaintDataWithNullNdd() throws ServiceException {

    }

    @Test
    public void getComplaintDataWithNullNumDossier() throws ServiceException {
        try {
            AppServiceFacade.getLegalService().getComplaintData("afnic.fr", null, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
            TestCase.fail("No Exception thrown.");
        } catch (IllegalArgumentException e) {
            TestCase.assertEquals("pNumDossier cannot be null.", e.getMessage());
        }
    }

    @Test
    public void getComplaintDataWithEmptyNdd() throws ServiceException {
        try {
            AppServiceFacade.getLegalService().getComplaintData("", "test", UserGenerator.getRootUserId(), TldServiceFacade.Fr);
            TestCase.fail("No Exception thrown.");
        } catch (IllegalArgumentException e) {
            TestCase.assertEquals("pNdd cannot be empty.", e.getMessage());
        }
    }

    @Test
    public void getComplaintDataWithEmptyNumDossier() throws ServiceException {
        try {
            AppServiceFacade.getLegalService().getComplaintData("afnic.fr", "", UserGenerator.getRootUserId(), TldServiceFacade.Fr);
            TestCase.fail("No Exception thrown.");
        } catch (IllegalArgumentException e) {
            TestCase.assertEquals("pNumDossier cannot be empty.", e.getMessage());
        }
    }

    @Test
    public void getComplaintDataWithComplaintNotFound() throws ServiceException {
        try {
            AppServiceFacade.getLegalService().getComplaintData("test.fr", "toto", UserGenerator.getRootUserId(), TldServiceFacade.Fr);
            TestCase.fail("No Exception thrown.");
        } catch (NotFoundException e) {
            TestCase.assertEquals("No complaint data found with Ndd[test.fr] and file number[toto].", e.getMessage());
        }
    }

    @Test
    public void getSyreliComplaintDataWithValidData() throws ServiceException {
        String vNdd = null;
        vNdd = "accrediweb.fr";
        String vNumDossier = "FR00009";
        SyreliLegalData vLegalData = AppServiceFacade.getLegalService().getSyreliComplaintData(vNdd, vNumDossier, UserGenerator.getRootUserId(), TldServiceFacade.Fr);

        TestCase.assertNotNull(vLegalData);
        TestCase.assertEquals(vLegalData.getDomainName(), vNdd);
        TestCase.assertEquals(vLegalData.getNumDossier(), vNumDossier);
        //TestCase.assertNotNull(vLegalData.getDomain());
    }

    @Test
    public void getComplaintDataWithValidData() throws ServiceException {
        String vNdd = null;
        try {
            vNdd = DomainGenerator.createNewDomain("testfr");
        } catch (GeneratorException e) {
            e.printStackTrace();
            TestCase.fail(e.getMessage());
        }
        String vNumDossier = "FR-2011-00018";
        try {
            AppServiceFacade.getLegalService().getComplaintData(vNdd, vNumDossier, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        } catch (NotFoundException e) {
            TestCase.assertEquals("No complaint data found with Ndd[" + vNdd + "] and file number[FR-2011-00018].", e.getMessage());
        }
        /*
                TestCase.assertNotNull(vLegalData);
                TestCase.assertEquals(vLegalData.getDomain().getName(), vNdd);
                TestCase.assertEquals(vLegalData.getSyreliLegalData().getNumDossier(), vNumDossier);*/
        //TestCase.assertNotNull(vLegalData.getDomain());
    }
}
