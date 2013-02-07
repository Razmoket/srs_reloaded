package fr.afnic.commons.beans.billing;

import java.util.Date;

import fr.afnic.commons.beans.Ticket;
import fr.afnic.commons.beans.customer.Customer;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;

public class Command {

    private final Ticket ticket;
    private final UserId createUserId;
    private final Customer delivrableCustomer;

    public Command(Ticket ticket, Customer delivrableCustomer, UserId createUserId) {
        this.ticket = ticket;
        this.createUserId = createUserId;
        this.delivrableCustomer = delivrableCustomer;
    }

    public Customer getBillingCustomer() throws ServiceException {
        return this.ticket.getRegistrar();
    }

    public Customer getPayingCustomer() throws ServiceException {
        return this.ticket.getRegistrar();
    }

    public Customer getDelivrableCustomer() {
        return this.delivrableCustomer;
    }

    public Ticket getTicket() {
        return this.ticket;
    }

    public Date getCreateDate() {
        return this.ticket.getCreateDate();
    }

    public UserId getCreateUserId() {
        return this.createUserId;
    }

}
