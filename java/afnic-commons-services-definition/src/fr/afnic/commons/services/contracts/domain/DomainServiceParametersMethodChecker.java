package fr.afnic.commons.services.contracts.domain;

import java.util.List;

import fr.afnic.commons.beans.Authorization;
import fr.afnic.commons.beans.WhoisContact;
import fr.afnic.commons.checkers.CheckerFacade;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.commons.test.generator.UserGenerator;

/**
 * Classe chargée de valider les parametres d'une méthode du DomainService.<br/>
 * Il est conseillé d'appeler les méthodes de cette classe en début de chaque méthode<br/>
 * d'implémentation de IDomainService.
 * 
 * 
 * @author ginguene
 */
public class DomainServiceParametersMethodChecker {

    public void checkIsValidDomainNameParameters(String domainName) throws ServiceException {
    }

    public void checkGetDomainWithNameParameters(String domainName) throws ServiceException {
        CheckerFacade.checkDomainName(domainName, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
    }

    public void checkGetDomainsWithNameContainingParameters(String domainNameChunk) throws ServiceException {
    }

    @Deprecated
    public void checkGetDomainsWithRegistrarCodeParameters(String code) throws ServiceException {
    }

    public void checkGetDomainsWithRegistrarIdentifierParameters(String identifier) throws ServiceException {
    }

    public void checkGetDomainsLinkedToLegalStructureIdParameters(String legalStructureId) throws ServiceException {
    }

    public void checkIsProhibitedOrReservedDomainNameParameters(String domain) throws ServiceException {
    }

    public void checkGetDnsServersWithDomainParameters(String domain) throws ServiceException {
    }

    public void checkApproveTradeDomainParameters(String domainName, String login) throws ServiceException {
    }

    public void checkCancelTradeDomainParameters(String domainName, String login) throws ServiceException {
    }

    public void checkGetDomainNamesWithHolderHandleParameters(String nicHandleStr) throws ServiceException {
    }

    public void checkGetDomainNamesWithAdminHandleParameters(String nicHandleStr) throws ServiceException {
    }

    public void checkCreateDomainParameters(String domainName, String authInfo, String registarCode, WhoisContact holder, WhoisContact contactAdmin, WhoisContact tech) throws ServiceException {
        CheckerFacade.checkDomainName(domainName, UserGenerator.getRootUserId(), TldServiceFacade.Fr);

    }

    public void checkBlockDomainsParameters(List<String> domainsName, String login) throws ServiceException {
    }

    public void checkUnblockDomainsParameters(List<String> domainsName, String login) throws ServiceException {
    }

    public void checkGetDomainNamesWithHolderHandleNotDeletedParameters(String nicHandleStr) throws ServiceException {
    }

    public void checkSuppressDomainsParameters(List<String> domainsName, String login) throws ServiceException {
    }

    public void checkSuppressDomainsAndNotifyParameters(List<String> domainsName, String login) throws ServiceException {
    }

    public void checkDeleteDomainParameters(String domainName, String login) throws ServiceException {
    }

    public void checkRecoverDomainParameters(String domainName,
                                             Authorization authorization,
                                             String adminContactHandle,
                                             String technicalContactHandle,
                                             String login) throws ServiceException {
    }

    public void checkSearchDomainParameters(String domainName, String registrar, String companyId, boolean like) throws ServiceException {
    }

}
