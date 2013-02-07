/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.beans.search.traderequest;

import fr.afnic.commons.beans.search.SearchCriterion;
import fr.afnic.commons.beans.search.SearchCriterionOperator;

public enum TradeRequestSearchCriterion implements SearchCriterion {

    LdhDomainName(SearchCriterionOperator.Equals),
    RegistrarCode(SearchCriterionOperator.Equals),
    HolderNichandle(SearchCriterionOperator.Equals),
    IncludedStatus(SearchCriterionOperator.Contains),
    ExcludedStatus(SearchCriterionOperator.NotContains);

    private SearchCriterionOperator operator;

    private TradeRequestSearchCriterion(SearchCriterionOperator operator) {
        this.operator = operator;
    }

    @Override
    public SearchCriterionOperator getOperator() {
        return this.operator;
    }

}
