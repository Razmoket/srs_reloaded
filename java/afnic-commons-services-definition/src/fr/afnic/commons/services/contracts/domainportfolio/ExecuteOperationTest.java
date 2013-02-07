package fr.afnic.commons.services.contracts.domainportfolio;

import java.util.concurrent.TimeUnit;

import junit.framework.TestCase;

import org.junit.Test;

import fr.afnic.commons.beans.WhoisContact;
import fr.afnic.commons.beans.domain.Domain;
import fr.afnic.commons.beans.domain.DomainStatus;
import fr.afnic.commons.beans.operations.qualification.operation.DomainPortfolioOperationType;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.exception.UncompletedExecutionException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.commons.test.generator.ContactGenerator;
import fr.afnic.commons.test.generator.DomainGenerator;
import fr.afnic.commons.test.generator.UserGenerator;
import fr.afnic.utils.Delay;

public class ExecuteOperationTest {

    @Test
    public void testFreezeDomainPortfolioWithIndividual() throws Exception {
        WhoisContact holder = ContactGenerator.createIndividualContact();
        String domainName = DomainGenerator.createNewDomainWithHolder("test-freeze-domain", holder);

        AppServiceFacade.getDomainPortfolioService().execute(DomainPortfolioOperationType.Freeze, holder.getHandle(), UserGenerator.getRootUserId(), TldServiceFacade.Fr);

        new Delay(2, TimeUnit.SECONDS).sleep();
        Domain domain = AppServiceFacade.getDomainService().getDomainWithName(domainName, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        TestCase.assertEquals(DomainStatus.Frozen, domain.getStatus());
    }

    @Test
    public void testFreezeDomainPortfolioOnRegistredDomain() throws Exception {
        WhoisContact holder = ContactGenerator.createCorporateEntityContact();
        String domainName = DomainGenerator.createNewDomainWithHolder("test-freeze-domain", holder);
        this.assertDomainStatus(domainName, DomainStatus.Registred);

        this.executeOperation(domainName, holder, DomainPortfolioOperationType.Freeze);
        this.assertDomainStatus(domainName, DomainStatus.Frozen);
    }

    @Test
    public void testFreezeDomainPortfolioOnFrozendDomain() throws Exception {
        WhoisContact holder = ContactGenerator.createCorporateEntityContact();
        String domainName = DomainGenerator.createNewDomainWithHolder("test-freeze-domain", holder);
        this.assertDomainStatus(domainName, DomainStatus.Registred);

        this.executeOperation(domainName, holder, DomainPortfolioOperationType.Freeze);
        this.assertDomainStatus(domainName, DomainStatus.Frozen);

        this.assertOperationFail(domainName, holder, DomainPortfolioOperationType.Freeze);
    }

    @Test
    public void testUnfreezeOnFrozenDomain() throws Exception {
        WhoisContact holder = ContactGenerator.createCorporateEntityContact();
        String domainName = DomainGenerator.createNewDomainWithHolder("test-unfreeze-domain", holder);
        this.assertDomainStatus(domainName, DomainStatus.Registred);

        this.executeOperation(domainName, holder, DomainPortfolioOperationType.Freeze);
        this.assertDomainStatus(domainName, DomainStatus.Frozen);

        this.executeOperation(domainName, holder, DomainPortfolioOperationType.Unfreeze);
        this.assertDomainStatus(domainName, DomainStatus.Registred);
    }

    @Test
    public void testUnfreezeOnRegistredDomain() throws Exception {
        WhoisContact holder = ContactGenerator.createCorporateEntityContact();
        String domainName = DomainGenerator.createNewDomainWithHolder("test-unfreeze-domain", holder);
        this.assertDomainStatus(domainName, DomainStatus.Registred);

        this.assertOperationFail(domainName, holder, DomainPortfolioOperationType.Unfreeze);
    }

    @Test
    public void testUnfreezeOnBlockedDomain() throws Exception {
        WhoisContact holder = ContactGenerator.createCorporateEntityContact();
        String domainName = DomainGenerator.createNewDomainWithHolder("test-unfreeze-domain", holder);
        this.assertDomainStatus(domainName, DomainStatus.Registred);

        this.executeOperation(domainName, holder, DomainPortfolioOperationType.Block);
        this.assertDomainStatus(domainName, DomainStatus.Blocked);

        this.assertOperationFail(domainName, holder, DomainPortfolioOperationType.Unfreeze);
    }

    @Test
    public void testBlockOnRegistredDomain() throws Exception {
        WhoisContact holder = ContactGenerator.createCorporateEntityContact();
        String domainName = DomainGenerator.createNewDomainWithHolder("test-block-domain", holder);
        this.assertDomainStatus(domainName, DomainStatus.Registred);

        this.executeOperation(domainName, holder, DomainPortfolioOperationType.Block);
        this.assertDomainStatus(domainName, DomainStatus.Blocked);
    }

    @Test
    public void testBlockOnFrozenDomain() throws Exception {
        WhoisContact holder = ContactGenerator.createCorporateEntityContact();
        String domainName = DomainGenerator.createNewDomainWithHolder("test-block-domain", holder);
        this.assertDomainStatus(domainName, DomainStatus.Registred);

        this.executeOperation(domainName, holder, DomainPortfolioOperationType.Freeze);
        this.assertDomainStatus(domainName, DomainStatus.Frozen);

        this.executeOperation(domainName, holder, DomainPortfolioOperationType.Block);
        this.assertDomainStatus(domainName, DomainStatus.Blocked);
    }

    @Test
    public void testBlockOnBlockedDomain() throws Exception {
        WhoisContact holder = ContactGenerator.createCorporateEntityContact();
        String domainName = DomainGenerator.createNewDomainWithHolder("test-block-domain", holder);
        this.assertDomainStatus(domainName, DomainStatus.Registred);

        this.executeOperation(domainName, holder, DomainPortfolioOperationType.Block);
        this.assertDomainStatus(domainName, DomainStatus.Blocked);

        this.assertOperationFail(domainName, holder, DomainPortfolioOperationType.Block);
    }

    @Test
    public void testUnblockOnBlockedDomain() throws Exception {
        WhoisContact holder = ContactGenerator.createCorporateEntityContact();
        String domainName = DomainGenerator.createNewDomainWithHolder("test-block-domain", holder);
        this.assertDomainStatus(domainName, DomainStatus.Registred);

        this.executeOperation(domainName, holder, DomainPortfolioOperationType.Block);
        this.assertDomainStatus(domainName, DomainStatus.Blocked);

        this.executeOperation(domainName, holder, DomainPortfolioOperationType.Unblock);
        this.assertDomainStatus(domainName, DomainStatus.Registred);
    }

    @Test
    public void testUnblockOnFrozenThenBlockedDomain() throws Exception {
        WhoisContact holder = ContactGenerator.createCorporateEntityContact();
        String domainName = DomainGenerator.createNewDomainWithHolder("test-block-domain", holder);
        this.assertDomainStatus(domainName, DomainStatus.Registred);

        this.executeOperation(domainName, holder, DomainPortfolioOperationType.Freeze);
        this.assertDomainStatus(domainName, DomainStatus.Frozen);

        this.executeOperation(domainName, holder, DomainPortfolioOperationType.Block);
        this.assertDomainStatus(domainName, DomainStatus.Blocked);

        this.executeOperation(domainName, holder, DomainPortfolioOperationType.Unblock);
        this.assertDomainStatus(domainName, DomainStatus.Registred);
    }

    @Test
    public void testUnblockOnRegistrerdDomain() throws Exception {
        WhoisContact holder = ContactGenerator.createCorporateEntityContact();
        String domainName = DomainGenerator.createNewDomainWithHolder("test-block-domain", holder);
        this.assertDomainStatus(domainName, DomainStatus.Registred);

        this.assertOperationFail(domainName, holder, DomainPortfolioOperationType.Unblock);
    }

    @Test
    public void testUnblockOnFrozenDomain() throws Exception {
        WhoisContact holder = ContactGenerator.createCorporateEntityContact();
        String domainName = DomainGenerator.createNewDomainWithHolder("test-block-domain", holder);
        this.assertDomainStatus(domainName, DomainStatus.Registred);

        this.executeOperation(domainName, holder, DomainPortfolioOperationType.Freeze);
        this.assertDomainStatus(domainName, DomainStatus.Frozen);

        this.assertOperationFail(domainName, holder, DomainPortfolioOperationType.Unblock);
    }

    @Test
    public void testSuppressOnRegistredDomain() throws Exception {
        WhoisContact holder = ContactGenerator.createCorporateEntityContact();
        String domainName = DomainGenerator.createNewDomainWithHolder("test-suppress-domain", holder);
        this.assertDomainStatus(domainName, DomainStatus.Registred);

        this.executeOperation(domainName, holder, DomainPortfolioOperationType.Suppress);
        this.assertDomainStatus(domainName, DomainStatus.Deleted);
    }

    @Test
    public void testSuppressOnBlockedDomain() throws Exception {
        WhoisContact holder = ContactGenerator.createCorporateEntityContact();
        String domainName = DomainGenerator.createNewDomainWithHolder("test-suppress-domain", holder);
        this.assertDomainStatus(domainName, DomainStatus.Registred);

        this.executeOperation(domainName, holder, DomainPortfolioOperationType.Block);
        this.assertDomainStatus(domainName, DomainStatus.Blocked);

        this.executeOperation(domainName, holder, DomainPortfolioOperationType.Suppress);
        this.assertDomainStatus(domainName, DomainStatus.Deleted);
    }

    private void executeOperation(String domainName, WhoisContact holder, DomainPortfolioOperationType type) throws ServiceException {
        AppServiceFacade.getDomainPortfolioService().execute(type, holder.getHandle(), UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        new Delay(2, TimeUnit.SECONDS).sleep();
    }

    private void assertOperationFail(String domainName, WhoisContact holder, DomainPortfolioOperationType type) throws ServiceException {

        try {
            this.executeOperation(domainName, holder, type);
            TestCase.fail("No exception thrown");
        } catch (UncompletedExecutionException e) {
            TestCase.assertEquals(0, e.getSucced().size());
            TestCase.assertEquals(1, e.getFailed().size());
            TestCase.assertEquals(domainName, e.getFailed().get(0));
        }
    }

    private void assertDomainStatus(String domainName, DomainStatus expectedStatus) throws ServiceException {
        Domain domain = AppServiceFacade.getDomainService().getDomainWithName(domainName, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        TestCase.assertEquals(expectedStatus, domain.getStatus());

    }

}
