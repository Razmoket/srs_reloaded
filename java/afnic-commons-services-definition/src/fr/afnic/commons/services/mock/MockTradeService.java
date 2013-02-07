/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.services.mock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import fr.afnic.commons.beans.documents.GddDocument;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.beans.request.TradeRequest;
import fr.afnic.commons.beans.request.TradeRequestStatus;
import fr.afnic.commons.beans.search.traderequest.TradeRequestSearchCriteria;
import fr.afnic.commons.services.ITradeService;
import fr.afnic.commons.services.exception.NotFoundException;
import fr.afnic.commons.services.exception.NotImplementedException;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.commons.utils.Preconditions;
import fr.afnic.utils.filters.Filter;

/**
 * Implémentation Mock du ITradeService
 * 
 * @TODO ajouter le service de contrat pour le service ITradeService
 * 
 * @author ginguene
 * 
 */
public class MockTradeService implements ITradeService {

    private int nextTradeRequestId = 1;

    /** Clé: id de la requete de trade; valeur: requete de trade */
    private final HashMap<Integer, TradeRequest> idMap = new HashMap<Integer, TradeRequest>();

    @Override
    public TradeRequest getTradeRequestWithId(int id, UserId userId, TldServiceFacade tld) throws ServiceException {
        TradeRequest tradeRequest = this.idMap.get(id);
        if (tradeRequest == null) {
            throw new NotFoundException("TradeRequest not found", TradeRequest.class);
        } else {
            return tradeRequest;
        }
    }

    @Override
    public List<TradeRequest> getTradeRequests(UserId userId, TldServiceFacade tld) throws ServiceException {
        List<TradeRequest> ret = new ArrayList<TradeRequest>();
        ret.addAll(this.idMap.values());
        return ret;
    }

    @Override
    public List<TradeRequest> getTradeRequestsToDisplay(UserId userId, TldServiceFacade tld) throws ServiceException {
        throw new NotImplementedException();
    }

    @Override
    public List<TradeRequest> getTradeRequestsToExpire(UserId userId, TldServiceFacade tld) throws ServiceException {
        throw new NotImplementedException();
    }

    @Override
    public List<TradeRequest> getPendingTradeRequests(UserId userId, TldServiceFacade tld) throws ServiceException {
        Filter<TradeRequest> filter = new Filter<TradeRequest>() {
            @Override
            public boolean test(TradeRequest element) {
                return element.getStatus() != TradeRequestStatus.Finished
                       && element.getStatus() != TradeRequestStatus.Aborded
                       && element.getStatus() != TradeRequestStatus.Suppressed;
            }
        };

        return filter.findElements(this.idMap.values());

    }

    @Override
    public int createTradeRequest(TradeRequest tradeRequest, UserId userId, TldServiceFacade tld) throws ServiceException {
        tradeRequest.setId(this.nextTradeRequestId++);
        this.idMap.put(tradeRequest.getId(), tradeRequest);
        return tradeRequest.getId();
    }

    @Override
    public void updateTradeRequest(TradeRequest tradeRequest, UserId userId, TldServiceFacade tld) throws ServiceException {
        this.idMap.put(tradeRequest.getId(), tradeRequest);
        AppServiceFacade.getRequestService().history(tradeRequest, userId, tld);
    }

    @Override
    public List<TradeRequest> getTradeRequestsWithDomain(String domain, UserId userId, TldServiceFacade tld) throws ServiceException {
        throw new NotImplementedException();
    }

    @Override
    public TradeRequest getRequestToLinkWithDocument(final GddDocument document, UserId userId, TldServiceFacade tld) throws ServiceException {
        Filter<TradeRequest> filter = new Filter<TradeRequest>() {
            @Override
            public boolean test(TradeRequest element) {
                return element.getDomainName().equals(document.getDomain());
            }
        };

        return Preconditions.checkNotNull(filter.findFirstElement(this.getPendingTradeRequests(userId, tld)),
                                          new NotFoundException("No request found to link document " + document.getHandle()));

    }

    @Override
    public List<TradeRequest> searchTradeRequest(TradeRequestSearchCriteria criteria, UserId userId, TldServiceFacade tld) throws ServiceException {
        // TODO Auto-generated method stub
        return null;
    }
}
