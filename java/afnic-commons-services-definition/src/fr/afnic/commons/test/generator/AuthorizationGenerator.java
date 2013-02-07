/*
 * $Id: AuthorizationGenerator.java,v 1.2 2010/09/09 08:26:42 ginguene Exp $
 * $Revision: 1.2 $
 * $Author: ginguene $
 */

package fr.afnic.commons.test.generator;

import java.util.Date;

import fr.afnic.commons.beans.Authorization;
import fr.afnic.commons.beans.AuthorizationOperation;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.beans.request.AuthorizationReferenceGenerator;
import fr.afnic.commons.beans.request.AuthorizationRequest;
import fr.afnic.commons.beans.request.AuthorizationRequestStatus;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.exception.invalidformat.InvalidFormatException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.utils.DateUtils;

/**
 * Classe utilitaire permettant de générer des autorisations <br/>
 * à utiliser dans des scénarios de test.
 * 
 * @author ginguene
 * 
 */
public final class AuthorizationGenerator {

    /**
     * Empeche l'instanciation de cette classe utilitaire.
     * 
     */
    private AuthorizationGenerator() {
    }

    /**
     * Génère un code d'autorisation déja expiré.
     * 
     * @param domainName
     *            Nom du domaine pour lequel on souhaite créer un code d'autorisation
     * @param registrarCode
     *            Code du bureau d'enregistrement du code d'autorisation
     * @param operation
     *            Opération autorisé via le code d'autorisation (Create ou Recover)
     * @param handle
     *            Titulaire du code d'autorisation
     * @return
     * @throws ServiceException
     *             Si la création du code d'autorisation échoue
     */
    public static int createExpiredAuthorization(String domainName, String registrarCode, AuthorizationOperation operation, String handle) throws ServiceException {
        Authorization authorization = new Authorization(UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        authorization.setDomainName(domainName);
        authorization.setRegistrarCode(registrarCode);
        authorization.setOperation(operation);
        authorization.setHolderHandle(handle);

        Date createDate = DateUtils.getNbDaysAgo(20);
        Date validityDate = DateUtils.getNbDaysAgo(3);

        authorization.setCreateDate(createDate);
        authorization.setExpirationDate(validityDate);

        String reference = AuthorizationReferenceGenerator.generateAuthorizationReference(authorization);
        authorization.setCode(reference);
        int authorizationId = AppServiceFacade.getAuthorizationService().createAuthorization(authorization, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        return authorizationId;
    }

    public static AuthorizationRequest createRunningAuthorizationRequestForOperation(AuthorizationOperation operation, UserId userId) throws ServiceException, InvalidFormatException {
        return AuthorizationGenerator.createRunningAuthorizationRequestForOperation(operation, ".fr", new Date(), userId);
    }

    public static AuthorizationRequest createRunningAuthorizationRequestForOperation(AuthorizationOperation operation, Date createDate, UserId userId) throws ServiceException, InvalidFormatException {
        return AuthorizationGenerator.createRunningAuthorizationRequestForOperation(operation, ".fr", createDate, userId);
    }

    public static AuthorizationRequest createRunningAuthorizationRequestForOperation(AuthorizationOperation operation, String domainExtension, Date createDate, UserId userId) throws ServiceException,
                                                                                                                                                                              InvalidFormatException {

        AuthorizationRequest authorizationRequest = new AuthorizationRequest(UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        authorizationRequest.setRequestedDomainName("nomdedomaine-" + System.currentTimeMillis() + domainExtension);
        authorizationRequest.setStatus(AuthorizationRequestStatus.Running);

        authorizationRequest.setRequestedHolderHandle("AFNI6-FRNIC");
        authorizationRequest.setOperation(operation);
        authorizationRequest.setCreateDate(createDate);
        authorizationRequest.setRegistrarCode("RENATER");

        int id = AppServiceFacade.getAuthorizationRequestService().createAuthorizationRequest(authorizationRequest, userId, TldServiceFacade.Fr);
        return AppServiceFacade.getAuthorizationRequestService().getAuthorizationRequestWithId(id, userId, TldServiceFacade.Fr);
    }

    public static AuthorizationRequest createGeneratedAuthorizationRequestForCreate() throws ServiceException, InvalidFormatException {

        AuthorizationRequest authorizationRequest = new AuthorizationRequest(UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        authorizationRequest.setRequestedDomainName("nomdedomaine-" + System.currentTimeMillis() + ".fr");
        authorizationRequest.setStatus(AuthorizationRequestStatus.Running);

        authorizationRequest.setRequestedHolderHandle("AFNI6-FRNIC");
        authorizationRequest.setOperation(AuthorizationOperation.CreateDomain);
        authorizationRequest.setCreateDate(new Date());
        authorizationRequest.setRegistrarCode("RENATER");

        int id = AppServiceFacade.getAuthorizationRequestService().createAuthorizationRequest(authorizationRequest, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        authorizationRequest.generateAuthorization(UserGenerator.getRootUserId(), TldServiceFacade.Fr);

        return AppServiceFacade.getAuthorizationRequestService().getAuthorizationRequestWithId(id, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
    }

}
