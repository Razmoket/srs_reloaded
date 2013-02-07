package fr.afnic.commons.services.multitld;

import java.util.List;

import fr.afnic.commons.beans.documents.GddDocument;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.beans.request.TradeRequest;
import fr.afnic.commons.beans.search.traderequest.TradeRequestSearchCriteria;
import fr.afnic.commons.services.ITradeService;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class MultiTldTradeService implements ITradeService {

    protected MultiTldTradeService() {
        super();
    }

    @Override
    public TradeRequest getTradeRequestWithId(int id, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getTradeService().getTradeRequestWithId(id, userId, tld);
    }

    @Override
    public List<TradeRequest> getTradeRequests(UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getTradeService().getTradeRequests(userId, tld);
    }

    @Override
    public List<TradeRequest> getTradeRequestsToDisplay(UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getTradeService().getTradeRequestsToDisplay(userId, tld);
    }

    @Override
    public List<TradeRequest> searchTradeRequest(TradeRequestSearchCriteria criteria, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getTradeService().searchTradeRequest(criteria, userId, tld);
    }

    @Override
    public List<TradeRequest> getTradeRequestsToExpire(UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getTradeService().getTradeRequestsToExpire(userId, tld);
    }

    @Override
    public List<TradeRequest> getPendingTradeRequests(UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getTradeService().getPendingTradeRequests(userId, tld);
    }

    @Override
    public int createTradeRequest(TradeRequest tradeRequest, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getTradeService().createTradeRequest(tradeRequest, userId, tld);
    }

    @Override
    public void updateTradeRequest(TradeRequest tradeRequest, UserId userId, TldServiceFacade tld) throws ServiceException {
        tld.getServiceProvider().getTradeService().updateTradeRequest(tradeRequest, userId, tld);
    }

    @Override
    public List<TradeRequest> getTradeRequestsWithDomain(String domain, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getTradeService().getTradeRequestsWithDomain(domain, userId, tld);
    }

    @Override
    public TradeRequest getRequestToLinkWithDocument(GddDocument document, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getTradeService().getRequestToLinkWithDocument(document, userId, tld);
    }
}
