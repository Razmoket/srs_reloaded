package fr.afnic.commons.services.contracts.ticket;

import org.junit.Test;

import fr.afnic.commons.services.exception.NotFoundException;
import fr.afnic.commons.services.exception.invalidformat.InvalidTicketIdException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.commons.test.generator.UserGenerator;

/**
 * Valide le fonctionnement de la méthode getTicketWithId() d'une implémentation de TicketService.
 * 
 * @author ginguene
 * 
 */
public class TicketServiceGetTicketWithIdContractTest {

    @Test(expected = InvalidTicketIdException.class)
    public void testGetTicketWithNullId() throws Exception {
        AppServiceFacade.getTicketService().getTicketWithId(null, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
    }

    @Test(expected = InvalidTicketIdException.class)
    public void testGetTicketWithInvalidFormatId() throws Exception {
        AppServiceFacade.getTicketService().getTicketWithId("123", UserGenerator.getRootUserId(), TldServiceFacade.Fr);
    }

    @Test(expected = NotFoundException.class)
    public void testGetTicketWithUnknowId() throws Exception {
        AppServiceFacade.getTicketService().getTicketWithId("NIC123456789012", UserGenerator.getRootUserId(), TldServiceFacade.Fr);
    }

}
