package fr.afnic.commons.services.multitld;

import fr.afnic.commons.beans.billing.BillableTicketInfo;
import fr.afnic.commons.beans.billing.Command;
import fr.afnic.commons.beans.billing.CommandId;
import fr.afnic.commons.beans.customer.Customer;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.IBillingService;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class MultiTldBillingService implements IBillingService {

    public MultiTldBillingService() {
        super();
    }

    @Override
    public void updateCustomer(Customer customer, UserId userId, TldServiceFacade tld) throws ServiceException {
        tld.getServiceProvider().getBillingService().updateCustomer(customer, userId, tld);
    }

    @Override
    public CommandId createCommand(Command command, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getBillingService().createCommand(command, tld);
    }

    @Override
    public void createCommand(BillableTicketInfo billableTicketInfo, UserId userId, TldServiceFacade tld) throws ServiceException {
        tld.getServiceProvider().getBillingService().createCommand(billableTicketInfo, userId, tld);
    }

}
