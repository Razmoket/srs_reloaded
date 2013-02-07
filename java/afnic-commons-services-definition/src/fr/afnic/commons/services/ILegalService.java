package fr.afnic.commons.services;

import java.util.List;

import fr.afnic.commons.beans.WhoisContact;
import fr.afnic.commons.beans.domain.Domain;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.legal.LegalData;
import fr.afnic.commons.legal.SyreliLegalData;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

public interface ILegalService {

    /**
    * Récupère dans le schéma juridique les informations liées à une réclamation
    * 
    * @param pNdd le NDD
    * @param pNumDossier le numéro de dossier
    * @return le formulaire de réclamation si il est trouve sinon null
    * @throws ServiceException
    *             si l'acces aux données n'est pas possible
    * @see fr.afnic.commons.legal.SyreliLegalData
    */
    public SyreliLegalData getSyreliComplaintData(String pNdd, String pNumDossier, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Indique si le Ndd est gelé dans AGTF
     * 
     * @param pNdd le NDD
     * @return false si le Ndd est présent dans agtf.terme avec une jointure sur r_statut.id_statut = 'VALID'
     * @throws ServiceException
     *             si l'acces aux données n'est pas possible
     */
    public boolean isOkAgtf(String pNdd, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * remplis les informations liées à BOA : le status du domaine, l'éventuelle qualif du nicHandle, la présence d'un ticket ouvert
     * Indique si le Ndd est bloqué/gelé/cible d'une opération en cours dans BOA
     * 
     * @param pNdd le NDD
     * @return false si le Ndd est bloqué/gelé/cible d'une opération en cours dans BOA. true sinon
     * @throws ServiceException
     *             si l'acces aux données n'est pas possible
     */
    public boolean fillBOAStatus(LegalData pLegalData, String pNdd, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Récupère les informations liées à une réclamation
     * 
     * @param pNdd le NDD
     * @param pNumDossier le numéro de dossier
     * @return le formulaire de réclamation si il est trouve sinon null
     * @throws ServiceException
     *             si l'acces aux données n'est pas possible
     * @see fr.afnic.commons.legal.LegalData
     */
    public LegalData getComplaintData(String pNdd, String pNumDossier, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Modifie l'état dans la base juridique d'un dossier
     * => 4 pour la fin de la période de 21 jours
     * => 6 pour un abandon
     * 
     * @param pNumDossier le numéro de dossier
     * @param pState l'id d'état
     * @throws ServiceException
     *             Exception retrournée en cas d'erreur
     */
    public void updateComplaintState(String pNumDossier, int pState, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Modifie l'état dans l'agtf
     * cas fonctionnels autorisés : gel -> dégel
     * rien -> gel
     * gel -> gel for recover
     * gel -> gel for delete
     *
     * 
     * @param pNumDossier le nom de domaine
     * @param pComment le commentaire
     * @param pState l'id d'état (0 pour un dégel, 1 pour un gel, 2 pour un gel for recover, 3 pour un gel for delete)
     * @throws ServiceException
     *             Exception retrournée en cas d'erreur
     */
    public void createOrUpdateAgtfState(String pNdd, int pState, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Recherche si l'id d'état existe dans la base juridique
     * 
     * @param pState l'id d'état
     * @return true si l'id existe. false sinon.
     * @throws ServiceException
     *             Exception retrournée en cas d'erreur
     */
    public boolean checkExistingLegalState(int pState, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Recherche si le numéro de dossier existe dans la base juridique
     * 
     * @param pNumDossier le numéro de dossier
     * @return true si le numéro existe. false sinon.
     * @throws ServiceException
     *             Exception retrournée en cas d'erreur
     */
    public boolean checkExistingNumDossier(String pNumDossier, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Remonte la liste des syreli associées à un nom de domaine
     * 
     * @param pNdd le nom de domaine
     * @throws ServiceException
     *             Exception retrournée en cas d'erreur
     */
    public List<SyreliLegalData> getListSyreliForDomainName(String pNdd, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Contrôle que la syreli correspond à un nom de domaine
     *
     * @param pNdd le NDD
     * @param pNumDossier le numéro de dossier
     * @return true si la syreli correspond au nom de domaine, false sinon
     * @throws ServiceException
     *             Exception retrournée en cas d'erreur
     */
    public boolean checkExistingSyreliForDomainName(String pNdd, String pNumDossier, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * retourne la liste des domaines gelés dans agtf pour un whois contact donné
     *
     * @param pWhoisContact le whois contact
     * @return la liste des domaines gelés dans agtf pour le whois. Une liste vide si aucun domaine n'est gelé
     * @throws ServiceException
     *             Exception retrournée en cas d'erreur
     */
    public List<Domain> getDomainAgtfFrozenList(WhoisContact pWhoisContact, UserId userId, TldServiceFacade tld) throws ServiceException;
}
