package fr.afnic.commons.beans.boarequest;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import fr.afnic.commons.beans.documents.Document;
import fr.afnic.commons.beans.mail.Email;
import fr.afnic.commons.beans.operations.CompositeOperation;
import fr.afnic.commons.beans.operations.Operation;
import fr.afnic.commons.beans.operations.OperationConfiguration;
import fr.afnic.commons.beans.operations.OperationId;
import fr.afnic.commons.beans.operations.OperationType;
import fr.afnic.commons.beans.operations.commons.AttachDocument;
import fr.afnic.commons.beans.operations.commons.SendEmail;
import fr.afnic.commons.beans.operations.qualification.operation.OperationFactoryFacade;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class TopLevelOperation extends CompositeOperation {

    protected TopLevelOperationStatus topLevelStatus = TopLevelOperationStatus.Initializing;

    public TopLevelOperation(UserId userId, TldServiceFacade tld) {
        super(userId, tld);
    }

    public TopLevelOperation(OperationConfiguration conf, UserId userId, TldServiceFacade tld) {
        super(conf, userId, tld);
    }

    public TopLevelOperationStatus getTopLevelStatus() {
        return this.topLevelStatus;
    }

    public void setTopLevelStatus(TopLevelOperationStatus topLevelStatus) throws ServiceException {
        this.topLevelStatus = topLevelStatus;
    }

    @Override
    public void updateTopLevelOperation() throws ServiceException {
        TopLevelOperation updatedTopLevelOperation = (TopLevelOperation) this.updateStatus();
        for (Operation subOperation : this.getSubOperations()) {
            subOperation.setParent(updatedTopLevelOperation);
        }

    }

    @Override
    public List<Document> getDocuments() throws ServiceException {
        List<Document> documents = super.getDocuments();

        Collections.sort(documents, new Comparator<Document>() {
            @Override
            public int compare(Document o1, Document o2) {
                return o2.getHandleAsInt() - o1.getHandleAsInt();
            }

        });

        return documents;
    }

    public boolean isNotFinished() {
        return !this.isFinished();
    }

    public boolean isFinished() {
        return this.topLevelStatus == TopLevelOperationStatus.Finished;
    }

    public void sortDocuments() throws ServiceException {
        for (Document document : this.getDocuments()) {
            document.sort();
        }
    }

    public TopLevelOperation attachDocument(String documentHandle) throws ServiceException {
        AttachDocument attachDocument = OperationFactoryFacade.createAttachDocument(this.createOperationConfiguration(), documentHandle, this.userIdCaller, this.tldCaller);
        attachDocument.setDocumentHandle(documentHandle);
        attachDocument.execute();
        return (TopLevelOperation) this.getId().getObjectOwner(this.userIdCaller, this.tldCaller);
    }

    public OperationConfiguration createOperationConfiguration() {
        return OperationConfiguration.create()
                                     .setParentId(this.getId())
                                     .setCreateUserId(this.userIdCaller);

    }

    /**
     * Envoi un email en créant une opération et l'attache à la qualification.
     */
    public void sendEmail(Email emailToSend, String comment) throws ServiceException {

        OperationId id = this.createAndAddSimpleOperation(new SendEmail(OperationConfiguration.create()
                                                                                              .setParentId(this.getId())
                                                                                              .setCreateUserId(this.userIdCaller),
                                                                        comment,
                                                                        emailToSend, this.userIdCaller, this.tldCaller));

        SendEmail sendEmail = AppServiceFacade.getOperationService().getOperation(id, SendEmail.class, this.userIdCaller, this.tldCaller);
        sendEmail.execute();
    }

    @Override
    public final boolean isStateAllowingOperation(OperationType operationType) {
        //Une top level operation ne doit pas etre executée directement
        return false;
    }
}
