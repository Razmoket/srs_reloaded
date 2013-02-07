package fr.afnic.commons.beans.operations.commons;

import javax.mail.SendFailedException;

import com.sun.mail.smtp.SMTPAddressFailedException;

import fr.afnic.commons.beans.documents.Document;
import fr.afnic.commons.beans.documents.Tree;
import fr.afnic.commons.beans.logs.ILogger;
import fr.afnic.commons.beans.mail.SentEmail;
import fr.afnic.commons.beans.operations.OperationConfiguration;
import fr.afnic.commons.beans.operations.OperationStatus;
import fr.afnic.commons.beans.operations.OperationType;
import fr.afnic.commons.beans.operations.qualification.Qualification;
import fr.afnic.commons.beans.operations.qualification.operation.SimpleQualificationOperation;
import fr.afnic.commons.beans.operations.qualification.operation.email.ParameterizedEmailTemplate;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class NotifyEmailWithTemplate extends SimpleQualificationOperation {

    private static final ILogger LOGGER = AppServiceFacade.getLoggerService().getLogger(NotifyEmailWithTemplate.class);

    public NotifyEmailWithTemplate(OperationType templateType, UserId userId, TldServiceFacade tld) {
        this(OperationConfiguration.create(), templateType, userId, tld);
    }

    public NotifyEmailWithTemplate(OperationConfiguration conf, OperationType templateType, UserId userId, TldServiceFacade tld) {
        super(conf.setType(templateType), userId, tld);
    }

    @Override
    protected OperationStatus executeSimpleImpl() throws Exception {

        ParameterizedEmailTemplate<Qualification> template = AppServiceFacade.getEmailService().getTemplate(this.getType(), Qualification.class, this.userIdCaller, this.tldCaller);

        SentEmail sentEmail = null;
        try {
            sentEmail = template.send(this.getQualification());
        } catch (final ServiceException e) {
            if (e.getCause() != null && e.getCause() instanceof SendFailedException) {
                if (!(e.getCause().getCause() instanceof SMTPAddressFailedException)) {
                    throw e;
                }
            } else {
                throw e;
            }
            return OperationStatus.Warn;
        }

        LOGGER.debug("send to  " + sentEmail.getToEmailAddressesAsString());
        Tree tree = AppServiceFacade.getDocumentService().getTree(this.userIdCaller, this.tldCaller);
        String handle = AppServiceFacade.getDocumentService().createDocument(sentEmail, tree.getOperation(), this.userIdCaller, this.tldCaller);

        LOGGER.debug("email handle   " + handle);
        Document doc = AppServiceFacade.getDocumentService().getDocumentWithHandle(handle, this.userIdCaller, this.tldCaller);
        AppServiceFacade.getOperationService().attach(this, this.userIdCaller, doc, this.tldCaller);
        return OperationStatus.Succed;
    }
}
