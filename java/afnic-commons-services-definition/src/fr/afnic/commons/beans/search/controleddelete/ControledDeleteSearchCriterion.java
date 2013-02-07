/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.beans.search.controleddelete;

import fr.afnic.commons.beans.search.SearchCriterion;
import fr.afnic.commons.beans.search.SearchCriterionOperator;

public enum ControledDeleteSearchCriterion implements SearchCriterion {

    Id(SearchCriterionOperator.Equals),
    BeginningCreateDate(SearchCriterionOperator.Superior),
    EndingCreateDate(SearchCriterionOperator.Inferior),
    Operation(SearchCriterionOperator.Equals),
    DomainName(SearchCriterionOperator.Equals),
    RegistrarCode(SearchCriterionOperator.Equals),
    Status(SearchCriterionOperator.Equals),
    LegalStructureIdentifier(SearchCriterionOperator.Contains),
    Handle(SearchCriterionOperator.Equals);

    private SearchCriterionOperator operator;

    private ControledDeleteSearchCriterion(SearchCriterionOperator operator) {
        this.operator = operator;
    }

    @Override
    public SearchCriterionOperator getOperator() {
        return operator;
    }

}
