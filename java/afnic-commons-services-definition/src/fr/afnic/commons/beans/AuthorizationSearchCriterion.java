package fr.afnic.commons.beans;

import fr.afnic.commons.beans.search.SearchCriterion;
import fr.afnic.commons.beans.search.SearchCriterionOperator;

public enum AuthorizationSearchCriterion implements SearchCriterion {

    DomainName(SearchCriterionOperator.Equals),
    RegistrarCode(SearchCriterionOperator.Equals),
    HolderHandle(SearchCriterionOperator.Equals),
    UsableOnly(SearchCriterionOperator.Equals);

    private final SearchCriterionOperator operator;

    private AuthorizationSearchCriterion(SearchCriterionOperator operator) {
        this.operator = operator;
    }

    @Override
    public SearchCriterionOperator getOperator() {
        return this.operator;
    }

}
