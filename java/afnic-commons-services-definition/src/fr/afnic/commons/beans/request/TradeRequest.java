/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/beans/request/TradeRequest.java#17 $
 * $Revision: #17 $
 * $Author: barriere $
 */

package fr.afnic.commons.beans.request;

import java.util.Date;

import fr.afnic.commons.beans.Ticket;
import fr.afnic.commons.beans.TradeTicket;
import fr.afnic.commons.beans.domain.Domain;
import fr.afnic.commons.beans.logs.ILogger;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.RequestFailedException;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.utils.DateUtils;

/**
 * 
 * Correspond a une demande de validation de Trade par Fax. Ici on ne gère que les Trade ayant reçu une réponse par fax. On ignore tous ceux qui
 * rçoivent une réponse par mail et qui sont gérés par un autre processus.
 * 
 * 
 * Il est a noté que la tradeRequest est liés à un ticket. Si ce ticket n'est pas fini c'est un tradeTicket. Quand il est fini il s'agit d'un simple
 * ticket. C'est la chaine qui fait ces transformation...
 * 
 * @author ginguene
 * 
 */
public class TradeRequest extends Request<TradeRequestStatus> {

    private static final long serialVersionUID = 1L;

    private static final ILogger LOGGER = AppServiceFacade.getLoggerService().getLogger(TradeRequest.class);
    private static final int NB_DAY_BEFORE_EXPIRATION = 15;

    private String ticketId;
    private TradeTicket tradeTicket;
    private Ticket ticket;
    private String domainName;
    private Domain domain;

    public TradeRequest(UserId userId, TldServiceFacade tld) {
        super(userId, tld);
    }

    public String getTicketId() {
        return this.ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public TradeTicket getTradeTicket() throws ServiceException {
        if (this.tradeTicket == null && this.ticketId != null) {
            try {
                this.tradeTicket = (TradeTicket) AppServiceFacade.getTicketService().getTicketWithId(this.ticketId, this.userIdCaller, this.tldCaller);
            } catch (ClassCastException cce) {
                // Le ticket est fermé et perd ses attribut de TradeTicket
                return null;
            }
        }
        return this.tradeTicket;
    }

    public Ticket getTicket() throws ServiceException {
        if (this.ticket == null) {
            this.ticket = AppServiceFacade.getTicketService()
                                          .getTicketWithId(this.ticketId, this.userIdCaller, this.tldCaller);
        }
        return this.ticket;
    }

    /**
     * initialise le status a partir d'une String. Retourne false si la string ne correspond à aucun statut
     * 
     * @see fr.afnic.gateway.
     * 
     * @param status
     * @return
     */
    public boolean setStatusFromString(String status) {
        try {
            this.status = TradeRequestStatus.valueOf(status);
            return true;
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return false;
        }
    }

    public Domain getDomain() throws ServiceException {
        if (this.domain == null && this.domainName != null) {
            try {
                LOGGER.debug("update domain for trade request " + this.getId());
                this.domain = AppServiceFacade.getDomainService()
                                              .getDomainWithName(this.domainName, this.userIdCaller, this.tldCaller);
            } catch (RequestFailedException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
        return this.domain;
    }

    public String getDomainName() {
        return this.domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    @Override
    public void update() throws ServiceException {

        AppServiceFacade.getTradeService()
                        .updateTradeRequest(this, this.userIdCaller.getObjectOwner(this.userIdCaller, this.tldCaller).getId(), this.tldCaller);
    }

    public boolean hasTradeTicket() throws ServiceException {
        return this.getTradeTicket() != null;
    }

    @Override
    public boolean isExpired() {
        return new Date().after(this.getExpirationDate());
    }

    public Date getExpirationDate() {
        return DateUtils.getNbDaysLaterFromDate(NB_DAY_BEFORE_EXPIRATION, this.createDate);
    }

    @Override
    protected RequestStatus getStatus(String statusAsString) {
        return TradeRequestStatus.valueOf(statusAsString);
    }

    public String getRegistrarCode() {
        try {
            if (this == null || this.getDomain() == null) {
                return "";
            } else {
                return this.getDomain().getRegistrarCode();
            }
        } catch (final ServiceException e) {
            LOGGER.error("getRegistrarCode() failed with tradeRequest " + this.getId(), e);
            return "";
        }
    }

    @Override
    public void attachResponse(String documentHandle) throws ServiceException {
        this.addDocument(documentHandle);
        if (!this.getStatus().isFinalStatus()) {
            this.setStatus(TradeRequestStatus.Answered);
            this.update();
        }
    }

}
