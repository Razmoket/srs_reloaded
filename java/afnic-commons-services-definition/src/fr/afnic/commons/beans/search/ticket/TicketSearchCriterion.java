/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.beans.search.ticket;

import fr.afnic.commons.beans.search.SearchCriterion;
import fr.afnic.commons.beans.search.SearchCriterionOperator;

public enum TicketSearchCriterion implements SearchCriterion {

    Id(SearchCriterionOperator.Equals),
    BeginningCreateDate(SearchCriterionOperator.Superior),
    EndingCreateDate(SearchCriterionOperator.Inferior),
    Operation(SearchCriterionOperator.Equals),
    DomainName(SearchCriterionOperator.Equals),
    HolderHandle(SearchCriterionOperator.Equals),
    RegistrarCode(SearchCriterionOperator.Equals),
    Status(SearchCriterionOperator.Equals),
    LegalStructureIdentifier(SearchCriterionOperator.Contains),
    Handle(SearchCriterionOperator.Equals);

    private SearchCriterionOperator operator;

    private TicketSearchCriterion(SearchCriterionOperator operator) {
        this.operator = operator;
    }

    @Override
    public SearchCriterionOperator getOperator() {
        return this.operator;
    }

}
