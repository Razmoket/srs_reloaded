package fr.afnic.commons.services.mock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.base.Objects;

import fr.afnic.commons.beans.documents.Document;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.beans.request.AuthorizationRequest;
import fr.afnic.commons.beans.request.Request;
import fr.afnic.commons.beans.request.RequestHistoryEvent;
import fr.afnic.commons.beans.request.RequestHistoryEventField;
import fr.afnic.commons.beans.request.RequestStatus;
import fr.afnic.commons.beans.request.TradeRequest;
import fr.afnic.commons.services.IRequestService;
import fr.afnic.commons.services.exception.NotFoundException;
import fr.afnic.commons.services.exception.NotImplementedException;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.utils.HashMapList;

/**
 * Mock du requestService.<br/>
 * Fonctionne via un système de map et conserve toutes les données en mémoire.
 * 
 * @author ginguene
 * 
 */
public class MockRequestService implements IRequestService {

    /** Clé: handle du document; Valeur: requete a laquelle le document est lié **/
    private final HashMap<String, Request> mapDocument = new HashMap<String, Request>();

    /** Clé: id de la requete sous la forme <Class_ID>: liste des elements d'historique de cette requete */
    private final HashMapList<String, RequestHistoryEvent> mapHistory = HashMapList.create();

    private static int lastHistoryId = 0;

    @Override
    public Request getRequestById(String type, int id, UserId userId, TldServiceFacade tld) throws ServiceException {

        if (AuthorizationRequest.class.getSimpleName().equals(type)) {
            return AppServiceFacade.getAuthorizationRequestService().getAuthorizationRequestWithId(id, userId, tld);
        }

        if (TradeRequest.class.getSimpleName().equals(type)) {
            return AppServiceFacade.getTradeService().getTradeRequestWithId(id, userId, tld);
        }

        throw new NotImplementedException("Not implemented for type " + type);
    }

    @Override
    public boolean addHistoryEvent(RequestHistoryEvent event, UserId userId, TldServiceFacade tld) throws ServiceException {
        String key = event.getRequest(userId, tld).getClass().getSimpleName() + "_" + event.getRequest(userId, tld).getId();
        this.mapHistory.add(key, event);
        event.setId(this.mapHistory.get(key).size());
        event.setDate(new Date());
        return true;
    }

    @Override
    public List<RequestHistoryEvent> getHistory(String type, int requestId, UserId userId, TldServiceFacade tld) throws ServiceException {
        return this.mapHistory.get(type + "_" + requestId);
    }

    @Override
    public Request getLinkedRequestToDocumentWithHandle(String handle, UserId userId, TldServiceFacade tld) throws ServiceException {

        Request request = this.mapDocument.get(handle);
        if (request != null) {
            return this.mapDocument.get(handle);
        } else {
            throw new NotFoundException("no request linked to document " + handle + " found", Request.class);
        }
    }

    @Override
    public void linkDocumentToRequest(Document document, Request request, UserId userId, TldServiceFacade tld) throws ServiceException {
        try {

            Request copy = request.copy();
            this.mapDocument.put(document.getHandle(), copy);
        } catch (Exception e) {
            throw new ServiceException("failed", e);
        }
    }

    @Override
    public String getRequestComment(Request request, UserId userId, TldServiceFacade tld) throws ServiceException {
        return null;
    }

    @Override
    public Date getFirstTimeStatusForRequest(RequestStatus status, Request request, UserId userId, TldServiceFacade tld) throws ServiceException {
        throw new NotImplementedException();
    }

    @Override
    public Date getLastTimeStatusForRequest(RequestStatus status, Request request, UserId userId, TldServiceFacade tld) throws ServiceException {
        throw new NotImplementedException();
    }

    @Override
    public void cancelRequest(Request request, UserId userId, TldServiceFacade tld) throws ServiceException {

    }

    @Override
    public void deleteRequest(Request request, String login, UserId userId, TldServiceFacade tld) throws ServiceException {
        throw new NotImplementedException();

    }

    @Override
    public void updateHistory(RequestHistoryEvent event, UserId userId, TldServiceFacade tld) throws ServiceException {
        // throw new NotImplementedException();
    }

    @Override
    public void changeLastStatusChange(Request request, Date newDate, UserId userId, TldServiceFacade tld) throws ServiceException {
        this.getRequestById(request.getClass().getSimpleName(), request.getId(), userId, tld).setLastStatusUpdate(newDate);
        this.getRequestById(request.getClass().getSimpleName(), request.getId(), userId, tld).getOlderHistoryEvent().setDate(newDate);

    }

    @Override
    public void changeDateCreation(Request request, Date newDate, UserId userId, TldServiceFacade tld) throws ServiceException {
        this.getRequestById(request.getClass().getSimpleName(), request.getId(), userId, tld).setCreateDate(newDate);

    }

    @Override
    public List<RequestHistoryEvent> getTodayHistoryWithUser(String userLogin, UserId userId, TldServiceFacade tld) throws ServiceException {
        throw new NotImplementedException();
    }

    @Override
    public Set<String> getDocumentsHandleLinkedToRequest(Request request, UserId userId, TldServiceFacade tld) throws ServiceException {
        Set<String> handles = new HashSet<String>();

        List<Document> documents = new ArrayList<Document>();

        for (Entry<String, Request> entry : this.mapDocument.entrySet()) {
            if (entry.getValue().getId() == request.getId()) {
                documents.add(AppServiceFacade.getDocumentService().getDocumentWithHandle(entry.getKey(), userId, tld));
            }
        }

        // Trie dans l'ordre de reception
        Collections.sort(documents, new Comparator<Document>() {
            @Override
            public int compare(Document arg0, Document arg1) {
                return arg1.getReceptionDate().compareTo(arg0.getReceptionDate());
            }

        });

        for (int i = 0; i < documents.size(); i++) {
            handles.add(documents.get(i).getHandle());
        }

        return handles;
    }

    @Override
    public void history(Request newVersion, UserId userId, TldServiceFacade tld) throws ServiceException {
        Request oldVersion = this.getRequestById(newVersion.getType(), newVersion.getId(), userId, tld);

        if (!Objects.equal(oldVersion.getStatus(), newVersion.getStatus())) {
            this.addHistoryEvent(RequestHistoryEventField.Status,
                                 oldVersion.getStatus().toString(),
                                 newVersion.getStatus().toString(),
                                 oldVersion,
                                 userId, tld);
        }

        if (!Objects.equal(oldVersion.getComment(), newVersion.getComment())) {
            this.addHistoryEvent(RequestHistoryEventField.Comment,
                                 oldVersion.getComment(),
                                 newVersion.getComment(),
                                 oldVersion,
                                 userId, tld);
        }
    }

    protected void addHistoryEvent(RequestHistoryEventField field, String oldValue, String newValue, Request request, UserId userId, TldServiceFacade tld) throws ServiceException {

        RequestHistoryEvent event = new RequestHistoryEvent();
        event.setId(lastHistoryId++);
        event.setRequestId(request.getId());
        event.setRequestType(request.getClass().getSimpleName());

        event.setField(field);
        event.setOldValue(oldValue);
        event.setNewValue(newValue);

        event.setUser(userId.getObjectOwner(userId, tld).getNicpersId());

        AppServiceFacade.getRequestService().addHistoryEvent(event, userId, tld);
    }

}
