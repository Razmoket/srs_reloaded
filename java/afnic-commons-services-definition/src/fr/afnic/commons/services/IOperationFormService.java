/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/services/IOperationFormService.java#9 $
 * $Revision: #9 $
 * $Author: barriere $
 */
package fr.afnic.commons.services;

import java.util.List;

import fr.afnic.commons.beans.OperationForm;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.beans.search.form.FormSearchCriteria;
import fr.afnic.commons.beans.validatable.OperationFormId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

/**
 * Service de gestion des formulaires de l'AFNIC
 * 
 * @author alaphilippe
 */
public interface IOperationFormService {

    /**
     * Retourne un formulaire à partir de son identifiant.
     * 
     * @param id
     *            l'identifiant du formulaire
     * @return le formulaire
     * @throws ServiceException
     *             si l'acces aux données n'est pas possible
     * @see fr.afnic.commons.beans.OperationForm
     */
    public OperationForm getOperationFormWithId(OperationFormId id, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Retourne un le commentaire d'un formulaire pour le dernier ticket de transmission associé au Ndd.
     * 
     * @param pNdd
     *            le nom de domaine
     * @return le commentaire du formulaire
     * @throws ServiceException
     *             si l'acces aux données n'est pas possible
     */
    public String getLastTransmissionCommentForDomainName(final String pNdd, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Retourne la saisie initiale avec tous les champs et leur valeur.
     * 
     * @param id
     *            l'identifiant du formulaire
     * @return le contenu de la saisie du formulaire
     * @throws ServiceException
     *             si l'acces aux données n'est pas possible
     */
    public String getOperationFormInitialContent(OperationFormId id, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Récupère le formulaire lié à un ticket, si ce ticket est lié avec un formlaire.
     * 
     * @param ticket
     *            l'identifiant du ticket.
     * @return le formulaire lié au ticket.
     * @throws ServiceException
     *             si l'acces aux données n'est pas possible
     * @see fr.afnic.commons.beans.OperationForm
     */
    public OperationForm getOperationFormWithTicket(String ticket, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Récupère tous les formulaires d'un domaine à partir de son nom.
     * 
     * @param domain
     *            le nom du domaine.
     * @return la <code>list</code> des formulaires du domaine.
     * @throws ServiceException
     *             si l'acces aux données n'est pas possible
     * @see fr.afnic.commons.beans.OperationForm
     */
    public List<OperationForm> getOperationFormsWithDomain(String domain, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Permet d'effectuer une recherche sur les différents formulaires.<br>
     * Voici la liste des critères de recherche, si plusieurs sont saisis, <br>
     * les résultats devront être ceux correspondants à tous les critères saisis:<br>
     * <ul>
     * <li>Par nom de domaine</li>
     * <li>Par numéro de ticket</li>
     * <li>Par numéro de formulaire</li>
     * <li>Par nichandle du titulaire</li>
     * <li>Par nichandle du contact admin</li>
     * <li>Par bureau d'enregistement, la valeur saisie pouvant être soit le code, soit le nom</li>
     * </ul>
     * 
     * @param informationToFind
     *            l'objet regroupe les informations fournies dans le formulaire de recherche.
     * @return la <code>list</code> des formulaires correspondant aux critères de recherche fournit.
     * @throws ServiceException
     */
    public List<OperationForm> searchOperationForms(FormSearchCriteria informationToFind, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Ferme un formulaire
     * 
     * @param operationFormId
     * @throws ServiceException
     */
    public void archiveOperationForm(OperationFormId operationFormId, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Retourne le status du formulaire.<br/>
     * Cette méthode n'est utilile que pour pallier à un bug de la gateway qui ne rempli pas correctement les statuts des formulaires.
     * 
     * 
     * @param operationFormId
     * @throws ServiceException
     */
    public boolean isArchived(OperationFormId operationFormId, UserId userId, TldServiceFacade tld) throws ServiceException;

}
