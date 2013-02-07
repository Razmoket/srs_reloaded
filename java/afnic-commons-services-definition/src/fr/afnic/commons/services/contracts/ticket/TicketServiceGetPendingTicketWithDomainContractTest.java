/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.services.contracts.ticket;

import junit.framework.TestCase;

import org.junit.Test;

import fr.afnic.commons.beans.Ticket;
import fr.afnic.commons.beans.TradeTicket;
import fr.afnic.commons.services.exception.DomainNotFoundException;
import fr.afnic.commons.services.exception.NullArgumentException;
import fr.afnic.commons.services.exception.invalidformat.InvalidDomainNameException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.commons.test.generator.DomainGenerator;
import fr.afnic.commons.test.generator.TradeGenerator;
import fr.afnic.commons.test.generator.UserGenerator;

/**
 * Test de la méthode getPendingTicketWithDomain() du service Ticket.
 * 
 * @author ginguene
 * 
 */
public class TicketServiceGetPendingTicketWithDomainContractTest {

    @Test(expected = NullArgumentException.class)
    public void testGetPendingTicketWithDomainWithNullDomainName() throws Exception {
        AppServiceFacade.getTicketService().getPendingTicketWithDomain(null, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
    }

    @Test(expected = InvalidDomainNameException.class)
    public void testGetPendingTicketWithDomainWithInvalidDomainName() throws Exception {
        AppServiceFacade.getTicketService().getPendingTicketWithDomain(DomainGenerator.getInvalidDomainName(), UserGenerator.getRootUserId(), TldServiceFacade.Fr);
    }

    @Test(expected = DomainNotFoundException.class)
    public void testGetPendingTicketWithDomainWithUnknownDomainName() throws Exception {
        AppServiceFacade.getTicketService().getPendingTicketWithDomain(DomainGenerator.getUnusedDomainName(), UserGenerator.getRootUserId(), TldServiceFacade.Fr);
    }

    @Test
    public void testGetPendingTicketWithDomainWithTicket() throws Exception {
        String domainName = DomainGenerator.createNewDomain("test-get-pending-ticket");
        TradeTicket tradeTicket = TradeGenerator.createNewTradeTicketForDomain(domainName);

        Ticket ticket = AppServiceFacade.getTicketService().getPendingTicketWithDomain(domainName, UserGenerator.getRootUserId(), TldServiceFacade.Fr);

        TestCase.assertEquals("Bad pending ticket id", tradeTicket.getId(), ticket.getId());

    }

}
