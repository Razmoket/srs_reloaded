package fr.afnic.commons.services.contracts.ticket;

import fr.afnic.commons.test.ServiceFacadeContractTest;

/**
 * Classe de Test permettant de valider une implementation du ITicketService.
 * 
 * @author ginguene
 * 
 */
public abstract class TicketServiceContractTest extends ServiceFacadeContractTest {

    @Override
    public Class<?>[] getSubContractTests() {
        return new Class<?>[] { // TicketServiceSearchtTicketContractTest.class,
                                // TicketServiceGetTicketWithIdContractTest.class,
        // TicketServiceCreateTradeTicketContractTest.class,
        // TicketServiceGetPendingTicketWithDomainContractTest.class,
        // TicketServiceChangeStatusContractTest.class 
        };
    }

}
