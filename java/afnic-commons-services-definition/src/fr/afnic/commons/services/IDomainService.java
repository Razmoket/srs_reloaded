/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/services/IDomainService.java#22 $
 * $Revision: #22 $
 * $Author: barriere $
 */

package fr.afnic.commons.services;

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
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

/**
 * Data Access Object sur les domaine. Permet de consulter des informations et d'effectuer des opérations sur les domaines
 * 
 * @author ginguene
 */
public interface IDomainService {

    /**
     * Recupere un domaine à partir de son nom
     * 
     * @param domain
     * @return un domaine si il est trouve sinon null
     * @throws ServiceException
     *             si l'acces aux données n'est pas possible
     * @see fr.afnic.commons.beans.domain.Domain
     */
    public Domain getDomainWithName(String domain, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Effectue la première étape de la suppression contrôlée sur un Ndd
     * 
     * @param domainName le nom de domaine
     * @param comment le commentaire
     * @param user l'utilisateur en session BOA
     * @param userLogin le login utilisateur
     * @throws ServiceException
     */
    public void controledDeleteDomain(String domainName, String comment, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Effectue la seconde étape de la suppression contrôlée sur un Ndd
     * Effectue la suppression avec un délais de rétention forcé à 0
     * 
     * @param domainName le nom de domaine
     * @throws ServiceException
     */
    public void controledDeleteDomainConfirm(String domainName, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Retourne tous les domaine contenant la chaine passee en parametre
     * 
     * @param domain
     * @return
     * @throws ServiceException
     *             si l'acces aux données n'est pas possible
     */
    public List<Domain> getDomainsWithNameContaining(String domainNameChunk, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Retourne tous les domaines dont le prestataire a le code passé en parametre
     * 
     * @param code
     * @return
     * @throws ServiceException
     */
    public List<Domain> getDomainsWithRegistrarCode(String code, UserId userId, TldServiceFacade tld) throws ServiceException;

    public List<Domain> getDomainsWithHolderHandle(String nicHandleStr, UserId userId, TldServiceFacade tld) throws ServiceException;

    public int getDomainsWithHolderHandleCount(String nicHandleStr, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Retourne la liste des serveurs DNS d'un domaine
     * 
     * @param domain
     * @return
     * @throws IllegalAccessException
     *             si l'acces aux données n'est pas possible
     */
    public List<DnsServer> getDnsServersWithDomain(String domain, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Valide un ticket de trade ouvert sur le domaine dont on passe le nom.
     * 
     * @param domainName
     * @return
     */
    public void approveTradeDomain(String domainName, String login, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Cancel un ticket de trade ouvert sur le domaine dont on passe le nom.
     * 
     * @param domainName
     * @return
     */
    public void cancelTradeDomain(String domainName, String login, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Retourne la liste des domaine dont le titulaire a le handle passé en parametre.
     * 
     * @param nicHandleStr
     * @return
     */
    public List<String> getDomainNamesWithHolderHandle(String nicHandleStr, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Cree un nouveau contact
     * 
     * @param domainName
     * @param registarCode
     * @param holderHandle
     * @param contactAdminHandle
     * @param nocHandle
     * @throws ServiceException
     */
    public Domain createDomain(String domainName, String authInfo, String registarCode, WhoisContact holder, WhoisContact contactAdmin, WhoisContact tech, UserId userId, TldServiceFacade tld)
                                                                                                                                                                                               throws ServiceException;

    /**
     * Retourne tous les nom de domaine d'un titulaire qui ne sont pas en status deleted
     * 
     * @param nicHandleStr
     * @return
     * @throws ServiceException
     */
    public List<String> getDomainNamesWithHolderHandleNotDeleted(String nicHandleStr, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Suppression d'un domaine, suivi d'une période de redemption.
     * 
     * @param domainName
     *            nom du domaine à supprimer
     * @param login
     *            login du user qui demande la suppression
     * @throws ServiceException
     *             Exception retrournée en cas d'erreur
     */
    public void deleteDomain(String domainName, String login, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Lance un recover sur le domain vers le même Registre.<br/>
     * Un code d'autorisation doit exister pour ce domaine.
     * 
     * @param domainName
     *            nom du domaine pour lequel on souhaite faire un recover
     * @param authorization
     *            Code d'autorisation du recover
     * @param adminContactHandle
     *            handle du nouveau contact admin du domaine à utiliser après le recover
     * @param technicalContact
     *            handle du nouveau contact technique du domaine à utiliser après le recover
     * @param login
     *            login du user qui demande la suppression
     * @throws ServiceException
     */
    public void recoverDomain(String domainName, Authorization authorization, String adminContactHandle, String technicalContactHandle, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Retourne la liste des domaines correspondant aux critères de recherche
     * 
     * @param criteria les critères de recherche
     * @return
     * @throws IllegalAccessException
     *             si l'acces aux données echoue ou si l'DAO n'a pas été correctement initialise
     * @throws ServiceException
     */
    public List<Domain> searchDomain(final DomainSearchCriteria criteria, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Retourne un objet ReservedDomainNameMotivation si le nom de domaine nécéssite un code d'autorisation pour etre créé.<br/>
     * Sinon retourne null.
     * 
     * @param domainName
     * @return
     * @throws ServiceException
     */
    public ReservedDomainNameMotivation getReservedDomainNameMotivation(String domainName, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Retourne la liste des DS d'un domaine
     * 
     * @param domain
     * @return
     * @throws IllegalAccessException
     *             si l'acces aux données n'est pas possible
     */
    public List<DSServer> getDSServerWithDomain(String domain, UserId userId, TldServiceFacade tld) throws ServiceException;

    List<String> getDomainNamesWithHolderHandleNotDeletedForSoapOperation(String nicHandleStr, DomainPortfolioOperationType operation, UserId userId, TldServiceFacade tld) throws ServiceException;

    List<Domain> getDomainNamesWithHolderHandleNotDeletedForSoapOperationForbidden(String nicHandleStr, DomainPortfolioOperationType operation, UserId userId, TldServiceFacade tld)
                                                                                                                                                                                    throws ServiceException;
}
