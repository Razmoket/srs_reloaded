/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.beans.search.domain;

import fr.afnic.commons.beans.search.SearchCriterion;
import fr.afnic.commons.beans.search.SearchCriterionOperator;

public enum DomainSearchCriterion implements SearchCriterion {

    Siret(SearchCriterionOperator.Equals),
    DomainName(SearchCriterionOperator.Equals),
    RegistrarNameOrCode(SearchCriterionOperator.Equals),
    HolderHandle(SearchCriterionOperator.Equals),
    HolderName(SearchCriterionOperator.Equals),
    ServerName(SearchCriterionOperator.Equals),
    AnniversaryDateDebut(SearchCriterionOperator.Equals),
    AnniversaryDateFin(SearchCriterionOperator.Equals),
    DomainNameLike(SearchCriterionOperator.Contains),
    HolderNameLike(SearchCriterionOperator.Contains),
    RegistrarNameLike(SearchCriterionOperator.Contains);

    private SearchCriterionOperator operator;

    private DomainSearchCriterion(SearchCriterionOperator operator) {
        this.operator = operator;
    }

    @Override
    public SearchCriterionOperator getOperator() {
        return this.operator;
    }

}
