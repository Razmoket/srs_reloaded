/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.beans.search.customer;

import fr.afnic.commons.beans.search.SearchCriterion;
import fr.afnic.commons.beans.search.SearchCriterionOperator;

public enum CustomerSearchCriterion implements SearchCriterion {

    Name(SearchCriterionOperator.Contains),
    CustomerNumber(SearchCriterionOperator.Equals),
    Code(SearchCriterionOperator.Equals),
    BusinessContactRegionCode(SearchCriterionOperator.Equals),
    BusinessContactCountryCode(SearchCriterionOperator.Equals),
    ContractType(SearchCriterionOperator.Contains),
    BenefitTypes(SearchCriterionOperator.Contains);

    private SearchCriterionOperator operator;

    private CustomerSearchCriterion(SearchCriterionOperator operator) {
        this.operator = operator;
    }

    @Override
    public SearchCriterionOperator getOperator() {
        return this.operator;
    }

}
