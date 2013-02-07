package fr.afnic.commons.services.proxy;

import fr.afnic.commons.services.ICustomerService;

public abstract class ProxyCustomerService extends ProxyService<ICustomerService> implements ICustomerService {

    protected ProxyCustomerService() {
        super();
    }

    protected ProxyCustomerService(ICustomerService delegationService) {
        super(delegationService);
    }

}
