/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.services.contracts.ticket;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import junit.framework.TestCase;

import org.apache.commons.lang.time.DateUtils;
import org.junit.Test;

import fr.afnic.commons.beans.Ticket;
import fr.afnic.commons.beans.TicketHistoryEvent;
import fr.afnic.commons.beans.TicketStatus;
import fr.afnic.commons.beans.TradeTicket;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.commons.test.generator.DomainGenerator;
import fr.afnic.commons.test.generator.TradeGenerator;
import fr.afnic.commons.test.generator.UserGenerator;

public class TicketServiceChangeStatusContractTest {

    @Test
    public void testChangeStatus() throws Exception {
        String domainName = DomainGenerator.createNewDomain("test-change-ticket-status");
        TicketStatus newStatus = TicketStatus.Cancelled;
        String userLogin = "ginguene";
        TradeTicket tradeTicket = TradeGenerator.createNewTradeTicketForDomain(domainName);
        String comment = "test pour changeStatus";

        AppServiceFacade.getTicketService().updateStatus(tradeTicket.getId(), newStatus, comment, userLogin, UserGenerator.getRootUserId(), TldServiceFacade.Fr);

        Ticket ticket = AppServiceFacade.getTicketService().getTicketWithId(tradeTicket.getId(), UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        TestCase.assertEquals("Bad ticket status after changeStatus", newStatus, ticket.getStatus());

        List<TicketHistoryEvent> events = ticket.getHistory();
        Collections.sort(events, new Comparator<TicketHistoryEvent>() {
            @Override
            public int compare(TicketHistoryEvent arg0, TicketHistoryEvent arg1) {
                return arg1.getDate().compareTo(arg0.getDate());
            }
        });
        TicketHistoryEvent event = ticket.getHistory().get(0);
        TestCase.assertEquals("Bad history comment", comment + "\nNouvel Etat: " + newStatus.getDescription(UserGenerator.getRootUserId(), TldServiceFacade.Fr) + "\n", event.getComment());
        TestCase.assertEquals("Bad history login", userLogin, userLogin);
        TestCase.assertTrue("Bad history date", DateUtils.isSameDay(event.getDate(), new Date()));

    }
}
