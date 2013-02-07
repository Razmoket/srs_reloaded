package fr.afnic.commons.services;

import java.util.List;

import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.beans.request.AuthorizationPreliminaryExam;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

public interface IAuthorizationPreliminaryExamService {

    /**
     * Retourne la liste des demandes préliminaires qui doivent etre transformées en requete de code d'autorisation.
     * 
     */
    public List<AuthorizationPreliminaryExam> getAuthorizationPreliminaryExamsToUseForAuthorizationRequestCreation(UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Retourne la demande préliminaire ayant comme id le paramêtre.
     * 
     */
    public AuthorizationPreliminaryExam getAuthorizationPreliminaryExamWithId(int id, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Ajoute une nouvelle demande préliminaire en base.
     * 
     */
    public AuthorizationPreliminaryExam createAuthorizationPreliminaryExam(AuthorizationPreliminaryExam authorizationPreliminaryExam, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Met à jour une demande préliminaire en base.
     * 
     */
    public AuthorizationPreliminaryExam updateAuthorizationPreliminaryExam(AuthorizationPreliminaryExam authorizationPreliminaryExam, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Retourne toutes les demande preliminaire en Pending pour le nom de domaine passé en parametre.
     * 
     */
    public List<AuthorizationPreliminaryExam> getPendingAuthorizationPreliminaryExamWithDomainName(String domainName, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Retourne toutes les AuthorizationPreliminaryExam de la base.
     */
    public List<AuthorizationPreliminaryExam> getAuthorizationPreliminaryExams(UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Supprime une AuthorizationPreliminaryExam de la base.
     */
    public void delete(AuthorizationPreliminaryExam exam, UserId userId, TldServiceFacade tld) throws ServiceException;

}
