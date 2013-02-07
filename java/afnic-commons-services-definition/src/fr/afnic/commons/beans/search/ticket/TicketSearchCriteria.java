/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.beans.search.ticket;

import java.util.Date;

import com.google.common.base.Preconditions;

import fr.afnic.commons.beans.TicketOperation;
import fr.afnic.commons.beans.TicketStatus;
import fr.afnic.commons.beans.logs.ILogger;
import fr.afnic.commons.beans.search.SearchCriteria;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.utils.DateUtils;
import fr.afnic.utils.ToStringHelper;

public class TicketSearchCriteria extends SearchCriteria<TicketSearchCriterion> {

    private static final long serialVersionUID = 1L;

    private static final ILogger LOGGER = AppServiceFacade.getLoggerService().getLogger(TicketSearchCriteria.class);

    private boolean domainNameSwitch;

    private String domainNameArea;

    public TicketSearchCriteria() {
        super();
        this.domainNameSwitch = false;
    }

    public void setTicketId(String ticketId) {
        this.addCriterion(TicketSearchCriterion.Id, ticketId);
    }

    public void setTicketStatus(TicketStatus status) {
        this.addCriterion(TicketSearchCriterion.Status, status);
    }

    public void setOperation(String operation) {
        this.addCriterion(TicketSearchCriterion.Operation, TicketOperation.valueOf(operation));
    }

    public void setOperation(TicketOperation operation) {
        this.addCriterion(TicketSearchCriterion.Operation, operation);
    }

    public void setHolderHandle(String holderHandle) throws ServiceException {
        this.addCriterion(TicketSearchCriterion.HolderHandle, holderHandle);
    }

    /**
     * Prend n'importe quel format de nom de domaine en entrée mais ne conserve que le format LDH.
     */
    public void setDomainName(String domainName) throws ServiceException {
        this.addCriterion(TicketSearchCriterion.DomainName, domainName);
    }

    public void setDomainNameSwitch(boolean domainNameSwitch) {
        this.domainNameSwitch = domainNameSwitch;
    }

    public void setDomainNameArea(String domainNameArea) {
        if (domainNameArea != null) {
            if ("undefined".equals(domainNameArea)) {
                domainNameArea = null;
            } else {
                domainNameArea = domainNameArea.trim();
            }
        }
        this.domainNameArea = domainNameArea;
        //this.addCriterion(TicketSearchCriterion.DomainNameArea, domainNameArea);
    }

    public void setRegistrarCode(String registrarCode) {
        this.addCriterion(TicketSearchCriterion.RegistrarCode, registrarCode);
    }

    public void setBeginningDate(Date beginningDate) {
        this.addCriterion(TicketSearchCriterion.BeginningCreateDate, DateUtils.clone(beginningDate));
    }

    public void setEndingDate(Date endingDate) {
        this.addCriterion(TicketSearchCriterion.EndingCreateDate, DateUtils.clone(endingDate));
    }

    public void setLegalStructureIdentifier(String legalStructureIdentifier) {
        Preconditions.checkNotNull(legalStructureIdentifier, "legalStructureIdentifier cannot be null.");
        this.addCriterion(TicketSearchCriterion.LegalStructureIdentifier, legalStructureIdentifier);
    }

    public String getTicketId() {
        return (String) this.getCriterionValue(TicketSearchCriterion.Id);
    }

    public String getHolderHandle() {
        return (String) this.getCriterionValue(TicketSearchCriterion.HolderHandle);
    }

    public String getDomainName() {
        return (String) this.getCriterionValue(TicketSearchCriterion.DomainName);
    }

    public boolean getDomainNameSwitch() {
        return this.domainNameSwitch;
    }

    public String getDomainNameArea() {
        return this.domainNameArea;
    }

    public String getRegistrarCode() {
        return (String) this.getCriterionValue(TicketSearchCriterion.RegistrarCode);
    }

    public Date getBeginningDate() {
        return (Date) this.getCriterionValue(TicketSearchCriterion.BeginningCreateDate);
    }

    public Date getEndingDate() {
        return (Date) this.getCriterionValue(TicketSearchCriterion.EndingCreateDate);
    }

    public TicketOperation getOperation() {
        return (TicketOperation) this.getCriterionValue(TicketSearchCriterion.Operation);
    }

    public TicketStatus getTicketStatus() {
        return (TicketStatus) this.getCriterionValue(TicketSearchCriterion.Status);
    }

    @Override
    public String toString() {
        return new ToStringHelper().addAllObjectAttributes(this).toString();
    }

}
