package fr.afnic.commons.beans.operations.commons;

import fr.afnic.commons.beans.documents.Document;
import fr.afnic.commons.beans.documents.Tree;
import fr.afnic.commons.beans.mail.Email;
import fr.afnic.commons.beans.mail.SentEmail;
import fr.afnic.commons.beans.operations.OperationConfiguration;
import fr.afnic.commons.beans.operations.OperationStatus;
import fr.afnic.commons.beans.operations.OperationType;
import fr.afnic.commons.beans.operations.qualification.operation.SimpleQualificationOperation;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class SendEmail extends SimpleQualificationOperation {

    private Email emailToSend;

    public SendEmail(UserId userId, TldServiceFacade tld) {
        this(OperationConfiguration.create(), null, null, userId, tld);
    }

    public SendEmail(OperationConfiguration conf, String comment, Email emailToSend, UserId userId, TldServiceFacade tld) {
        super(conf.setType(OperationType.SendEmail)
                  .setComment(comment), userId, tld);
        this.emailToSend = emailToSend;

    }

    @Override
    protected OperationStatus executeSimpleImpl() throws Exception {

        SentEmail sentEmail = AppServiceFacade.getEmailService().sendEmail(this.emailToSend, this.userIdCaller, this.tldCaller);
        Tree tree = AppServiceFacade.getDocumentService().getTree(this.userIdCaller, this.tldCaller);
        String handle = AppServiceFacade.getDocumentService().createDocument(sentEmail, tree.getOperation(), this.userIdCaller, this.tldCaller);

        Document doc = AppServiceFacade.getDocumentService().getDocumentWithHandle(handle, this.userIdCaller, this.tldCaller);

        AppServiceFacade.getOperationService().attach(this, this.userIdCaller, doc, this.tldCaller);
        return OperationStatus.Succed;
    }

    public void setEmailToSend(Email emailToSend) {
        this.emailToSend = emailToSend;
    }

    public Email getEmailToSend() {
        return this.emailToSend;
    }

    @Override
    public void populate() throws ServiceException {
        AppServiceFacade.getOperationService().populateSendEmail(this, this.userIdCaller, this.tldCaller);
    }

}
