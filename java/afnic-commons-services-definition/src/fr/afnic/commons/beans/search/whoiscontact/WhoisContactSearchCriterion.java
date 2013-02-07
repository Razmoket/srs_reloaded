/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.beans.search.whoiscontact;

import fr.afnic.commons.beans.search.SearchCriterion;
import fr.afnic.commons.beans.search.SearchCriterionOperator;

public enum WhoisContactSearchCriterion implements SearchCriterion {

    DomainName(SearchCriterionOperator.Equals),
    DomainNameLike(SearchCriterionOperator.Equals),
    WhoisContactNicHandle(SearchCriterionOperator.Equals),
    WhoisContactName(SearchCriterionOperator.Equals),
    WhoisContactNameLike(SearchCriterionOperator.Equals),
    WhoisContactIdentifier(SearchCriterionOperator.Equals);

    private SearchCriterionOperator operator;

    private WhoisContactSearchCriterion(SearchCriterionOperator operator) {
        this.operator = operator;
    }

    @Override
    public SearchCriterionOperator getOperator() {
        return this.operator;
    }

}
