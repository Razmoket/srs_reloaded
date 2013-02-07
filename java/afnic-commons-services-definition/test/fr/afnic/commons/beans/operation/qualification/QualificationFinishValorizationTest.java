package fr.afnic.commons.beans.operation.qualification;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import fr.afnic.commons.beans.CorporateEntityWhoisContact;
import fr.afnic.commons.beans.WhoisContact;
import fr.afnic.commons.beans.boarequest.TopLevelOperationStatus;
import fr.afnic.commons.beans.contactdetails.EmailAddress;
import fr.afnic.commons.beans.operations.OperationStatus;
import fr.afnic.commons.beans.operations.qualification.PublicQualificationItemStatus;
import fr.afnic.commons.beans.operations.qualification.Qualification;
import fr.afnic.commons.beans.operations.qualification.QualificationOperationType;
import fr.afnic.commons.beans.operations.qualification.QualificationSource;
import fr.afnic.commons.beans.operations.qualification.ReachStatus;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.facade.MockAppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.commons.test.generator.ContactGenerator;
import fr.afnic.commons.test.generator.DomainGenerator;
import fr.afnic.commons.test.generator.UserGenerator;
import fr.afnic.commons.test.generator.exception.GeneratorException;

public class QualificationFinishValorizationTest {

    @Before
    public void initFacade() throws ServiceException {
        MockAppServiceFacade sf = new MockAppServiceFacade();
        sf.use();
    }

    @Test
    public void testWithSourceAutoAndNoReachAndNoEligibility() throws ServiceException, GeneratorException {

        CorporateEntityWhoisContact contact = ContactGenerator.createCorporateEntityContact();

        Qualification qualification = new Qualification(UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        qualification.setHolderNicHandle(contact.getHandle());
        qualification.setSource(QualificationSource.Auto);

        qualification = AppServiceFacade.getQualificationService().createAndGet(qualification, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        qualification = qualification.execute(QualificationOperationType.FinishValorization, null, null);

        qualification = AppServiceFacade.getQualificationService().getQualification(qualification.getId(), UserGenerator.getRootUserId(), TldServiceFacade.Fr);

        TestCase.assertEquals("Bad qualification status", OperationStatus.Succed, qualification.getStatus());
        TestCase.assertEquals("Bad qualification top level status", TopLevelOperationStatus.Finished, qualification.getTopLevelStatus());

        WhoisContact gettedContact = AppServiceFacade.getWhoisContactService().getContactWithHandle(contact.getHandle(), UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        TestCase.assertEquals(PublicQualificationItemStatus.NotIdentified, gettedContact.getReachStatus());
        TestCase.assertEquals(PublicQualificationItemStatus.NotIdentified, gettedContact.getEligibilityStatus());
        TestCase.assertNull(gettedContact.getReachMedia());

    }

    @Test
    public void testWithSourceAutoAndReachAndNoEligibility() throws ServiceException, GeneratorException {

        CorporateEntityWhoisContact contact = ContactGenerator.createCorporateEntityContact();

        Qualification qualification = new Qualification(UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        qualification.setHolderNicHandle(contact.getHandle());
        qualification.setSource(QualificationSource.Auto);
        qualification.setReachStatus(ReachStatus.Email);

        qualification = AppServiceFacade.getQualificationService().createAndGet(qualification, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        qualification = qualification.execute(QualificationOperationType.FinishValorization, null, null);

        qualification = AppServiceFacade.getQualificationService().getQualification(qualification.getId(), UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        TestCase.assertEquals("Bad qualification status", OperationStatus.Succed, qualification.getStatus());
        TestCase.assertEquals("Bad qualification top level status", TopLevelOperationStatus.Finished, qualification.getTopLevelStatus());
    }

    @Test
    public void testWithSourceReportingAndNoReachAndNoEligibility() throws ServiceException, GeneratorException {

        CorporateEntityWhoisContact contact = ContactGenerator.createCorporateEntityContact();
        DomainGenerator.createNewDomainWithHolder("finish-level-one", contact);

        Qualification qualification = new Qualification(UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        qualification.setHolderNicHandle(contact.getHandle());
        qualification.setSource(QualificationSource.Reporting);
        qualification.setReachStatus(ReachStatus.Email);
        qualification.setInitiatorEmailAddress(new EmailAddress("ginguene+test@nic.fr"));

        qualification = AppServiceFacade.getQualificationService().createAndGet(qualification, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        qualification = qualification.execute(QualificationOperationType.FinishValorization, null, null);

        qualification = AppServiceFacade.getQualificationService().getQualification(qualification.getId(), UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        TestCase.assertEquals("Bad qualification status", OperationStatus.Succed, qualification.getStatus());
        TestCase.assertEquals("Bad qualification top level status", TopLevelOperationStatus.PendingResponse, qualification.getTopLevelStatus());

    }
}
