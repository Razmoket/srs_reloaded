package fr.afnic.commons.services.jooq;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import fr.afnic.commons.beans.billing.BillableTicketInfo;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.IBillingService;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.utils.sql.pool.SqlDatabaseEnum;

public class JooqBillingServiceTest {

    IBillingService service;

    @Before
    public void initService() throws ServiceException {
        this.service = new JooqBillingService(SqlDatabaseEnum.Grc, TldServiceFacade.Fr);

    }

    @Test
    public void testCreateCommand() throws ServiceException {

        BillableTicketInfo info = new BillableTicketInfo();
        info.setCommandDate(new Date());
        info.setArticle("CRE1");
        info.setBilledCustomer("GANDI");
        info.setPayersCustomer("GANDI");
        info.setTicketId("NIC000000001");
        info.setTld("FR");
        info.setDomainName("test.fr");

        this.service.createCommand(info, new UserId(1), TldServiceFacade.Fr);

    }

}
