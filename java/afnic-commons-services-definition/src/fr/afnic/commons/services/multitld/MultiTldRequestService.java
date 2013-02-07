package fr.afnic.commons.services.multitld;

import java.util.Date;
import java.util.List;
import java.util.Set;

import fr.afnic.commons.beans.documents.Document;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.beans.request.Request;
import fr.afnic.commons.beans.request.RequestHistoryEvent;
import fr.afnic.commons.beans.request.RequestStatus;
import fr.afnic.commons.services.IRequestService;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class MultiTldRequestService implements IRequestService {

    protected MultiTldRequestService() {
        super();
    }

    @Override
    public Request getRequestById(String type, int id, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getRequestService().getRequestById(type, id, userId, tld);
    }

    @Override
    public boolean addHistoryEvent(RequestHistoryEvent event, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getRequestService().addHistoryEvent(event, userId, tld);
    }

    @Override
    public List<RequestHistoryEvent> getHistory(String type, int requestId, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getRequestService().getHistory(type, requestId, userId, tld);
    }

    @Override
    public void history(Request request, UserId userId, TldServiceFacade tld) throws ServiceException {
        tld.getServiceProvider().getRequestService().history(request, userId, tld);
    }

    @Override
    public Request getLinkedRequestToDocumentWithHandle(String handle, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getRequestService().getLinkedRequestToDocumentWithHandle(handle, userId, tld);
    }

    @Override
    public String getRequestComment(Request request, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getRequestService().getRequestComment(request, userId, tld);
    }

    @Override
    public Date getFirstTimeStatusForRequest(RequestStatus status, Request request, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getRequestService().getFirstTimeStatusForRequest(status, request, userId, tld);
    }

    @Override
    public Date getLastTimeStatusForRequest(RequestStatus status, Request request, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getRequestService().getLastTimeStatusForRequest(status, request, userId, tld);
    }

    @Override
    public void cancelRequest(Request request, UserId userId, TldServiceFacade tld) throws ServiceException {
        tld.getServiceProvider().getRequestService().cancelRequest(request, userId, tld);
    }

    @Override
    public void deleteRequest(Request request, String login, UserId userId, TldServiceFacade tld) throws ServiceException {
        tld.getServiceProvider().getRequestService().deleteRequest(request, login, userId, tld);
    }

    @Override
    public void updateHistory(RequestHistoryEvent event, UserId userId, TldServiceFacade tld) throws ServiceException {
        tld.getServiceProvider().getRequestService().updateHistory(event, userId, tld);
    }

    @Override
    public void changeLastStatusChange(Request request, Date newDate, UserId userId, TldServiceFacade tld) throws ServiceException {
        tld.getServiceProvider().getRequestService().changeLastStatusChange(request, newDate, userId, tld);
    }

    @Override
    public void changeDateCreation(Request request, Date newDate, UserId userId, TldServiceFacade tld) throws ServiceException {
        tld.getServiceProvider().getRequestService().changeDateCreation(request, newDate, userId, tld);
    }

    @Override
    public List<RequestHistoryEvent> getTodayHistoryWithUser(String userLogin, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getRequestService().getTodayHistoryWithUser(userLogin, userId, tld);
    }

    @Override
    public void linkDocumentToRequest(Document document, Request request, UserId userId, TldServiceFacade tld) throws ServiceException {
        tld.getServiceProvider().getRequestService().linkDocumentToRequest(document, request, userId, tld);
    }

    @Override
    public Set<String> getDocumentsHandleLinkedToRequest(Request request, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getRequestService().getDocumentsHandleLinkedToRequest(request, userId, tld);
    }
}
