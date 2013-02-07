/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.services.contracts.operationform;

import junit.framework.TestCase;

import org.junit.Test;

import fr.afnic.commons.beans.OperationForm;
import fr.afnic.commons.beans.TradeTicket;
import fr.afnic.commons.beans.validatable.OperationFormId;
import fr.afnic.commons.services.exception.NotFoundException;
import fr.afnic.commons.services.exception.invalidformat.InvalidFormatException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.commons.test.generator.DomainGenerator;
import fr.afnic.commons.test.generator.TradeGenerator;
import fr.afnic.commons.test.generator.UserGenerator;

public class OperationFormCloseOperationFormTest {

    @Test(expected = IllegalArgumentException.class)
    public void testArchiveWithNullId() throws Exception {
        AppServiceFacade.getOperationFormService().archiveOperationForm(null, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
    }

    @Test(expected = InvalidFormatException.class)
    public void testArchiveWithInvalidId() throws Exception {
        AppServiceFacade.getOperationFormService().archiveOperationForm(new OperationFormId(-2), UserGenerator.getRootUserId(), TldServiceFacade.Fr);
    }

    @Test(expected = NotFoundException.class)
    public void testArchiveWithInvalidUnknownId() throws Exception {
        AppServiceFacade.getOperationFormService().archiveOperationForm(new OperationFormId(1), UserGenerator.getRootUserId(), TldServiceFacade.Fr);
    }

    @Test
    public void testArchiveWithValidId() throws Exception {

        String domainName = DomainGenerator.createNewDomain("test-close-form");
        TradeTicket ticket = TradeGenerator.createNewTradeTicketForDomain(domainName);

        OperationForm operationForm = ticket.getOperationForm();
        TestCase.assertFalse(operationForm.isArchived());

        AppServiceFacade.getOperationFormService().archiveOperationForm(operationForm.getOperationFormId(), UserGenerator.getRootUserId(), TldServiceFacade.Fr);

        OperationForm archivedOperationForm = AppServiceFacade.getOperationFormService().getOperationFormWithId(operationForm.getOperationFormId(), UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        TestCase.assertTrue("Form was not archived", archivedOperationForm.isArchived());
    }
}
