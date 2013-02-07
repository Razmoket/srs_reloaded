/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/beans/request/AuthorizationRequest.java#42 $
 * $Revision: #42 $
 * $Author: ginguene $
 */

package fr.afnic.commons.beans.request;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.lang.StringUtils;

import fr.afnic.commons.beans.Authorization;
import fr.afnic.commons.beans.AuthorizationOperation;
import fr.afnic.commons.beans.WhoisContact;
import fr.afnic.commons.beans.customer.Customer;
import fr.afnic.commons.beans.documents.Document;
import fr.afnic.commons.beans.documents.Tree;
import fr.afnic.commons.beans.domain.Domain;
import fr.afnic.commons.beans.domain.DomainNameDetail;
import fr.afnic.commons.beans.logs.ILogger;
import fr.afnic.commons.beans.mail.Email;
import fr.afnic.commons.beans.mail.SentEmail;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.beans.request.email.AuthorizationRequestCodeGenerationTemplate;
import fr.afnic.commons.beans.request.reserveddomains.ReservedDomainNameMotivation;
import fr.afnic.commons.beans.statistics.DailyIncrementalMesure;
import fr.afnic.commons.beans.statistics.Measure;
import fr.afnic.commons.beans.statistics.Statistic;
import fr.afnic.commons.beans.statistics.StatisticsFactory;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.facade.exception.ServiceFacadeException;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.utils.DateUtils;

/**
 * Représente une requete de demande de code d'autorisation. Permet de générer un code d'autorisation pour une opération de Recoder ou de Create afin
 * qu'un contact puisse devenir titulaire d'un nom de domaine
 * 
 * 
 */
public class AuthorizationRequest extends Request<AuthorizationRequestStatus> {

    private static final long serialVersionUID = 1L;

    protected static final ILogger LOGGER = AppServiceFacade.getLoggerService().getLogger(AuthorizationRequest.class);

    public static final int NUMBER_OF_DAYS_OF_VALIDITY = 15;

    // nombre de jour durant lesquels une demande est valide.
    // est utilisé pour déterminer la date d'expiration
    private static int nbDayValidity = 15;

    private String requestedDomainName;
    private String requestedHolderHandle;

    private Date validityDate;

    private String registrarCode;

    private WhoisContact holder;

    private Domain domain;

    private AuthorizationRequestSource source;

    // Code d'authorization
    private Authorization authorization;
    private int authorizationId = -1;

    private AuthorizationOperation operation;

    private String motivation;

    private int authorizationPreliminaryExamId;

    public AuthorizationRequest(UserId userId, TldServiceFacade tld) {
        super(userId, tld);
        this.setCreateDate(new Date());
    }

    public String getRequestedDomainName() {
        return this.requestedDomainName;
    }

    public void setRequestedDomainName(String domain) {
        this.requestedDomainName = domain;
    }

    @Override
    public void setCreateDate(Date createDate) {

        if (createDate != null) {
            this.createDate = (Date) createDate.clone();

            GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(this.createDate);
            calendar.add(Calendar.DATE, AuthorizationRequest.nbDayValidity);
            this.setValidityDate(calendar.getTime());
        } else {
            this.createDate = null;
        }
    }

    public AuthorizationOperation getOperation() {
        return this.operation;
    }

    public String getOperationAsString() {
        if (this.operation != null) {
            return this.operation.toString();
        } else {
            return "";
        }
    }

    public AuthorizationRequestSource getSource() {
        if (this.source == null) {
            this.source = this.computeSource();
        }
        return this.source;
    }

    private AuthorizationRequestSource computeSource() {
        if (this.hasAuthorizationPreliminaryExam()) {
            return AuthorizationRequestSource.Extranet;
        } else {
            return AuthorizationRequestSource.DOA;
        }

    }

    public void setOperation(AuthorizationOperation operation) {
        this.operation = operation;
    }

    public String getRegistrarCode() {
        return this.registrarCode;
    }

    public String getMotivation() {
        return this.motivation;
    }

    public void setMotivation(String motivation) {
        this.motivation = motivation;
    }

    public void setRegistrarCode(String registrarCode) {
        this.registrarCode = registrarCode;
    }

    public Customer getCustomer() throws ServiceException {
        return AppServiceFacade.getCustomerService().getCustomerWithCode(this.registrarCode, this.userIdCaller, this.tldCaller);
    }

    public WhoisContact getHolder() throws ServiceException {
        if (this.holder == null) {
            this.holder = AppServiceFacade.getWhoisContactService().getContactWithHandle(this.requestedHolderHandle, this.userIdCaller, this.tldCaller);
        }

        return this.holder;
    }

    public void setHolder(WhoisContact holder) {
        this.holder = holder;
    }

    public String getRequestedHolderHandle() {
        return this.requestedHolderHandle;
    }

    public void setRequestedHolderHandle(String holderHandle) {
        this.requestedHolderHandle = holderHandle;
    }

    public Date getValidityDate() {
        return DateUtils.clone(this.validityDate);
    }

    public void setValidityDate(Date validityDate) {
        this.validityDate = DateUtils.clone(validityDate);

    }

    public void setAuthorizationId(int authorizationId) {
        this.authorizationId = authorizationId;
        this.authorization = null;
    }

    public int getAuthorizationId() {
        return this.authorizationId;
    }

    public Authorization getAuthorization() throws ServiceException {
        if (this.authorization == null && this.authorizationId > 0) {
            this.authorization = AppServiceFacade.getAuthorizationService()
                                                 .getAuthorizationWithId(this.authorizationId, this.userIdCaller, this.tldCaller);
        }
        return this.authorization;
    }

    public void setAuthorization(Authorization authorization) {
        this.authorization = authorization;
    }

    /**
     * Genere une nouvelle authorization. 
     * Son code sera toujours le même pour une même association domaine/titulaire/be/operation. On change la
     * reference (code d'authorization) de l'authorization et on la retourne
     * 
     */
    public Authorization generateAuthorization(UserId userId, TldServiceFacade tld) throws ServiceException {
        this.throwExceptionIfIdentificationRequestNotValidToGenerateAuthorization();

        if (this.getAuthorization() == null || this.getAuthorization().isNoLongerValid()) {
            try {
                Authorization authorizationTmp = new Authorization(this.userIdCaller, this.tldCaller);
                authorizationTmp.setDomainName(this.requestedDomainName);
                authorizationTmp.setHolderHandle(this.requestedHolderHandle);
                authorizationTmp.setOperation(this.operation);
                authorizationTmp.setRegistrarCode(this.getRegistrarCode());
                //authorizationTmp.setExpirationDate(new DateTime().plusDays(AuthorizationRequest.NUMBER_OF_DAYS_OF_VALIDITY).toDate());

                // ajout de l'authorization dans la base;
                int authorizationId = AppServiceFacade.getAuthorizationService().createAuthorization(authorizationTmp, userId, tld);

                this.setAuthorizationId(authorizationId);
                this.setStatus(AuthorizationRequestStatus.Generated);

                this.update();
                this.authorization = authorizationTmp;
            } catch (Exception e) {
                throw new ServiceException("generateAuthorization(" + userId + ") failed", e);
            }
        }

        return this.authorization;
    }

    /**
     * Retourne une exception si la requete n'a pas les champs correctement initialisés pour pouvoir générer un code d'autorisation
     * 
     * @throws IllegalArgumentException
     */
    private void throwExceptionIfIdentificationRequestNotValidToGenerateAuthorization() throws IllegalArgumentException {
        if (this.requestedDomainName == null) throw new IllegalArgumentException("Cannot generate authorization: domain not initialized");
        if (this.operation == null) throw new IllegalArgumentException("Cannot generate authorization: operation not initialized");
        if (this.requestedHolderHandle == null) throw new IllegalArgumentException("Cannot generate authorization: holderHandle not initialized");
        if (this.registrarCode == null) throw new IllegalArgumentException("Cannot generate authorization: registrarCode not initialized");
        if (this.createDate == null) throw new IllegalArgumentException("Cannot generate authorization: createDate not initialized");

    }

    /**
     * Reecriture du hashcode
     */
    @Override
    public int hashCode() {
        return this.requestedDomainName.hashCode() ^ this.registrarCode.hashCode() ^ this.requestedHolderHandle.hashCode()
               ^ this.operation.hashCode();

    }

    @Override
    public void attachResponse(String documentHandle) throws ServiceException {
        this.addDocument(documentHandle);
        if (!this.getStatus().isFinalStatus()) {
            this.setStatus(AuthorizationRequestStatus.Answered);
            this.update();
        }
    }

    /**
     * Passe la requete en statut probleme en ajoutant un commentaire. La modification est immediatement enregistée en base
     * 
     * @param comment
     * @throws ServiceException
     */
    public void fail(String comment, UserId userId) throws ServiceException {
        this.setStatus(AuthorizationRequestStatus.Failed);
        this.setComment(comment);
        AppServiceFacade.getAuthorizationRequestService()
                        .updateAuthorizationRequest(this, this.userIdCaller, this.tldCaller);
    }

    @Override
    public void update() throws ServiceException {
        AppServiceFacade.getAuthorizationRequestService().updateAuthorizationRequest(this, this.userIdCaller, this.tldCaller);
    }

    public boolean hasAuthorization() throws ServiceException {
        return this.getAuthorization() != null;
    }

    @Override
    public boolean isExpired() {
        return new Date().after(this.getExpirationDate());
    }

    public boolean hasExpiredAuthorization() throws ServiceException {
        if (this.hasAuthorization()) {
            return this.getAuthorization().isNoLongerValid();
        }
        return false;
    }

    public boolean hasUsedAuthorization() throws ServiceException {
        if (this.hasAuthorization()) {
            return this.getAuthorization().hasBeenUsed();
        }
        return false;
    }

    public Date getExpirationDate() {
        return DateUtils.getNbDaysLaterFromDate(AuthorizationRequest.NUMBER_OF_DAYS_OF_VALIDITY, this.createDate);
    }

    @Override
    protected RequestStatus getStatus(String statusAsString) {
        return AuthorizationRequestStatus.valueOf(statusAsString);
    }

    @Override
    public AuthorizationRequest copy() throws ServiceException {
        return (AuthorizationRequest) super.copy();
    }

    public int getAuthorizationPreliminaryExamId() {
        return this.authorizationPreliminaryExamId;
    }

    public void setAuthorizationPreliminaryExamId(int authorizationPreliminaryExamId) {
        this.authorizationPreliminaryExamId = authorizationPreliminaryExamId;
    }

    public boolean hasAuthorizationPreliminaryExam() {
        return this.authorizationPreliminaryExamId > 0;
    }

    public Domain getDomain() throws ServiceException {
        if (this.domain == null) {
            this.domain = AppServiceFacade.getDomainService().getDomainWithName(this.requestedDomainName, this.userIdCaller, this.tldCaller);
        }
        return this.domain;
    }

    public void setDomain(Domain domain) {
        this.domain = domain;
    }

    public AuthorizationPreliminaryExam getAuthorizationPreliminaryExam() throws ServiceException {
        return AppServiceFacade.getAuthorizationPreliminaryExamService().getAuthorizationPreliminaryExamWithId(this.authorizationPreliminaryExamId, this.userIdCaller, this.tldCaller);
    }

    public String getReservedDomainNameMotivationAsString() throws ServiceException {
        ReservedDomainNameMotivation reservedDomainNameMotivation = this.getReservedDomainNameMotivation();
        if (reservedDomainNameMotivation == null) {
            return "";
        } else {
            return reservedDomainNameMotivation.getDescription();
        }

    }

    public ReservedDomainNameMotivation getReservedDomainNameMotivation() throws ServiceException {
        DomainNameDetail detail = AppServiceFacade.getQualityService().normalizeDomainName(this.getRequestedDomainName(), this.userIdCaller, this.tldCaller);
        return AppServiceFacade.getDomainService().getReservedDomainNameMotivation(detail.getAsciiEquivalent(), this.userIdCaller, this.tldCaller);

    }

    public void changePreliminaryExamStatus(AuthorizationPreliminaryExamStatus newStatus) throws ServiceException {
        if (this.hasAuthorizationPreliminaryExam()) {
            AuthorizationPreliminaryExam exam = this.getAuthorizationPreliminaryExam();
            exam.setStatus(newStatus);
            AppServiceFacade.getAuthorizationPreliminaryExamService().updateAuthorizationPreliminaryExam(exam, this.userIdCaller, this.tldCaller);
        }
    }

    public void generateAuthorizationAndSendNotification() throws ServiceException {

        this.generateAuthorization(this.userIdCaller, this.tldCaller);
        this.updateAuthorizationPreliminaryExamStatus(AuthorizationPreliminaryExamStatus.Accepted);

        SentEmail sentEmail = new AuthorizationRequestCodeGenerationTemplate(this.userIdCaller, this.tldCaller).send(this);
        this.linkEmail(sentEmail, null);

        this.incrementStatistic(StatisticsFactory.AUTHORIZATION_GENERATED);
    }

    public void invalidateAuthorization(Email email, String comment) throws ServiceException {
        AppServiceFacade.getAuthorizationService().invalidateAuthorization(this.getAuthorizationId(), this.userIdCaller, this.tldCaller);

        this.setStatus(AuthorizationRequestStatus.InvalidatedCode);
        this.update();
        this.incrementStatistic(StatisticsFactory.AUTHORIZATION_INVALIDATED);

        this.sendEmail(email, comment);
    }

    public void abord(Email email, String comment) throws ServiceException {
        this.sendEmailAndChangeStatus(email, comment,
                                      AuthorizationRequestStatus.Aborded,
                                      StatisticsFactory.AUTHORIZATION_ABORD);

        this.updateAuthorizationPreliminaryExamStatus(AuthorizationPreliminaryExamStatus.Aborted);
    }

    public void reject(Email email, String comment) throws ServiceException {
        this.sendEmailAndChangeStatus(email, comment,
                                      AuthorizationRequestStatus.Rejected,
                                      StatisticsFactory.AUTHORIZATION_REJECT);

        this.updateAuthorizationPreliminaryExamStatus(AuthorizationPreliminaryExamStatus.Rejected);
    }

    public void sendEmail(Email email, String comment) throws ServiceException {
        this.sendEmailAndChangeStatus(email, comment,
                                      AuthorizationRequestStatus.Waiting,
                                      StatisticsFactory.AUTHORIZATION_SEND_MAIL);
    }

    private void sendEmailAndChangeStatus(Email email, String comment, AuthorizationRequestStatus newStatus, Statistic statistic) throws ServiceException, ServiceFacadeException {

        SentEmail sentEmail = AppServiceFacade.getEmailService().sendEmail(email, this.userIdCaller, this.tldCaller);

        comment = "[" + this.userIdCaller.getObjectOwner(this.userIdCaller, this.tldCaller).getDisplayFullName() + "]" + comment;
        this.linkEmail(sentEmail, comment);

        this.setStatus(newStatus);
        this.update();
        this.incrementStatistic(statistic);
    }

    private void incrementStatistic(Statistic statistic) {
        try {
            Measure measure = new DailyIncrementalMesure(statistic, this.userIdCaller.getObjectOwner(this.userIdCaller, this.tldCaller).getNicpersLogin());
            AppServiceFacade.getStatisticService().createOrIncrementMeasure(measure, this.userIdCaller, this.tldCaller);
        } catch (Exception e) {
            // Si la mise à jour des stats rencontre un problème, on le signale seulement dans les logs
            LOGGER.warn("incrementStatistic(" + statistic.getLabel() + ", " + this.userIdCaller + ") failed", e);
        }
    }

    private void linkEmail(SentEmail sentEmail, String comment) throws ServiceException {

        Tree tree = AppServiceFacade.getDocumentService().getTree(this.userIdCaller, this.tldCaller);
        String documentHandle = AppServiceFacade.getDocumentService().createDocument(sentEmail, tree.getRequest(), this.userIdCaller, this.tldCaller);

        Document document = AppServiceFacade.getDocumentService().getDocumentWithHandle(documentHandle, this.userIdCaller, this.tldCaller);
        AppServiceFacade.getRequestService().linkDocumentToRequest(document, this, this.userIdCaller, this.tldCaller);

        if (StringUtils.isNotBlank(comment)) {
            AppServiceFacade.getDocumentService().updateComment(document.getHandle(), comment, this.userIdCaller, this.tldCaller);
        }
    }

    private void updateAuthorizationPreliminaryExamStatus(AuthorizationPreliminaryExamStatus newStatus) throws ServiceException {
        if (this.getAuthorizationPreliminaryExam() != null) {
            AuthorizationPreliminaryExam exam = this.getAuthorizationPreliminaryExam();
            exam.setStatus(newStatus);
            AppServiceFacade.getAuthorizationPreliminaryExamService().updateAuthorizationPreliminaryExam(exam, this.userIdCaller, this.tldCaller);
        }
    }

}
