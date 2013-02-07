package fr.afnic.commons.services.qualification;

import java.util.List;

import fr.afnic.commons.beans.WhoisContact;
import fr.afnic.commons.beans.application.env.Environnement;
import fr.afnic.commons.beans.boarequest.TopLevelOperationStatus;
import fr.afnic.commons.beans.contactdetails.EmailAddress;
import fr.afnic.commons.beans.documents.Document;
import fr.afnic.commons.beans.documents.LegalDocument;
import fr.afnic.commons.beans.documents.Tree;
import fr.afnic.commons.beans.domain.Domain;
import fr.afnic.commons.beans.logs.ILogger;
import fr.afnic.commons.beans.mail.Email;
import fr.afnic.commons.beans.mail.SentEmail;
import fr.afnic.commons.beans.operations.Operation;
import fr.afnic.commons.beans.operations.OperationConfiguration;
import fr.afnic.commons.beans.operations.OperationStatus;
import fr.afnic.commons.beans.operations.OperationType;
import fr.afnic.commons.beans.operations.commons.NotifyEmailWithTemplate;
import fr.afnic.commons.beans.operations.qualification.PortfolioStatus;
import fr.afnic.commons.beans.operations.qualification.PublicQualificationItemStatus;
import fr.afnic.commons.beans.operations.qualification.Qualification;
import fr.afnic.commons.beans.operations.qualification.QualificationSource;
import fr.afnic.commons.beans.operations.qualification.QualificationStep;
import fr.afnic.commons.beans.operations.qualification.operation.PortfolioStatusUpdate;
import fr.afnic.commons.beans.operations.qualification.operation.email.ParameterizedEmailTemplate;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.NotFoundException;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.utils.StackTraceUtils;

public class QualificationCreator {

    private static final ILogger LOGGER = AppServiceFacade.getLoggerService().getLogger(QualificationCreator.class);

    public static void finalizeAttachedFile(Qualification qualification, LegalDocument document, UserId userId, TldServiceFacade tld) throws ServiceException {
        AppServiceFacade.getOperationService().attach(qualification, userId, document, tld);
        AppServiceFacade.getDocumentService().moveDocument(document.getHandle(), AppServiceFacade.getDocumentService().getTree(userId, tld).getOperation(), userId, tld);
        String newTitle = "[" + qualification.getSource() + "-" + qualification.getIdAsString() + "] " + document.getTitle();
        AppServiceFacade.getDocumentService().updateTitle(document.getHandle(), newTitle, userId, tld);

    }

    public static Qualification createQualification(String holderNicHandle, QualificationSource source, String initiatorEmail, String domainName, UserId userId, TldServiceFacade tld)
                                                                                                                                                                                      throws ServiceException {
        try {
            Qualification qualificationInProgress = AppServiceFacade.getQualificationService().getQualificationInProgress(holderNicHandle, userId, tld);
            LOGGER.error("Try to create qualification for " + holderNicHandle + " but qualification[" + qualificationInProgress.getIdAsString() + "] is already in progress.");
            sendErrorEmailAlreadyExisting(holderNicHandle, source, initiatorEmail, userId, tld);
            throw new ServiceException("createQualification failed");
        } catch (NotFoundException e) {
            LOGGER.info("Creating qualification for " + holderNicHandle);
        } catch (Exception e) {
            sendErrorEmail(holderNicHandle, source, initiatorEmail, e, userId, tld);
            throw new ServiceException("createQualification failed");
        }

        if ((domainName != null)) {
            List<Domain> domainList = AppServiceFacade.getDomainService().getDomainsWithHolderHandle(holderNicHandle, userId, tld);
            boolean found = false;
            for (Domain domain : domainList) {
                found = found || domain.getName().equals(domainName);
            }
            if (!found) {
                sendErrorEmailInvalidNichandle(holderNicHandle, source, initiatorEmail, userId, tld);
                throw new ServiceException("createQualification failed. The nicHandle doesn't match the domain name");
            }
        }

        Qualification qualification = null;
        try {
            qualification = new Qualification(userId, tld);
            qualification.setSource(source);

            qualification.setHolderNicHandle(holderNicHandle);
            if (source == QualificationSource.Reporting || source == QualificationSource.Plaint) {
                qualification.setInitiatorEmailAddress(new EmailAddress(initiatorEmail));
                qualification.setDomainNameInitializedFrom(domainName);
            }

            qualification.setComments("Created by QualificationMaker");

            WhoisContact holder = qualification.getHolder();
            qualification.setCustomerId(holder.getCustomer().getCustomerId());
            holder.setEligibilityStatus(PublicQualificationItemStatus.Pending);
            holder.setReachStatus(PublicQualificationItemStatus.Pending);
            AppServiceFacade.getWhoisContactService().updateContact(holder, userId.getObjectOwner(userId, tld).getLogin(), userId, tld);

            qualification = AppServiceFacade.getQualificationService().createAndGet(qualification, userId, tld);
            qualification.setStatus(OperationStatus.Succed);

            AppServiceFacade.getOperationService().updateStatus(qualification, userId, tld);

            if (qualification.getSource() != QualificationSource.Plaint) {

                AppServiceFacade.getEppService().notifyOfQualificationStep(QualificationStep.Start, qualification, userId, tld);
                sendEmailNotification(qualification, OperationType.NotifyEmailStartValorizationToRegistrar, userId, tld);
                if (qualification.getSource() == QualificationSource.Reporting) {
                    sendEmailNotification(qualification, OperationType.NotifyEmailStartValorizationToInitiator, userId, tld);
                }
                qualification.setTopLevelStatus(TopLevelOperationStatus.Pending);
                AppServiceFacade.getQualificationService().updateTopLevelStatus(qualification, userId, tld);
            } else {
                OperationConfiguration conf = OperationConfiguration.create()
                                                                    .setParentId(qualification.getId())
                                                                    .setBlocking(true)
                                                                    .setCreateUserId(userId);

                PortfolioStatusUpdate operation = AppServiceFacade.getOperationService().createAndGet(new PortfolioStatusUpdate(conf, qualification.getPortfolioStatus(),
                                                                                                                                PortfolioStatus.PendingFreeze, userId, tld), userId, tld);

                qualification.addSubOperation(operation);
                NotifyEmailWithTemplate operation2 = AppServiceFacade.getOperationService().createAndGet(new NotifyEmailWithTemplate(conf, OperationType.NotifyEmailFromComplaintToInitiator, userId,
                                                                                                                                     tld), userId,
                                                                                                         tld);

                qualification.addSubOperation(operation2);

                OperationStatus execute = operation.execute();
                operation.setStatus(execute);
                AppServiceFacade.getOperationService().updateStatus(operation, userId, tld);
                if (execute == OperationStatus.Succed || execute == OperationStatus.Checked) {
                    execute = operation2.execute();
                    operation2.setStatus(execute);
                    AppServiceFacade.getOperationService().updateStatus(operation2, userId, tld);
                    if (execute != OperationStatus.Succed && execute != OperationStatus.Checked) {
                        Operation op = qualification.getTopLevelOperation();
                        op.setStatus(OperationStatus.Failed);
                        AppServiceFacade.getOperationService().updateStatus(op, userId, tld);
                    }
                } else {
                    Operation op = qualification.getTopLevelOperation();
                    op.setStatus(OperationStatus.Failed);
                    AppServiceFacade.getOperationService().updateStatus(op, userId, tld);
                }
            }
            return qualification;
        } catch (Exception e) {
            if (qualification != null) {
                try {
                    Operation operation = qualification.getTopLevelOperation();
                    operation.setStatus(OperationStatus.Failed);
                    AppServiceFacade.getOperationService().updateStatus(operation, userId, tld);
                } catch (Exception e2) {
                    LOGGER.error("impossible de passer le statut de la qualification à failed : " + e2, e2);
                }
            }
            sendErrorEmail(holderNicHandle, source, initiatorEmail, e, userId, tld);
            throw new ServiceException("createQualification failed", e);
        }
    }

    /**
     * Envoie un mail à AJPR en cas de problème lors de la création d'une qualification suite à une plaintes ou à un signalement
     * @throws  
     * @throws ServiceException 
     */
    public static void sendErrorEmail(String holderNicHandle, QualificationSource source, String initiatorEmail, Exception e, UserId userId, TldServiceFacade tld) throws ServiceException {
        Email email = new Email();
        String subject = "[Injector] Echec lors de la création d'une qualification pour le nichandle " + holderNicHandle;

        email.setFromEmailAddress("<injector>boa@nic.fr");

        if (AppServiceFacade.getApplicationService().isEnv(Environnement.Prod)) {
            email.setToEmailAddresses("gdd@afnic.fr");
            email.setCcEmailAddresses("boa@nic.fr");
        } else if (AppServiceFacade.getApplicationService().isEnv(Environnement.Preprod)) {
            email.setToEmailAddresses("testing@nic.fr");
            email.setCcEmailAddresses("boa@nic.fr");
        } else {
            email.setToEmailAddresses("boa@nic.fr");
            subject = "[" + AppServiceFacade.getApplicationService().getCurrentVersion().getEnvironnement() + "]" + subject;
        }
        email.setSubject(subject);

        String msg;
        if (e instanceof ServiceException) {
            msg = ((ServiceException) e).getFirstCauseMessage();
        } else {
            msg = e.getMessage();
        }

        StringBuilder builder = new StringBuilder();
        builder.append("La création de la qualification du titulaire " + holderNicHandle + " a échoué, le message d'erreur remonté est: " + msg);
        builder.append("\nLa source est " + source);
        builder.append("\nL'adresse mail de l'emetteur est " + initiatorEmail);
        builder.append("\n\nLe message d'erreur complet est:\n " + StackTraceUtils.getStackTrace(e));
        email.setContent(builder.toString());

        AppServiceFacade.getEmailService().sendEmail(email, userId, tld);
    }

    /**
     * Envoie un mail à AJPR en cas de problème lors de la création d'une qualification suite à une plaintes ou à un signalement
     * @throws  
     * @throws ServiceException 
     */
    public static void sendErrorEmailAlreadyExisting(String holderNicHandle, QualificationSource source, String initiatorEmail, UserId userId, TldServiceFacade tld) throws ServiceException {
        Email email = new Email();
        String subject = "[Injector] Echec lors de la création d'une qualification pour le nichandle " + holderNicHandle;

        email.setFromEmailAddress("<injector>boa@nic.fr");

        if (AppServiceFacade.getApplicationService().isEnv(Environnement.Prod)) {
            email.setToEmailAddresses("gdd@nic.fr");
            email.setCcEmailAddresses("boa@nic.fr");
        } else if (AppServiceFacade.getApplicationService().isEnv(Environnement.Preprod)) {
            email.setToEmailAddresses("testing@nic.fr");
            email.setCcEmailAddresses("boa@nic.fr");
        } else {
            email.setToEmailAddresses("boa@nic.fr");
            subject = "[" + AppServiceFacade.getApplicationService().getCurrentVersion().getEnvironnement() + "]" + subject;
        }
        email.setSubject(subject);

        StringBuilder builder = new StringBuilder();
        builder.append("La création de la qualification du titulaire " + holderNicHandle + " a échoué, car une qualification pour ce nicHandle existe déjà.");
        builder.append("\nLa source est " + source);
        builder.append("\nL'adresse mail de l'emetteur est " + initiatorEmail);
        email.setContent(builder.toString());

        AppServiceFacade.getEmailService().sendEmail(email, userId, tld);
    }

    /**
     * Envoie un mail à AJPR en cas de problème lors de la création d'une qualification suite à une plaintes ou à un signalement
     * @throws  
     * @throws ServiceException 
     */
    public static void sendErrorEmailInvalidNichandle(String holderNicHandle, QualificationSource source, String initiatorEmail, UserId userId, TldServiceFacade tld) throws ServiceException {
        Email email = new Email();
        String subject = "[Injector] Echec lors de la création d'une qualification pour le nichandle " + holderNicHandle;

        email.setFromEmailAddress("<injector>boa@nic.fr");

        if (AppServiceFacade.getApplicationService().isEnv(Environnement.Prod)) {
            email.setToEmailAddresses("support-only@nic.fr");
            email.setCcEmailAddresses("boa@nic.fr");
        } else if (AppServiceFacade.getApplicationService().isEnv(Environnement.Preprod)) {
            email.setToEmailAddresses("testing@nic.fr");
            email.setCcEmailAddresses("boa@nic.fr");
        } else {
            email.setToEmailAddresses("boa@nic.fr");
            subject = "[" + AppServiceFacade.getApplicationService().getCurrentVersion().getEnvironnement() + "]" + subject;
        }
        email.setSubject(subject);

        StringBuilder builder = new StringBuilder();
        builder.append("La création de la qualification du titulaire " + holderNicHandle + " a échoué, car le nom de domaine indiqué ne correspond pas au nicHandle");
        builder.append("\nLa source est " + source);
        builder.append("\nL'adresse mail de l'emetteur est " + initiatorEmail);
        email.setContent(builder.toString());

        AppServiceFacade.getEmailService().sendEmail(email, userId, tld);
    }

    public static void sendEmailNotification(Qualification qualification, OperationType type, UserId userId, TldServiceFacade tld) throws ServiceException {

        ParameterizedEmailTemplate<Qualification> template = AppServiceFacade.getEmailService().getTemplate(type, Qualification.class, userId, tld);

        SentEmail sentEmail = template.send(qualification);

        LOGGER.debug("send to  " + sentEmail.getToEmailAddressesAsString());
        Tree tree = AppServiceFacade.getDocumentService().getTree(userId, tld);
        String handle = AppServiceFacade.getDocumentService().createDocument(sentEmail, tree.getOperation(), userId, tld);

        LOGGER.debug("email handle   " + handle);
        Document doc = AppServiceFacade.getDocumentService().getDocumentWithHandle(handle, userId, tld);

        AppServiceFacade.getOperationService().attach(qualification, userId, doc, tld);

    }
}
