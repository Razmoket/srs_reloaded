/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.services.contracts.ticket;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;

import fr.afnic.commons.beans.Ticket;
import fr.afnic.commons.beans.TicketOperation;
import fr.afnic.commons.beans.TicketStatus;
import fr.afnic.commons.beans.search.ticket.TicketSearchCriteria;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.exception.TooManyResultException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.mock.MockTicketService;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.commons.test.generator.ContactGenerator;
import fr.afnic.commons.test.generator.DomainGenerator;
import fr.afnic.commons.test.generator.TradeGenerator;
import fr.afnic.commons.test.generator.UserGenerator;

public class TicketServiceSearchtTicketContractTest {

    @Test
    public void testSearchTicketWithInvalidId() throws Exception {

        TicketSearchCriteria criteria = new TicketSearchCriteria();
        criteria.setTicketId("123");

        List<Ticket> tickets = AppServiceFacade.getTicketService().searchTicket(criteria, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        Assert.assertTrue("No result should be found", tickets.isEmpty());

    }

    @Test
    public void testSearchTicketWithUnknownId() throws Exception {

        TicketSearchCriteria criteria = new TicketSearchCriteria();
        criteria.setTicketId("NIC999999999999");

        List<Ticket> tickets = AppServiceFacade.getTicketService().searchTicket(criteria, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        Assert.assertTrue("No result should be found", tickets.isEmpty());

    }

    @Test
    public void testSearchTicketWithId() throws Exception {

        String domainName = DomainGenerator.createNewDomain("test-search-ticket");
        Ticket ticket = TradeGenerator.createNewTradeTicketForDomain(domainName);

        TicketSearchCriteria criteria = new TicketSearchCriteria();
        criteria.setTicketId(ticket.getId());

        List<Ticket> tickets = AppServiceFacade.getTicketService().searchTicket(criteria, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        Assert.assertEquals("Should find only one ticket", 1, tickets.size());
        Assert.assertEquals(ticket.getId(), tickets.get(0).getId());

    }

    @Test
    public void testSearchTicketWithUnknownRegistrarCode() throws Exception {

        TicketSearchCriteria criteria = new TicketSearchCriteria();
        criteria.setRegistrarCode("CODE QUI N EXISTE PAS");

        List<Ticket> tickets = AppServiceFacade.getTicketService().searchTicket(criteria, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        Assert.assertTrue("No result should be found", tickets.isEmpty());

    }

    @Test
    public void testSearchTicketWithRegistrarCode() throws Exception {

        // On crée un domaine car cela entraine la création d'un ticket
        DomainGenerator.createNewDomain("test-search-ticket");

        TicketSearchCriteria criteria = new TicketSearchCriteria();
        String registrarCode = ContactGenerator.CUSTOMER_CODE;
        criteria.setRegistrarCode(registrarCode);

        criteria.setBeginningDate(new DateTime().withHourOfDay(6).toDate());
        criteria.setEndingDate(new DateTime().withHourOfDay(10).toDate());

        List<Ticket> tickets = AppServiceFacade.getTicketService().searchTicket(criteria, UserGenerator.getRootUserId(), TldServiceFacade.Fr);

        Assert.assertTrue("No tickets found for customer " + registrarCode, tickets.size() > 0);

        for (Ticket ticket : tickets) {
            Assert.assertEquals("Bad registrar code for ticket " + ticket.getId(), registrarCode, ticket.getRegistrarCode());
        }

    }

    @Test
    public void testSearchTicketWithOperation() throws Exception {

        // On crée un domaine car cela entraine la création d'un ticket
        DomainGenerator.createNewDomain("test-search-ticket");

        TicketSearchCriteria criteria = new TicketSearchCriteria();
        TicketOperation operation = TicketOperation.CreateDomain;
        criteria.setOperation(operation);

        criteria.setBeginningDate(new DateTime().withHourOfDay(6).toDate());
        criteria.setEndingDate(new DateTime().withHourOfDay(10).toDate());

        List<Ticket> tickets = AppServiceFacade.getTicketService().searchTicket(criteria, UserGenerator.getRootUserId(), TldServiceFacade.Fr);

        Assert.assertTrue("No tickets found for operation " + operation, tickets.size() > 0);

        for (Ticket ticket : tickets) {
            Assert.assertEquals("Bad operation for ticket " + ticket.getId(), TicketOperation.CreateDomain, ticket.getOperation());
        }
    }

    @Test
    public void testSearchTicketWithNotExistOperation() throws Exception {

        // On crée un domaine car cela entraine la création d'un ticket
        DomainGenerator.createNewDomain("test-search-ticket");

        TicketSearchCriteria criteria = new TicketSearchCriteria();
        TicketOperation operation = TicketOperation.Undefined;
        criteria.setOperation(operation);

        criteria.setBeginningDate(new DateTime().withHourOfDay(6).toDate());
        criteria.setEndingDate(new DateTime().withHourOfDay(10).toDate());

        List<Ticket> tickets = AppServiceFacade.getTicketService().searchTicket(criteria, UserGenerator.getRootUserId(), TldServiceFacade.Fr);

        Assert.assertEquals("No tickets should be found for operation " + operation, tickets.size(), 0);

    }

    @Test
    public void testSearchTicketWithStatus() throws Exception {

        // On crée un domaine car cela entraine la création d'un ticket
        DomainGenerator.createNewDomain("test-search-ticket");

        TicketSearchCriteria criteria = new TicketSearchCriteria();
        TicketStatus status = TicketStatus.Closed;
        criteria.setTicketStatus(status);

        criteria.setBeginningDate(new DateTime().withHourOfDay(6).toDate());
        criteria.setEndingDate(new DateTime().withHourOfDay(10).toDate());

        List<Ticket> tickets = AppServiceFacade.getTicketService().searchTicket(criteria, UserGenerator.getRootUserId(), TldServiceFacade.Fr);

        Assert.assertTrue("No tickets found for status " + status, tickets.size() > 0);

        for (Ticket ticket : tickets) {
            Assert.assertEquals("Bad status for ticket " + ticket.getId(), status, ticket.getStatus());
        }

    }

    @Test
    public void testSearchTicketWithNotExistStatus() throws Exception {

        // On crée un domaine car cela entraine la création d'un ticket
        DomainGenerator.createNewDomain("test-search-ticket");

        TicketSearchCriteria criteria = new TicketSearchCriteria();
        TicketStatus status = TicketStatus.PendingIdentifyHolder;
        criteria.setTicketStatus(status);

        criteria.setBeginningDate(new DateTime().withHourOfDay(6).toDate());
        criteria.setEndingDate(new DateTime().withHourOfDay(10).toDate());

        List<Ticket> tickets = AppServiceFacade.getTicketService().searchTicket(criteria, UserGenerator.getRootUserId(), TldServiceFacade.Fr);

        Assert.assertEquals("No tickets should be found for status " + status, tickets.size(), 0);

    }

    @Test
    public void testSearchTicketWithUnknownDomainName() throws Exception {
        TicketSearchCriteria criteria = new TicketSearchCriteria();
        String domainName = "domaine qui n'existe pas";
        criteria.setDomainName(domainName);

        List<Ticket> tickets = AppServiceFacade.getTicketService().searchTicket(criteria, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        Assert.assertEquals("No tickets should be found for domain " + domainName, 0, tickets.size());

    }

    @Test
    public void testSearchTicketWithDomainName() throws Exception {

        TicketSearchCriteria criteria = new TicketSearchCriteria();
        String domainName = "google.fr";
        criteria.setDomainName(domainName);

        List<Ticket> tickets = AppServiceFacade.getTicketService().searchTicket(criteria, UserGenerator.getRootUserId(), TldServiceFacade.Fr);

        for (Ticket ticket : tickets) {
            Assert.assertEquals("Bad domain name for ticket " + ticket.getId(), domainName, ticket.getDomainName());
        }
    }

    @Test
    public void testSearchTicketWithBeginningAndEndingDate() throws Exception {
        // expectedResultCount est le résultat obtenu lorsque ce test a été effectué pour la première fois.
        int expectedResultCount = 82;

        DateTime beginningDateTime = new DateTime(2010, 2, 1, 10, 0, 0, 0);
        Date beginningDate = beginningDateTime.toDate();
        Date endingDate = beginningDateTime.plusDays(1).toDate();

        this.createResultForMockTicketService(beginningDateTime, expectedResultCount);

        TicketSearchCriteria criteria = new TicketSearchCriteria();

        criteria.setBeginningDate(beginningDate);
        criteria.setEndingDate(endingDate);

        List<Ticket> tickets = AppServiceFacade.getTicketService().searchTicket(criteria, UserGenerator.getRootUserId(), TldServiceFacade.Fr);

        Assert.assertEquals("Bad number of tickets found ", expectedResultCount, tickets.size());

        for (Ticket ticket : tickets) {
            Assert.assertFalse("create date before beginning date : " + ticket.getCreateDate().toLocaleString() + " < " + beginningDate + " for ticket " + ticket.getId(),
                               ticket.getCreateDate().before(beginningDate));
            Assert.assertFalse("create date after ending date: " + ticket.getCreateDate() + " > " + endingDate, ticket.getCreateDate().after(endingDate));
        }

    }

    @Test
    public void testSearchTicketWithBeginningDateAfterEndingDate() throws Exception {

        TicketSearchCriteria criteria = new TicketSearchCriteria();

        GregorianCalendar calendar = new GregorianCalendar();
        criteria.setBeginningDate(calendar.getTime());

        calendar.add(Calendar.DATE, -1);
        criteria.setEndingDate(calendar.getTime());

        List<Ticket> tickets = AppServiceFacade.getTicketService().searchTicket(criteria, UserGenerator.getRootUserId(), TldServiceFacade.Fr);

        Assert.assertEquals("No result should be found", 0, tickets.size());

    }

    @Test
    public void testSearchTicketWithBeginningDateInTheFuture() throws Exception {
        TicketSearchCriteria criteria = new TicketSearchCriteria();

        GregorianCalendar calendar = new GregorianCalendar();
        calendar.add(Calendar.YEAR, 1);
        criteria.setBeginningDate(calendar.getTime());

        List<Ticket> tickets = AppServiceFacade.getTicketService().searchTicket(criteria, UserGenerator.getRootUserId(), TldServiceFacade.Fr);

        Assert.assertEquals("No result should be found", 0, tickets.size());

    }

    @Test
    public void testSearchTicketWithEndingDateFarInThePast() throws Exception {
        TicketSearchCriteria criteria = new TicketSearchCriteria();

        GregorianCalendar calendar = new GregorianCalendar();
        calendar.set(Calendar.YEAR, 1980);
        criteria.setEndingDate(calendar.getTime());

        List<Ticket> tickets = AppServiceFacade.getTicketService().searchTicket(criteria, UserGenerator.getRootUserId(), TldServiceFacade.Fr);

        // 15 est le résultat obtenu lorsque ce test a été effectué pour la première fois.
        Assert.assertEquals("No result should be found", 0, tickets.size());

    }

    /**
     * On teste 2 critères car le status seul remonterait beaucoup trop de résultats
     * 
     * @throws Exception
     */
    @Test
    public void testSearchTicketWithStatusAndRegistrarCode() throws Exception {

        TicketSearchCriteria criteria = new TicketSearchCriteria();

        TicketStatus status = TicketStatus.Closed;
        criteria.setTicketStatus(status);

        String registrarCode = "RENATER";
        criteria.setRegistrarCode(registrarCode);
        criteria.setMaxResultCount(10000000);

        List<Ticket> tickets = AppServiceFacade.getTicketService().searchTicket(criteria, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        for (Ticket ticket : tickets) {
            Assert.assertEquals("Bad registrar code for ticket " + ticket.getId(), registrarCode, ticket.getRegistrarCode());
            Assert.assertEquals("Bad status for ticket " + ticket.getId(), status, ticket.getStatus());
        }

    }

    /**
     * On teste 2 critères car l'operation seule remonterait beaucoup trop de résultats
     * 
     * @throws Exception
     */
    @Test
    public void testSearchTicketWithOperationAndRegistrarCode() throws Exception {

        TicketSearchCriteria criteria = new TicketSearchCriteria();

        TicketOperation operation = TicketOperation.DeleteDomain;
        criteria.setOperation(operation);

        String registrarCode = "RENATER";
        criteria.setRegistrarCode(registrarCode);

        List<Ticket> tickets = AppServiceFacade.getTicketService().searchTicket(criteria, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        for (Ticket ticket : tickets) {
            Assert.assertEquals("Bad registrar code for ticket " + ticket.getId(), registrarCode, ticket.getRegistrarCode());
            Assert.assertEquals("Bad operation for ticket " + ticket.getId(), operation, ticket.getOperation());
        }

    }

    @Test
    public void testSearchWithTooManyResults() throws Exception {
        int maxResult = 10;

        TicketSearchCriteria criteria = new TicketSearchCriteria();

        criteria.setMaxResultCount(maxResult);

        // On ajoute un critère de date pour lequel on sait que l'on a plus de 10 résultats
        DateTime beginningDateTime = new DateTime(2010, 2, 1, 10, 0, 0, 0);
        Date beginningDate = beginningDateTime.toDate();
        Date endingDate = beginningDateTime.plusDays(1).toDate();
        criteria.setBeginningDate(beginningDate);
        criteria.setEndingDate(endingDate);

        this.createResultForMockTicketService(beginningDateTime, maxResult + 1);

        try {
            AppServiceFacade.getTicketService().searchTicket(criteria, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
            Assert.fail("Should have a TooManyResultException");
        } catch (TooManyResultException e) {
            Assert.assertEquals("Bad exception maxExceptedtResults", maxResult, e.getResultCount());
        }
    }

    /**
     * Crée des entrée dans les mock. Si le service n'est pas mock rien n'est fait.
     * 
     * @param beginningDateTime
     * @param resultCount
     */
    private void createResultForMockTicketService(DateTime beginningDateTime, int resultCount) {
        // Initialisation pour les mocks
        if (AppServiceFacade.getTicketService() instanceof MockTicketService) {
            MockTicketService mockTicketService = (MockTicketService) AppServiceFacade.getTicketService();

            long num = 1000000000000L;
            DateTime createDateTime = beginningDateTime.plusHours(1);
            for (int i = 0; i < resultCount; i++) {
                Ticket ticket = null;
                try {
                    ticket = new Ticket(UserGenerator.getRootUserId(), TldServiceFacade.Fr);
                } catch (ServiceException e) {
                    Assert.fail("Should be able to get a user");
                }
                ticket.setId("NIC" + num);
                ticket.setCreateDate(createDateTime.toDate());
                mockTicketService.storeTicket(ticket);
                createDateTime = createDateTime.plusMinutes(1);
                num++;
            }
        }
    }
}
