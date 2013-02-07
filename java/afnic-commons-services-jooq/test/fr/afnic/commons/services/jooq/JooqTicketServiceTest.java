package fr.afnic.commons.services.jooq;

import java.util.List;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import fr.afnic.commons.beans.billing.BillableTicketInfo;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.ITicketService;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.utils.sql.pool.SqlDatabaseEnum;

public class JooqTicketServiceTest {

    ITicketService service;

    @Before
    public void initService() throws ServiceException {
        this.service = new JooqTicketService(SqlDatabaseEnum.Boa, TldServiceFacade.Fr);

    }

    @Test
    public void testGetBillableTicketWithOneResult() throws ServiceException {

        List<BillableTicketInfo> tickets = this.service.getBillableTickets(1, 2013, 1, new UserId(1), TldServiceFacade.Fr);
        TestCase.assertNotNull(tickets);
        TestCase.assertEquals(tickets.size(), 1);

    }

    @Test
    public void testGetBillableTicketWithTenResult() throws ServiceException {
        List<BillableTicketInfo> tickets = this.service.getBillableTickets(1, 2013, 10, new UserId(1), TldServiceFacade.Fr);
        TestCase.assertNotNull(tickets);
        TestCase.assertEquals(tickets.size(), 10);
    }
}
