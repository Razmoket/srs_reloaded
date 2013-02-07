package fr.afnic.commons.services;

import fr.afnic.commons.beans.billing.BillableTicketInfo;
import fr.afnic.commons.beans.billing.Command;
import fr.afnic.commons.beans.billing.CommandId;
import fr.afnic.commons.beans.customer.Customer;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

public interface IBillingService {

    public void updateCustomer(Customer customer, UserId userId, TldServiceFacade tld) throws ServiceException;

    public CommandId createCommand(Command command, TldServiceFacade tld) throws ServiceException;

    public void createCommand(BillableTicketInfo billableTicketInfo, UserId userId, TldServiceFacade tld) throws ServiceException;

}
