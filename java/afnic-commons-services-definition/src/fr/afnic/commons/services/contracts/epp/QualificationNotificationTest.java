package fr.afnic.commons.services.contracts.epp;

import java.util.List;

import junit.framework.TestCase;

import org.junit.Test;

import fr.afnic.commons.beans.WhoisContact;
import fr.afnic.commons.beans.epp.EppMessage;
import fr.afnic.commons.beans.operations.qualification.Qualification;
import fr.afnic.commons.beans.operations.qualification.QualificationStep;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.commons.test.generator.ContactGenerator;
import fr.afnic.commons.test.generator.QualificationGenerator;
import fr.afnic.commons.test.generator.UserGenerator;

public class QualificationNotificationTest {

    @Test
    public void testQualificationStepWitoutQualificationWithNullStep() throws ServiceException {

        try {
            AppServiceFacade.getEppService().notifyOfQualificationStep(null, new Qualification(UserGenerator.getRootUserId(), TldServiceFacade.Fr), UserGenerator.getRootUserId(), TldServiceFacade.Fr);
            TestCase.fail("No Exception thrown");
        } catch (IllegalArgumentException e) {
            TestCase.assertEquals("step cannot be null.", e.getMessage());
        }

    }

    @Test
    public void testQualificationStepWitoutQualificationWithNullContact() throws ServiceException {

        for (QualificationStep step : QualificationStep.values()) {
            try {
                AppServiceFacade.getEppService().notifyOfQualificationStep(step, null, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
                TestCase.fail("No Exception thrown");
            } catch (IllegalArgumentException e) {
                TestCase.assertEquals("qualification cannot be null.", e.getMessage());
            }
        }
    }

    @Test
    public void testQualificationStepWithQualification() throws ServiceException {
        for (QualificationStep step : QualificationStep.values()) {
            WhoisContact contact = ContactGenerator.createCorporateEntityContact();
            Qualification qualification = QualificationGenerator.createQualification(contact.getHandle());
            AppServiceFacade.getEppService().notifyOfQualificationStep(step, qualification, UserGenerator.getRootUserId(), TldServiceFacade.Fr);

            qualification = AppServiceFacade.getQualificationService().getQualification(qualification.getId(), UserGenerator.getRootUserId(), TldServiceFacade.Fr);
            List<EppMessage> eppMessages = AppServiceFacade.getEppService().getEppMessages(qualification.getHolderSnapshotId(), UserGenerator.getRootUserId(), TldServiceFacade.Fr);
            TestCase.assertNotNull(eppMessages);
            TestCase.assertEquals(1, eppMessages.size());
            TestCase.assertEquals(step.getEppType(), eppMessages.get(0).getType());

        }
    }
}
