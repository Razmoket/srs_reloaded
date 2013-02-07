/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/beans/documents/EmailDocument.java#10 $
 * $Revision: 
 * $Author: barriere $
 */

package fr.afnic.commons.beans.documents;

import java.util.List;

import fr.afnic.commons.beans.mail.SentEmail;
import fr.afnic.commons.beans.operations.OperationId;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.NotFoundException;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

/**
 * 
 * Document correspondant à un EMail stocké dans docushare.
 * 
 * 
 * 
 * @author ginguene
 * 
 */
public class EmailDocument extends Document implements Cloneable {

    private static final long serialVersionUID = 1L;

    private SentEmail sentEmail = null;

    public EmailDocument(UserId userId, TldServiceFacade tld) {
        super(userId, tld);
    }

    public SentEmail getSentEmail() {
        return this.sentEmail;
    }

    public void setSentEmail(SentEmail sentEmail) {
        this.sentEmail = sentEmail;
    }

    public boolean hasSentEmail() {
        return this.sentEmail != null;
    }

    @Override
    public EmailDocument copy() throws CloneNotSupportedException {
        return (EmailDocument) this.clone();
    }

    @Override
    public OperationId searchOperationForAttachment() throws ServiceException {
        OperationId operationId = null;
        operationId = this.searchOperationForAttachmentUsingSubject();

        if (operationId != null) {
            return operationId;
        } else {
            throw new NotFoundException("Cannot find operation for document" + this.getHandle());
        }

    }

    private OperationId searchOperationForAttachmentUsingSubject() throws ServiceException {
        return new SubjectOperationIdFinder(this).findOperationId(this.userIdCaller, this.tldCaller);
    }

    public List<Document> getAttachements() throws ServiceException {
        return null;//AppServiceFacade.getDocumentService().getAttachements(this.handle, this.userIdCaller, this.tldCaller);
    }

}
