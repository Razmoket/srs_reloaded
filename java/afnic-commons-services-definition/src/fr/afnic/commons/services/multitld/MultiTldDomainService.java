package fr.afnic.commons.services.multitld;

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

public class MultiTldDomainService implements IDomainService {

    protected MultiTldDomainService() {
        super();
    }

    @Override
    public Domain getDomainWithName(String domain, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getDomainService().getDomainWithName(domain, userId, tld);
    }

    @Override
    public void controledDeleteDomain(String domainName, String comment, UserId userId, TldServiceFacade tld) throws ServiceException {
        tld.getServiceProvider().getDomainService().controledDeleteDomain(domainName, comment, userId, tld);
    }

    @Override
    public void controledDeleteDomainConfirm(String domainName, UserId userId, TldServiceFacade tld) throws ServiceException {
        tld.getServiceProvider().getDomainService().controledDeleteDomainConfirm(domainName, userId, tld);
    }

    @Override
    public List<Domain> getDomainsWithNameContaining(String domainNameChunk, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getDomainService().getDomainsWithNameContaining(domainNameChunk, userId, tld);
    }

    @Override
    public List<Domain> getDomainsWithRegistrarCode(String code, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getDomainService().getDomainsWithRegistrarCode(code, userId, tld);
    }

    @Override
    public List<Domain> getDomainsWithHolderHandle(String nicHandleStr, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getDomainService().getDomainsWithHolderHandle(nicHandleStr, userId, tld);
    }

    @Override
    public int getDomainsWithHolderHandleCount(String nicHandleStr, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getDomainService().getDomainsWithHolderHandleCount(nicHandleStr, userId, tld);
    }

    @Override
    public List<DnsServer> getDnsServersWithDomain(String domain, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getDomainService().getDnsServersWithDomain(domain, userId, tld);
    }

    @Override
    public void approveTradeDomain(String domainName, String login, UserId userId, TldServiceFacade tld) throws ServiceException {
        tld.getServiceProvider().getDomainService().approveTradeDomain(domainName, login, userId, tld);
    }

    @Override
    public void cancelTradeDomain(String domainName, String login, UserId userId, TldServiceFacade tld) throws ServiceException {
        tld.getServiceProvider().getDomainService().cancelTradeDomain(domainName, login, userId, tld);
    }

    @Override
    public List<String> getDomainNamesWithHolderHandle(String nicHandleStr, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getDomainService().getDomainNamesWithHolderHandle(nicHandleStr, userId, tld);
    }

    @Override
    public Domain createDomain(String domainName, String authInfo, String registarCode, WhoisContact holder, WhoisContact contactAdmin, WhoisContact tech, UserId userId, TldServiceFacade tld)
                                                                                                                                                                                               throws ServiceException {
        return tld.getServiceProvider().getDomainService().createDomain(domainName, authInfo, registarCode, holder, contactAdmin, tech, userId, tld);
    }

    @Override
    public List<String> getDomainNamesWithHolderHandleNotDeleted(String nicHandleStr, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getDomainService().getDomainNamesWithHolderHandleNotDeleted(nicHandleStr, userId, tld);
    }

    @Override
    public void deleteDomain(String domainName, String login, UserId userId, TldServiceFacade tld) throws ServiceException {
        tld.getServiceProvider().getDomainService().deleteDomain(domainName, login, userId, tld);
    }

    @Override
    public void recoverDomain(String domainName, Authorization authorization, String adminContactHandle, String technicalContactHandle, UserId userId, TldServiceFacade tld) throws ServiceException {
        tld.getServiceProvider().getDomainService().recoverDomain(domainName, authorization, adminContactHandle, technicalContactHandle, userId, tld);
    }

    @Override
    public List<Domain> searchDomain(DomainSearchCriteria criteria, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getDomainService().searchDomain(criteria, userId, tld);
    }

    @Override
    public ReservedDomainNameMotivation getReservedDomainNameMotivation(String domainName, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getDomainService().getReservedDomainNameMotivation(domainName, userId, tld);
    }

    @Override
    public List<String> getDomainNamesWithHolderHandleNotDeletedForSoapOperation(String nicHandleStr, DomainPortfolioOperationType operation, UserId userId, TldServiceFacade tld)
                                                                                                                                                                                  throws ServiceException {
        return tld.getServiceProvider().getDomainService().getDomainNamesWithHolderHandleNotDeletedForSoapOperation(nicHandleStr, operation, userId, tld);
    }

    @Override
    public List<Domain> getDomainNamesWithHolderHandleNotDeletedForSoapOperationForbidden(String nicHandleStr, DomainPortfolioOperationType operation, UserId userId, TldServiceFacade tld)
                                                                                                                                                                                           throws ServiceException {
        return tld.getServiceProvider().getDomainService().getDomainNamesWithHolderHandleNotDeletedForSoapOperationForbidden(nicHandleStr, operation, userId, tld);
    }

    @Override
    public List<DSServer> getDSServerWithDomain(String domain, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getDomainService().getDSServerWithDomain(domain, userId, tld);
    }

}
