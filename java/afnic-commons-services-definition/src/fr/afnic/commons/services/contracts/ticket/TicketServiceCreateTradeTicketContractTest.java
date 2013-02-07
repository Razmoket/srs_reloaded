package fr.afnic.commons.services.contracts.ticket;

import java.util.Date;

import junit.framework.Assert;

import org.apache.commons.lang.time.DateUtils;
import org.junit.Before;
import org.junit.Test;

import fr.afnic.commons.beans.ContactIdentificationStatus;
import fr.afnic.commons.beans.TradeTicket;
import fr.afnic.commons.beans.WhoisContact;
import fr.afnic.commons.beans.domain.Domain;
import fr.afnic.commons.services.exception.DomainNotFoundException;
import fr.afnic.commons.services.exception.IllegalArgumentException;
import fr.afnic.commons.services.exception.NullArgumentException;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.exception.invalidformat.InvalidDomainNameException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.facade.SpringAppServiceFacade;
import fr.afnic.commons.services.mock.MockUserService;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.commons.test.generator.ContactGenerator;
import fr.afnic.commons.test.generator.DomainGenerator;
import fr.afnic.commons.test.generator.UserGenerator;

/**
 * Valide le fonctionnement de la méthode createTicket() d'une implémentation de TicketService.
 * 
 * @author ginguene
 * 
 */
public class TicketServiceCreateTradeTicketContractTest {

    @Before
    public void init() throws ServiceException {

        SpringAppServiceFacade facade = new SpringAppServiceFacade(false);
        facade.setUserService(new MockUserService());
        facade.use();
    }

    @Test(expected = NullArgumentException.class)
    public void testCreateTradeTicketWithNullDomainName() throws Exception {
        AppServiceFacade.getTicketService().createTradeTicket(null, "TOTO-FRNIC", "toto", UserGenerator.getRootUserId(), TldServiceFacade.Fr);
    }

    @Test(expected = InvalidDomainNameException.class)
    public void testCreateTradeTicketWithInvalidDomainName() throws Exception {
        AppServiceFacade.getTicketService().createTradeTicket(DomainGenerator.getInvalidDomainName(), "TOTO-FRNIC", "toto", UserGenerator.getRootUserId(), TldServiceFacade.Fr);
    }

    @Test(expected = DomainNotFoundException.class)
    public void testCreateTradeTicketWithUnknownDomainName() throws Exception {
        AppServiceFacade.getTicketService().createTradeTicket(DomainGenerator.getUnusedDomainName(), "TOTO-FRNIC", "toto", UserGenerator.getRootUserId(), TldServiceFacade.Fr);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateTradeTicketWithDomainWithNotOkHolder() throws Exception {
        WhoisContact holder = ContactGenerator.createIndividualContact();
        holder.setIdentificationStatus(ContactIdentificationStatus.Ko);
        AppServiceFacade.getWhoisContactService().updateContact(holder, UserGenerator.ROOT_LOGIN, UserGenerator.getRootUserId(), TldServiceFacade.Fr);

        WhoisContact newHolder = ContactGenerator.createIndividualContact();
        WhoisContact adminContact = ContactGenerator.createCorporateEntityContact();

        Domain domain = AppServiceFacade.getDomainService().createDomain(DomainGenerator.getUnusedDomainName(), "authinfo", holder.getRegistrarCode(), holder, adminContact, adminContact,
                                                                         UserGenerator.getRootUserId(), TldServiceFacade.Fr);

        AppServiceFacade.getTicketService().createTradeTicket(domain.getName(), newHolder.getHandle(), UserGenerator.ROOT_LOGIN, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
    }

    /**
     * l'update de contact semble échouer
     * 
     * @throws Exception
     */
    @Test
    public void testCreateTradeTicketWithNoRegistrarChange() throws Exception {
        WhoisContact holder = ContactGenerator.createIndividualContact();
        holder.setIdentificationStatus(ContactIdentificationStatus.Ok);
        AppServiceFacade.getWhoisContactService().updateContact(holder, UserGenerator.ROOT_LOGIN, UserGenerator.getRootUserId(), TldServiceFacade.Fr);

        Assert.assertEquals("Update contact failed", ContactIdentificationStatus.Ok,
                            AppServiceFacade.getWhoisContactService().getContactWithHandle(holder.getHandle(), UserGenerator.getRootUserId(), TldServiceFacade.Fr).getIdentificationStatus());

        WhoisContact newHolder = ContactGenerator.createIndividualContact();
        WhoisContact adminContact = ContactGenerator.createCorporateEntityContact();

        Domain domain = AppServiceFacade.getDomainService().createDomain(DomainGenerator.getUnusedDomainName(), "authinfo", holder.getRegistrarCode(), holder, adminContact, adminContact,
                                                                         UserGenerator.getRootUserId(), TldServiceFacade.Fr);

        TradeTicket ticket = AppServiceFacade.getTicketService().createTradeTicket(domain.getName(), newHolder.getHandle(), UserGenerator.ROOT_LOGIN, UserGenerator.getRootUserId(),
                                                                                   TldServiceFacade.Fr);

        Assert.assertNotNull("Ticket is null", ticket);
        Assert.assertNotNull("Ticket.id is null", ticket.getId());
        Assert.assertTrue("Ticket.createDate is not today", DateUtils.isSameDay(new Date(), ticket.getCreateDate()));
    }
}
