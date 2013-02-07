/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/beans/documents/Document.java#33 $
 * $Revision: #33 $
 * $Author: barriere $
 */

package fr.afnic.commons.beans.documents;

import java.text.SimpleDateFormat;
import java.util.Date;

import fr.afnic.commons.beans.logs.ILogger;
import fr.afnic.commons.beans.operations.Operation;
import fr.afnic.commons.beans.operations.OperationId;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.beans.validatable.InvalidDataException;
import fr.afnic.commons.services.exception.NotFoundException;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.exception.invalidformat.InvalidFormatException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.facade.exception.ServiceFacadeException;
import fr.afnic.commons.services.tld.TldServiceFacade;

/**
 * 
 * Document sans entete reconnu
 * 
 * @author ginguene
 * 
 */
public abstract class Document extends Element {

    private static final long serialVersionUID = 1L;

    private static ILogger LOGGER = AppServiceFacade.getLoggerService().getLogger(Document.class);

    private static final String dateFormat = "dd/MM/yyyy HH:mm";

    protected String sender;
    protected Date receptionDate;

    private DocumentSource source;

    public Document(UserId userId, TldServiceFacade tld) {
        super(userId, tld);
    }

    public String getSender() {
        return this.sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public Date getReceptionDate() {
        if (this.receptionDate != null) {
            return (Date) this.receptionDate.clone();
        } else {
            return null;
        }
    }

    /**
     * Fonction assez dépendante de docushare où l'on a des id de type MailMessage-<ID>.
     * Pour une autre Ged, il faudrait faire une modification pour n'utiliser qu'un identifiant int
     * 
     * @return
     */
    public int getHandleAsInt() {
        try {
            String arr[] = this.handle.split("-");
            String str = arr[1];
            return Integer.parseInt(str);
        } catch (Exception e) {
            LOGGER.error("getHandleAsInt() failed for handle:" + this.handle, e);
            return -1;
        }
    }

    /**
     * Change la date de reception de l'objet et ddans docushare
     * 
     * @param receptionDate
     */
    public void setReceptionDate(Date receptionDate) {
        if (receptionDate != null) {
            this.receptionDate = (Date) receptionDate.clone();
        } else {
            this.receptionDate = null;
        }
    }

    public String getReceptionDateStr() {
        if (this.receptionDate != null) {
            return new SimpleDateFormat(dateFormat).format(this.receptionDate);
        } else {
            return "";
        }
    }

    public DocumentSource getSource() {
        return this.source;
    }

    public void setSource(DocumentSource source) {
        this.source = source;
    }

    public void setSourceString(String source) throws InvalidFormatException {
        try {
            this.source = DocumentSource.valueOf(source);
        } catch (Exception e) {
            throw new InvalidFormatException("source", DocumentSource.class, String.valueOf(DocumentSource.values()), e);
        }
    }

    /**
     * Archive le document. Si le document est déja archivé cela ne change rien pour lui
     * 
     * @throws DaoException
     */
    public void archive() throws ServiceException {
        AppServiceFacade.getDocumentService().archive(this, this.userIdCaller, this.tldCaller);
    }

    public boolean hasComment() {
        return this.comment != null
               && !this.comment.isEmpty();
    }

    /**
     * Deplace le document dans un autre répertoire
     * 
     * @param folderType
     */
    public void moveToFolder(FolderType folderType) throws ServiceException {
        AppServiceFacade.getOldDocumentService().moveDocumentToFolder(this.handle, folderType, this.userIdCaller, this.tldCaller);
    }

    public FolderType getFolderType() throws ServiceException {
        return AppServiceFacade.getOldDocumentService().getFolderTypeForDocument(this.handle, this.userIdCaller, this.tldCaller);
    }

    /**
     * Constructeur par copie
     * 
     * @param gddDocument
     * @throws CloneNotSupportedException
     */
    public Document copy() throws CloneNotSupportedException {
        return (Document) super.clone();
    };

    public OperationId getOperationId(UserId userId, TldServiceFacade tld) throws ServiceException {
        return AppServiceFacade.getOperationService().getOperation(this.handle, userId, tld);
    }

    public Operation getOperation() throws ServiceException {
        try {
            if (this.getOperationId(this.userIdCaller, this.tldCaller) == null || this.getOperationId(this.userIdCaller, this.tldCaller).getIntValue() <= 0) {
                return null;
            } else {
                return this.getOperationId(this.userIdCaller, this.tldCaller).getObjectOwner(this.userIdCaller, this.tldCaller);
            }
        } catch (InvalidDataException e) {
            //operationId invalide, correspond à un document non lié à une opération
            return null;
        }
    }

    /**
     * Déplace le document dans le bon répertoire dans la GED.
     * @throws ServiceFacadeException 
     * @throws ServiceException 
     */
    public void sort() throws ServiceException {
        AppServiceFacade.getDocumentService().sort(this, this.userIdCaller, this.tldCaller);
    }

    /**
     * Cherche une opération à laquelle le document peut etre attaché.
     * Sur un document de haut niveau, il n'y a pas de règle.
     * Il faut surchargé cette méthode dans les classes filles.
     * 
     */
    public OperationId searchOperationForAttachment() throws ServiceException {
        throw new NotFoundException("Cannot find operation for document " + this.getHandle());
    }

    public void attachTo(Operation operation) throws ServiceException {
        AppServiceFacade.getOperationService().attach(operation, this.userIdCaller, this, this.tldCaller);
    }

    public void attachTo(String documentHandle) throws ServiceException {
        //AppServiceFacade.getDocumentService().attach(this, documentHandle, this.userIdCaller, this.tldCaller);
    }

    public String download(String destFolder) throws ServiceException {
        return AppServiceFacade.getDocumentService().download(this.getHandle(), destFolder, this.userIdCaller, this.tldCaller);
    }

}
