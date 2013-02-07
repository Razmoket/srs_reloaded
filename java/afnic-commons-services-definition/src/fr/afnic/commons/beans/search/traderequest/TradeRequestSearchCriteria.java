/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.beans.search.traderequest;

import java.util.List;

import fr.afnic.commons.beans.request.TradeRequestStatus;
import fr.afnic.commons.beans.search.SearchCriteria;
import fr.afnic.utils.ToStringHelper;

public class TradeRequestSearchCriteria extends SearchCriteria<TradeRequestSearchCriterion> {

    private static final long serialVersionUID = 1L;

    public TradeRequestSearchCriteria() {
        super();
    }

    @Override
    public String toString() {
        return new ToStringHelper().addAllObjectAttributes(this).toString();
    }

    public void setLdhDomainName(String ldhDomainName) {
        this.addCriterion(TradeRequestSearchCriterion.LdhDomainName, ldhDomainName);
    }

    public void setRegistrarCode(String registrarCode) {
        this.addCriterion(TradeRequestSearchCriterion.RegistrarCode, registrarCode);
    }

    public void setHolderNichandle(String holderNichandle) {
        this.addCriterion(TradeRequestSearchCriterion.HolderNichandle, holderNichandle);
    }

    public void setExcludedStatus(List<TradeRequestStatus> excludedStatus) {
        this.addCriterion(TradeRequestSearchCriterion.ExcludedStatus, excludedStatus);
    }

    public void setIncludedStatus(List<TradeRequestStatus> includedStatus) {
        this.addCriterion(TradeRequestSearchCriterion.IncludedStatus, includedStatus);
    }

    public String getLdhDomainName() {
        return (String) this.getCriterionValue(TradeRequestSearchCriterion.LdhDomainName);
    }

    public String getRegistrarCode() {
        return (String) this.getCriterionValue(TradeRequestSearchCriterion.RegistrarCode);
    }

    public String getHolderNichandle() {
        return (String) this.getCriterionValue(TradeRequestSearchCriterion.HolderNichandle);
    }

    @SuppressWarnings("unchecked")
    public List<TradeRequestStatus> getExcludedStatus() {
        return (List<TradeRequestStatus>) this.getCriterionValue(TradeRequestSearchCriterion.ExcludedStatus);
    }

    @SuppressWarnings("unchecked")
    public List<TradeRequestStatus> getIncludedStatus() {
        return (List<TradeRequestStatus>) this.getCriterionValue(TradeRequestSearchCriterion.IncludedStatus);
    }

}
