package fr.afnic.commons.services.proxy;

import fr.afnic.commons.services.exception.NotImplementedException;

public abstract class ProxyService<SERVICE> {

    private final SERVICE delegationService;

    protected ProxyService() {
        this.delegationService = null;
    }

    protected ProxyService(SERVICE delegationService) {
        this.delegationService = delegationService;
    }

    protected SERVICE getDelegationService() throws NotImplementedException {
        if (this.delegationService == null) {
            throw new NotImplementedException("delegationService is not set for " + this.getClass().getSimpleName() + ".");
        } else {
            return this.delegationService;
        }
    }

}
