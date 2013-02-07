package fr.afnic.commons.beans.billing;

import fr.afnic.commons.beans.Ticket;
import fr.afnic.commons.beans.customer.Customer;
import fr.afnic.commons.beans.profiling.users.UserId;

public class Order extends Command {

    public Order(Ticket ticket, Customer delivrableCustomer, UserId createUserId) {
        super(ticket, delivrableCustomer, createUserId);
    }
}
