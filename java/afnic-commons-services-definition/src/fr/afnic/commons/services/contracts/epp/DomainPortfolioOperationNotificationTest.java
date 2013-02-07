package fr.afnic.commons.services.contracts.epp;

import junit.framework.TestCase;

import org.junit.Test;

import fr.afnic.commons.beans.WhoisContact;
import fr.afnic.commons.beans.operations.qualification.PortfolioStatus;
import fr.afnic.commons.beans.operations.qualification.Qualification;
import fr.afnic.commons.beans.operations.qualification.operation.DomainPortfolioOperationType;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.commons.test.generator.ContactGenerator;
import fr.afnic.commons.test.generator.DomainGenerator;
import fr.afnic.commons.test.generator.QualificationGenerator;
import fr.afnic.commons.test.generator.UserGenerator;

public class DomainPortfolioOperationNotificationTest {

    @Test
    public void notifyOfDomainPortfolioOperationWithIndividual() throws Exception {
        for (DomainPortfolioOperationType type : DomainPortfolioOperationType.values()) {
            String handle = ContactGenerator.createIndividualContact().getHandle();
            AppServiceFacade.getEppService().notifyOfDomainPortfolioOperation(type, handle, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
            //Holder Contact is not an Organization 
        }
    }

    @Test
    public void notifyOfDomainPortfolioOperationWithCorporateEntityWithoutDomains() throws Exception {
        for (DomainPortfolioOperationType type : DomainPortfolioOperationType.values()) {
            String handle = ContactGenerator.createCorporateEntityContact().getHandle();
            AppServiceFacade.getEppService().notifyOfDomainPortfolioOperation(type, handle, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        }
    }

    @Test
    public void notifyOfDomainPortfolioOperationWithOneDomainWithGoodStatus() throws Exception {
        for (DomainPortfolioOperationType type : DomainPortfolioOperationType.values()) {

            WhoisContact holder = ContactGenerator.createCorporateEntityContact();
            QualificationGenerator.createQualification(holder.getHandle());
            DomainGenerator.createNewDomainWithHolder("test-notify-good-status", holder);

            Qualification qualification = holder.getQualificationInProgress();
            qualification.setPortfolioStatus(this.getExpectedPortfolioStatusToNotify(type));
            AppServiceFacade.getQualificationService().updatePortfolioStatus(qualification, UserGenerator.getRootUserId(), TldServiceFacade.Fr);

            AppServiceFacade.getEppService().notifyOfDomainPortfolioOperation(type, holder.getHandle(), UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        }
    }

    @Test
    public void notifyOfDomainPortfolioOperationWithTwoDomains() throws Exception {
        for (DomainPortfolioOperationType type : DomainPortfolioOperationType.values()) {
            if (type != DomainPortfolioOperationType.Suppress) {
                WhoisContact holder = ContactGenerator.createCorporateEntityContact();
                QualificationGenerator.createQualification(holder.getHandle());
                DomainGenerator.createNewDomainWithHolder("test-notify-two-domains-1", holder);
                DomainGenerator.createNewDomainWithHolder("test-notify-two-domains-2", holder);

                Qualification qualification = holder.getQualificationInProgress();
                qualification.setPortfolioStatus(this.getExpectedPortfolioStatusToNotify(type));
                AppServiceFacade.getQualificationService().updatePortfolioStatus(qualification, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
                AppServiceFacade.getEppService().notifyOfDomainPortfolioOperation(type, holder.getHandle(), UserGenerator.getRootUserId(), TldServiceFacade.Fr);
            }
        }
    }

    public PortfolioStatus getExpectedPortfolioStatusToNotify(DomainPortfolioOperationType type) {
        return DomainPortfolioOperationTypeToPortfolioStatusConverter.convert(type);
    }

    @Test
    public void notifyOfDomainPortfolioOperationWithOneDomainWithBadStatus() throws Exception {
        for (DomainPortfolioOperationType type : DomainPortfolioOperationType.values()) {
            if (type != DomainPortfolioOperationType.Unblock && type != DomainPortfolioOperationType.Unfreeze) {

                WhoisContact holder = ContactGenerator.createCorporateEntityContact();
                QualificationGenerator.createQualification(holder.getHandle());
                DomainGenerator.createNewDomainWithHolder("test-notify-bad-status", holder);

                try {
                    AppServiceFacade.getEppService().notifyOfDomainPortfolioOperation(type, holder.getHandle(), UserGenerator.getRootUserId(), TldServiceFacade.Fr);
                    TestCase.fail("No Exception thrown");
                } catch (ServiceException e) {
                    String expectedMessage;
                    if (type == DomainPortfolioOperationType.Suppress) {
                        expectedMessage = "Last qualification request for Contact with handle '" + holder.getHandle() + "' has id portfolio status '1', not '" + this.getTypeAsIntValue(type) + "'.";

                    } else {
                        expectedMessage = "Pending qualification request for Contact with handle '" + holder.getHandle() + "' has portfolio id status '1', not '" + this.getTypeAsIntValue(type) + "'.";

                    }
                    TestCase.assertEquals(expectedMessage, e.getFirstCauseMessage());
                }
            }
        }
    }

    /**
     * Converti le type en type gateway qui est indiqué dans le message d'erreur   
     */
    private int getTypeAsIntValue(DomainPortfolioOperationType type) {
        switch (type) {
        case Suppress:
            return 7;
        case Freeze:
            return 3;
        case Block:
            return 5;
        default:
            throw new IllegalArgumentException("No value defined for " + type);
        }
    }

}
