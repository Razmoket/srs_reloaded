package fr.afnic.commons.services.proxy;

import java.util.List;

import fr.afnic.commons.beans.Authorization;
import fr.afnic.commons.beans.DSServer;
import fr.afnic.commons.beans.DnsServer;
import fr.afnic.commons.beans.WhoisContact;
import fr.afnic.commons.beans.domain.Domain;
import fr.afnic.commons.beans.operations.qualification.operation.DomainPortfolioOperationType;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.beans.request.reserveddomains.ReservedDomainNameMotivation;
import fr.afnic.commons.beans.search.domain.DomainSearchCriteria;
import fr.afnic.commons.services.IDomainService;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

public abstract class ProxyDomainService extends ProxyService<IDomainService> implements IDomainService {

    protected ProxyDomainService() {
        super();
    }

    protected ProxyDomainService(IDomainService delegationService) {
        super(delegationService);
    }

    @Override
    public Domain getDomainWithName(String domain, UserId userId, TldServiceFacade tld) throws ServiceException {
        return this.getDelegationService().getDomainWithName(domain, userId, tld);
    }

    @Override
    public void controledDeleteDomain(String domainName, String comment, UserId userId, TldServiceFacade tld) throws ServiceException {
        this.getDelegationService().controledDeleteDomain(domainName, comment, userId, tld);
    }

    @Override
    public void controledDeleteDomainConfirm(String domainName, UserId userId, TldServiceFacade tld) throws ServiceException {
        this.getDelegationService().controledDeleteDomainConfirm(domainName, userId, tld);
    }

    @Override
    public List<Domain> getDomainsWithNameContaining(String domainNameChunk, UserId userId, TldServiceFacade tld) throws ServiceException {
        return this.getDelegationService().getDomainsWithNameContaining(domainNameChunk, userId, tld);
    }

    @Override
    public List<Domain> getDomainsWithRegistrarCode(String code, UserId userId, TldServiceFacade tld) throws ServiceException {
        return this.getDelegationService().getDomainsWithRegistrarCode(code, userId, tld);
    }

    @Override
    public List<Domain> getDomainsWithHolderHandle(String nicHandleStr, UserId userId, TldServiceFacade tld) throws ServiceException {
        return this.getDelegationService().getDomainsWithHolderHandle(nicHandleStr, userId, tld);
    }

    @Override
    public int getDomainsWithHolderHandleCount(String nicHandleStr, UserId userId, TldServiceFacade tld) throws ServiceException {
        return this.getDelegationService().getDomainsWithHolderHandleCount(nicHandleStr, userId, tld);
    }

    @Override
    public List<DnsServer> getDnsServersWithDomain(String domain, UserId userId, TldServiceFacade tld) throws ServiceException {
        return this.getDelegationService().getDnsServersWithDomain(domain, userId, tld);
    }

    @Override
    public void approveTradeDomain(String domainName, String login, UserId userId, TldServiceFacade tld) throws ServiceException {
        this.getDelegationService().approveTradeDomain(domainName, login, userId, tld);
    }

    @Override
    public void cancelTradeDomain(String domainName, String login, UserId userId, TldServiceFacade tld) throws ServiceException {
        this.getDelegationService().cancelTradeDomain(domainName, login, userId, tld);
    }

    @Override
    public List<String> getDomainNamesWithHolderHandle(String nicHandleStr, UserId userId, TldServiceFacade tld) throws ServiceException {
        return this.getDelegationService().getDomainNamesWithHolderHandle(nicHandleStr, userId, tld);
    }

    @Override
    public Domain createDomain(String domainName, String authInfo, String registarCode, WhoisContact holder, WhoisContact contactAdmin, WhoisContact tech, UserId userId, TldServiceFacade tld)
                                                                                                                                                                                               throws ServiceException {
        return this.getDelegationService().createDomain(domainName, authInfo, registarCode, holder, contactAdmin, tech, userId, tld);
    }

    @Override
    public List<String> getDomainNamesWithHolderHandleNotDeleted(String nicHandleStr, UserId userId, TldServiceFacade tld) throws ServiceException {
        return this.getDelegationService().getDomainNamesWithHolderHandleNotDeleted(nicHandleStr, userId, tld);
    }

    @Override
    public List<String> getDomainNamesWithHolderHandleNotDeletedForSoapOperation(String nicHandleStr, DomainPortfolioOperationType operation, UserId userId, TldServiceFacade tld)
                                                                                                                                                                                  throws ServiceException {
        return this.getDelegationService().getDomainNamesWithHolderHandleNotDeletedForSoapOperation(nicHandleStr, operation, userId, tld);
    }

    @Override
    public List<Domain> getDomainNamesWithHolderHandleNotDeletedForSoapOperationForbidden(String nicHandleStr, DomainPortfolioOperationType operation, UserId userId, TldServiceFacade tld)
                                                                                                                                                                                           throws ServiceException {
        return this.getDelegationService().getDomainNamesWithHolderHandleNotDeletedForSoapOperationForbidden(nicHandleStr, operation, userId, tld);
    }

    @Override
    public void deleteDomain(String domainName, String login, UserId userId, TldServiceFacade tld) throws ServiceException {
        this.getDelegationService().deleteDomain(domainName, login, userId, tld);

    }

    @Override
    public void recoverDomain(String domainName, Authorization authorization, String adminContactHandle, String technicalContactHandle, UserId userId, TldServiceFacade tld) throws ServiceException {
        this.getDelegationService().recoverDomain(domainName, authorization, adminContactHandle, technicalContactHandle, userId, tld);
    }

    @Override
    public List<Domain> searchDomain(final DomainSearchCriteria criteria, UserId userId, TldServiceFacade tld) throws ServiceException {
        return this.getDelegationService().searchDomain(criteria, userId, tld);
    }

    @Override
    public ReservedDomainNameMotivation getReservedDomainNameMotivation(String domainName, UserId userId, TldServiceFacade tld) throws ServiceException {
        return this.getDelegationService().getReservedDomainNameMotivation(domainName, userId, tld);
    }

    @Override
    public List<DSServer> getDSServerWithDomain(String domain, UserId userId, TldServiceFacade tld) throws ServiceException {
        return this.getDelegationService().getDSServerWithDomain(domain, userId, tld);
    }

}
