package fr.afnic.commons.services;

import fr.afnic.commons.beans.domain.Domain;
import fr.afnic.commons.beans.domain.DomainPortfolio;
import fr.afnic.commons.beans.operations.qualification.operation.DomainPortfolioOperationType;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

public interface IDomainPortfolioService {

    /**
     * exporte le portefeuille d'un client en envoyant son contenu par mail.
     */
    @Deprecated
    public boolean startDomainPortfolioExport(String email, String customerCode, String userLogin, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * 
     * Retourne le portefeuille d'un client
     */
    public DomainPortfolio getPortfolioSize(String customerCode, String userLogin, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Effectue une opération sur le portefeuille de domaines d'un titulaire.
     */
    public void execute(DomainPortfolioOperationType operation, String holderNicHandle, UserId userId, TldServiceFacade tld) throws ServiceException;

    public void execute(DomainPortfolioOperationType operation, Domain domain, UserId userId, TldServiceFacade tld) throws ServiceException;

}
