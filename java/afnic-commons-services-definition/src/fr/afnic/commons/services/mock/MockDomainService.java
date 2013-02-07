package fr.afnic.commons.services.mock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import fr.afnic.commons.beans.Authorization;
import fr.afnic.commons.beans.DSServer;
import fr.afnic.commons.beans.DnsServer;
import fr.afnic.commons.beans.OperationForm;
import fr.afnic.commons.beans.Ticket;
import fr.afnic.commons.beans.TicketStatus;
import fr.afnic.commons.beans.WhoisContact;
import fr.afnic.commons.beans.domain.Domain;
import fr.afnic.commons.beans.domain.DomainStatus;
import fr.afnic.commons.beans.operations.qualification.operation.DomainPortfolioOperationType;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.beans.request.reserveddomains.FundamentalTerm;
import fr.afnic.commons.beans.request.reserveddomains.ReservedDomainNameMotivation;
import fr.afnic.commons.beans.search.domain.DomainSearchCriteria;
import fr.afnic.commons.beans.validatable.OperationFormId;
import fr.afnic.commons.checkers.SpecialDomainChecker;
import fr.afnic.commons.services.IDomainService;
import fr.afnic.commons.services.contracts.domain.DomainServiceParametersMethodChecker;
import fr.afnic.commons.services.exception.NotFoundException;
import fr.afnic.commons.services.exception.NotImplementedException;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;

/**
 * Implémentation Mock du IDomainService.
 * 
 * @author ginguene
 * 
 */
public class MockDomainService implements IDomainService {

    private final DomainServiceParametersMethodChecker methodChecker = new DomainServiceParametersMethodChecker();

    /** map contenant en clé le nom du domaine et en valeur le domaine correspondant */
    private final HashMap<String, Domain> nameMap = new HashMap<String, Domain>();

    @Override
    public Domain getDomainWithName(String domainName, UserId userId, TldServiceFacade tld) throws ServiceException {
        this.methodChecker.checkGetDomainWithNameParameters(domainName);
        Domain domain = this.nameMap.get(domainName);
        if (domain != null) {
            return domain;
        } else {
            throw new NotFoundException("Domain " + domainName + " not found", Domain.class);
        }
    }

    @Override
    public List<Domain> getDomainsWithNameContaining(String domainNameChunk, UserId userId, TldServiceFacade tld)
                                                                                                                 throws ServiceException {
        List<Domain> retour = new ArrayList<Domain>();
        for (String domainName : this.nameMap.keySet()) {
            if (domainName.startsWith(domainNameChunk)) {
                retour.add(this.nameMap.get(domainName));
            }
        }

        if (retour.isEmpty()) {
            throw new NotFoundException("Domain " + domainNameChunk + " not found", Domain.class);
        }
        return retour;
    }

    @Override
    public List<Domain> getDomainsWithRegistrarCode(String code, UserId userId, TldServiceFacade tld)
                                                                                                     throws ServiceException {
        List<Domain> retour = new ArrayList<Domain>();
        for (Domain domain : this.nameMap.values()) {
            if (code.equals(domain.getRegistrarCode())) {
                retour.add(domain);
            }
        }

        if (retour.isEmpty()) {
            throw new NotFoundException("Registrar code" + code + " not found", Domain.class);
        }
        return retour;
    }

    @Override
    public List<DnsServer> getDnsServersWithDomain(String domain, UserId userId, TldServiceFacade tld)
                                                                                                      throws ServiceException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void approveTradeDomain(String domainName, String login, UserId userId, TldServiceFacade tld)
                                                                                                        throws ServiceException {
        // TODO Auto-generated method stub

    }

    @Override
    public void cancelTradeDomain(String domainName, String login, UserId userId, TldServiceFacade tld) throws ServiceException {
        Ticket ticket = AppServiceFacade.getTicketService().getPendingTicketWithDomain(domainName, userId, tld);
        AppServiceFacade.getTicketService().updateStatus(ticket.getId(), TicketStatus.Closed, "CLOSED BY MOCK", login, userId, tld);
    }

    @Override
    public List<String> getDomainNamesWithHolderHandle(String nicHandleStr, UserId userId, TldServiceFacade tld)
                                                                                                                throws ServiceException {
        List<String> domainNames = new ArrayList<String>();
        for (Domain domain : this.nameMap.values()) {
            if (nicHandleStr.equals(domain.getHolderHandle())) {
                domainNames.add(domain.getName());
            }
        }
        return domainNames;
    }

    @Override
    public Domain createDomain(String domainName,
                               String authInfo,
                               String registarCode,
                               WhoisContact holder,
                               WhoisContact contactAdmin,
                               WhoisContact tech, UserId userId, TldServiceFacade tld) throws ServiceException {

        this.methodChecker.checkCreateDomainParameters(domainName, authInfo, registarCode, holder, contactAdmin, tech);

        Domain domain = new Domain(userId, tld);
        domain.setName(domainName);
        domain.setRegistrarCode(registarCode);
        domain.setHolderHandle(holder.getHandle());
        domain.setStatus(DomainStatus.Active);
        if (contactAdmin != null) {
            domain.setAdminContactHandle(contactAdmin.getHandle());
        }

        this.nameMap.put(domainName, domain);

        OperationFormId formId = null;
        if (AppServiceFacade.getOperationFormService() instanceof MockOperationFormService) {
            MockOperationFormService mockOperationFormService = (MockOperationFormService) AppServiceFacade.getOperationFormService();
            OperationForm operationForm = mockOperationFormService.createCreateDomainOperationForm(domain, registarCode, userId, tld);
            formId = operationForm.getOperationFormId();
        }
        if (AppServiceFacade.getTicketService() instanceof MockTicketService) {
            MockTicketService mockTicketService = (MockTicketService) AppServiceFacade.getTicketService();
            mockTicketService.createCreateDomainTicket(domainName, registarCode, formId, userId, tld);
        }
        return domain;
    }

    @Override
    public List<String> getDomainNamesWithHolderHandleNotDeleted(String nicHandleStr, UserId userId, TldServiceFacade tld) throws ServiceException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void deleteDomain(String domainName, String login, UserId userId, TldServiceFacade tld) throws ServiceException {
        Domain domain = this.nameMap.get(domainName);
        domain.setStatus(DomainStatus.Deleted);
    }

    @Override
    public void recoverDomain(String domainName,
                              Authorization authorization,
                              String adminContactHandle,
                              String technicalContactHandle,
                              UserId userId, TldServiceFacade tld) throws ServiceException {
        if (AppServiceFacade.getAuthorizationService() instanceof MockAuthorizationService) {
            MockAuthorizationService authorizationService = (MockAuthorizationService) AppServiceFacade.getAuthorizationService();
            authorizationService.getAuthorizationWithId(authorization.getId(), userId, tld).setActif(false);

        }

        Domain domain = this.nameMap.get(domainName);
        domain.setHolderHandle(authorization.getHolderHandle());
    }

    @Override
    public ReservedDomainNameMotivation getReservedDomainNameMotivation(String domainName, UserId userId, TldServiceFacade tld) throws ServiceException {
        ReservedDomainNameMotivation motivation = SpecialDomainChecker.getReservedDomainNameMotivation(domainName);
        if (motivation != null) {
            return motivation;
        }

        if (domainName.equals("pompier.fr")) {
            return new FundamentalTerm("Etat");
        }

        if (domainName.equals("paris.fr")) {
            return new FundamentalTerm("Commune -Nom Reserve-");
        }

        if (domainName.equals("meurtre.fr")) {
            return new FundamentalTerm("Infractions");
        }
        return null;
    }

    @Override
    public List<Domain> getDomainsWithHolderHandle(String nicHandleStr, UserId userId, TldServiceFacade tld) throws ServiceException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getDomainsWithHolderHandleCount(String nicHandleStr, UserId userId, TldServiceFacade tld) throws ServiceException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void controledDeleteDomain(String domainName, String comment, UserId userId, TldServiceFacade tld) throws ServiceException {
        throw new NotImplementedException();
    }

    @Override
    public void controledDeleteDomainConfirm(String domainName, UserId userId, TldServiceFacade tld) throws ServiceException {
        throw new NotImplementedException();
    }

    @Override
    public List<Domain> searchDomain(DomainSearchCriteria criteria, UserId userId, TldServiceFacade tld) throws ServiceException {
        throw new NotImplementedException();
    }

    @Override
    public List<String> getDomainNamesWithHolderHandleNotDeletedForSoapOperation(String nicHandleStr, DomainPortfolioOperationType operation, UserId userId, TldServiceFacade tld)
                                                                                                                                                                                  throws ServiceException {
        throw new NotImplementedException();
    }

    @Override
    public List<Domain> getDomainNamesWithHolderHandleNotDeletedForSoapOperationForbidden(String nicHandleStr, DomainPortfolioOperationType operation, UserId userId, TldServiceFacade tld)
                                                                                                                                                                                           throws ServiceException {
        throw new NotImplementedException();
    }

    @Override
    public List<DSServer> getDSServerWithDomain(String domain, UserId userId, TldServiceFacade tld) throws ServiceException {
        // TODO Auto-generated method stub
        throw new NotImplementedException();
    }

}
