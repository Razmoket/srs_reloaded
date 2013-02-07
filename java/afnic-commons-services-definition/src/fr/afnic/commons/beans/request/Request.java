/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/beans/request/Request.java#29 $
 * $Revision: #29 $
 * $Author: barriere $
 */

package fr.afnic.commons.beans.request;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

import fr.afnic.commons.beans.documents.Document;
import fr.afnic.commons.beans.documents.FolderType;
import fr.afnic.commons.beans.documents.Tree;
import fr.afnic.commons.beans.logs.ILogger;
import fr.afnic.commons.beans.mail.Email;
import fr.afnic.commons.beans.mail.SentEmail;
import fr.afnic.commons.beans.notifications.EmailNotification;
import fr.afnic.commons.beans.notifications.Notification;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.comparators.DocumentReceptionDateComparator;
import fr.afnic.commons.services.exception.NotFoundException;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.utils.DateUtils;

/**
 * Requete traité par le GDD (Gestion Des Domaines).
 * 
 * 
 * @author ginguene
 * 
 */
public abstract class Request<STATUS extends RequestStatus> implements Cloneable, Serializable {

    private static final long serialVersionUID = 1L;

    protected static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    protected int id;
    protected String comment;
    protected Date createDate;
    protected List<RequestHistoryEvent> history;

    protected STATUS status;

    protected List<Document> documents = null;

    protected Date lastStatusUpdate;

    /** le user appelant l'objet */
    protected final UserId userIdCaller;
    /** le tld sur lequel on appelle l'objet */
    protected final TldServiceFacade tldCaller;

    public Request(UserId userId, TldServiceFacade tld) {
        this.userIdCaller = userId;
        this.tldCaller = tld;
        this.createDate = new Date();
    }

    /**
     * Retourne tous les documents de la GED lies a la requete.<br/>
     * Il sont trié du plus récemment recu au plus vieux.
     * 
     * 
     * @return La liste des documents liés à la requete.
     */
    public List<Document> getDocuments() {
        // if (this.documents == null) {
        try {
            this.documents = new ArrayList<Document>();
            Set<String> handleSet = AppServiceFacade.getRequestService()
                                                    .getDocumentsHandleLinkedToRequest(this, this.userIdCaller, this.tldCaller);

            if (handleSet != null) {
                for (String handle : handleSet) {
                    try {
                        Document document = AppServiceFacade.getDocumentService()
                                                            .getDocumentWithHandle(handle, this.userIdCaller, this.tldCaller);
                        this.documents.add(document);
                    } catch (Exception e) {
                        getLogger().warn("getDocuments() for request " + this.getId() + " failed to found linked document " + handle, e);
                    }
                }
            }
            Collections.sort(this.documents, DocumentReceptionDateComparator.getInstance());
        } catch (Exception e) {
            getLogger().error(e.getMessage(), e);
        }
        // }
        return this.documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }

    public boolean hasDocuments() {
        return this.getDocuments() != null && !this.documents.isEmpty();
    }

    public int getId() {
        return this.id;
    }

    public String getIdAsString() {
        return Integer.toString(this.id);
    }

    public void setId(int id) {
        this.id = id;
    }

    public abstract void attachResponse(String documentHandle) throws ServiceException;

    public void addDocument(String handle) throws ServiceException {
        Document document = AppServiceFacade.getDocumentService()
                                            .getDocumentWithHandle(handle, this.userIdCaller, this.tldCaller);

        this.addDocument(document);
    }

    /**
     * Ajoute un document a la request. L'ajout est persistant (on modifie les données dans la BD).<br/>
     * Le statut de la requete est également modifié si nécéssaire.
     * 
     * 
     * @param document
     * @throws DaoException
     */
    public void addDocument(Document document) throws ServiceException {
        this.documents = this.getDocuments();
        AppServiceFacade.getRequestService().linkDocumentToRequest(document, this, this.userIdCaller, this.tldCaller);
        AppServiceFacade.getDocumentService().moveDocument(document.getHandle(), Tree.get(this.userIdCaller, this.tldCaller).getRequest(), this.userIdCaller, this.tldCaller);
        this.documents.add(document);
    }

    public String getComment() throws ServiceException {
        if (this.comment == null) {
            this.comment = AppServiceFacade.getRequestService().getRequestComment(this, this.userIdCaller, this.tldCaller);
            if (this.comment == null) {
                this.comment = "";
            }
        }
        return this.comment;

    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getCreateDate() {
        return DateUtils.clone(this.createDate);
    }

    /**
     * retourne une chaine de caractère correspondant à la date de creation du du code d'autorisation On peut definir le format de cette date via la
     * methode setDateFormat
     * 
     * @return
     */
    public String getCreateDateStr() {
        if (this.getCreateDate() == null) {
            return "";
        } else {
            return Request.DATE_FORMAT.format(this.getCreateDate());
        }
    }

    public void setCreateDate(Date createDate) {
        this.createDate = DateUtils.clone(createDate);
    }

    /**
     * Retourne l'historique de la requete.<br/>
     * L'evenement le plus recent étant en première position et le <br>
     * plus ancien en dernière position.
     * 
     * 
     * @return
     * @throws ServiceException
     */
    public List<RequestHistoryEvent> getHistory() throws ServiceException {
        if (this.history == null) {
            this.history = AppServiceFacade.getRequestService().getHistory(this.getClass().getSimpleName(), this.id, this.userIdCaller, this.tldCaller);
        }
        return this.history;
    }

    public boolean hasHistory() throws ServiceException {
        return this.getHistory() != null && !this.getHistory().isEmpty();
    }

    public void addHisto(RequestHistoryEvent event) {
        if (this.history == null) {
            this.history = new ArrayList<RequestHistoryEvent>();
        }

        this.history.add(event);
    }

    public STATUS getStatus() {
        return this.status;
    }

    public void setStatus(STATUS status) {
        this.status = status;
    }

    public String getStatusDescription() throws ServiceException {
        if (this.status != null) {
            return this.status.getDescription();
        } else {
            return "Status undefined: " + this.status;
        }
    }

    /**
     * Archive tous les documents liés à la doc
     * 
     */
    public void archiveDocuments() {
        getLogger().debug("About to perform archiveDocuments for Authorization Request with "
                          + "id=" + this.getId() + ", "
                          + " (" + this.getDocuments().size() + " documents)");

        if (this.hasDocuments()) {
            for (Document document : this.getDocuments()) {
                try {
                    document.archive();
                } catch (ServiceException e) {
                    getLogger().error(e.getMessage());
                }
            }
        }
    }

    /**
     * Met à jour les modification de la requete dans la base de données
     * 
     * @param userLogin
     * @throws ServiceException
     */
    public abstract void update() throws ServiceException;

    public boolean hasFinalStatus() {
        return this.getStatus().isFinalStatus();
    }

    public boolean hasNotFinalStatus() {
        return !this.hasFinalStatus();
    }

    /**
     * Retourne la date du dernier changement de status ou la date de creation si le status n'a jamais été modifié
     * 
     * @return
     * @throws ServiceException
     */
    public Date getLastUpdateStatus() throws ServiceException {
        if (this.lastStatusUpdate == null) {
            this.lastStatusUpdate = this.createDate;
        }
        return DateUtils.clone(this.lastStatusUpdate);
    }

    /**
     * Retourne le nom de l'operateur qui a effectue le dernier update
     * 
     * @return
     * @throws ServiceException
     */
    public String getLastStatusUpdater() throws ServiceException {
        List<RequestHistoryEvent> history = this.getHistory();
        if (history != null && history.size() > 0) {
            return this.getHistory().get(0).getUser();
        } else {
            throw new NotFoundException("No history for request " + this.id);
        }
    }

    public void setLastStatusUpdate(Date lastStatusUpdate) {
        if (lastStatusUpdate != null) {
            this.lastStatusUpdate = (Date) lastStatusUpdate.clone();
        } else {
            this.lastStatusUpdate = null;
        }
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + ": id[" + this.id + "]";
    }

    /**
     * Ajoute des notification comme document à la requete
     * 
     * @param notifications
     * @throws ServiceException
     */
    public void addNotifications(List<Notification> notifications) throws ServiceException {
        if (notifications == null) throw new IllegalArgumentException("notifications is null");
        for (Notification notification : notifications) {
            if (notification instanceof EmailNotification) {
                EmailNotification emailNotification = (EmailNotification) notification;
                String handle = AppServiceFacade.getOldDocumentService().addMailInFolder(emailNotification.getSentMail(), FolderType.Request, this.userIdCaller, this.tldCaller);
                this.addDocument(handle);
            }
        }
    }

    private static ILogger getLogger() {
        return AppServiceFacade.getLoggerService().getLogger(Request.class);
    }

    public RequestHistoryEvent getOlderHistoryEvent() throws ServiceException {
        if (this.hasHistory()) {
            return this.getHistory().get(this.getHistory().size() - 1);
        } else {
            return null;
        }
    }

    public Request copy() throws ServiceException {
        try {
            return (Request) this.clone();
        } catch (Exception e) {
            throw new ServiceException("copy() failed", e);
        }
    }

    /**
     * Retourne le premier evenement d premier passage en état final.
     * 
     * 
     * @return
     * @throws ServiceException
     */
    public RequestHistoryEvent getFirstFinalStatusHistoryEvent() throws ServiceException {
        RequestHistoryEvent firstFinalEvent = null;

        if (this.hasHistory()) {
            for (RequestHistoryEvent event : this.getHistory()) {
                if (event.getField() == RequestHistoryEventField.Status) {
                    RequestStatus status = this.getStatus(event.getNewValue());
                    if (firstFinalEvent == null
                        || (status.isFinalStatus() && event.getDate().before(firstFinalEvent.getDate()))) {
                        firstFinalEvent = event;
                    }
                }
            }
        }
        return firstFinalEvent;

    }

    public String getType() {
        return this.getClass().getSimpleName();
    }

    public void sendEmail(Email email) throws ServiceException {
        SentEmail sentEmail = AppServiceFacade.getEmailService().sendEmail(email, this.userIdCaller, this.tldCaller);
        String newDocHandle = AppServiceFacade.getDocumentService().createDocument(sentEmail, Tree.get(this.userIdCaller, this.tldCaller).getRequest(), this.userIdCaller, this.tldCaller);
        this.addDocument(newDocHandle);

    }

    protected abstract RequestStatus getStatus(String statusAsString);

    public abstract boolean isExpired();

    public UserId getUserIdCaller() {
        return this.userIdCaller;
    }

}
