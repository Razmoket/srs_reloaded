package fr.afnic.commons.beans.operations.commons;

import fr.afnic.commons.beans.boarequest.TopLevelOperationStatus;
import fr.afnic.commons.beans.documents.Document;
import fr.afnic.commons.beans.documents.EmailDocument;
import fr.afnic.commons.beans.operations.OperationConfiguration;
import fr.afnic.commons.beans.operations.OperationStatus;
import fr.afnic.commons.beans.operations.OperationType;
import fr.afnic.commons.beans.operations.SimpleOperation;
import fr.afnic.commons.beans.operations.qualification.Qualification;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.commons.utils.Preconditions;

public class AttachDocument extends SimpleOperation {

    private String documentHandle;

    public AttachDocument(UserId userId, TldServiceFacade tld) {
        super(OperationConfiguration.create()
                                    .setType(OperationType.AttachDocument), userId, tld);
    }

    public AttachDocument(OperationConfiguration conf, String documentHandle, UserId userId, TldServiceFacade tld) throws ServiceException {
        super(conf.setType(OperationType.AttachDocument), userId, tld);
    }

    @Override
    protected OperationStatus executeSimpleImpl() throws Exception {
        Preconditions.checkNotEmpty(this.documentHandle, "this.documentHandle");

        Document document = AppServiceFacade.getDocumentService().getDocumentWithHandle(this.documentHandle, this.userIdCaller, this.tldCaller);
        AppServiceFacade.getOperationService().attach(this, this.userIdCaller, document, this.tldCaller);

        if (document instanceof EmailDocument) {
            EmailDocument emailDocument = (EmailDocument) document;
            for (Document attach : emailDocument.getAttachements()) {
                AppServiceFacade.getOperationService().attach(this, this.userIdCaller, attach, this.tldCaller);
                attach.sort();
            }
        }

        Qualification qualif = (Qualification) this.getTopLevelOperation();

        if (qualif.getTopLevelStatus() != TopLevelOperationStatus.Finished) {
            qualif.setTopLevelStatus(TopLevelOperationStatus.ReceivedResponse);
            AppServiceFacade.getQualificationService().updateTopLevelStatus(qualif, this.userIdCaller, this.tldCaller);
        }

        this.updateTopLevelOperation();

        document.sort();

        return OperationStatus.Succed;
    }

    public String getDocumentHandle() {
        return this.documentHandle;
    }

    public void setDocumentHandle(String documentHandle) {
        this.documentHandle = documentHandle;
    }

}
