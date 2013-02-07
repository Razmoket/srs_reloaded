/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.beans.search.form;

import fr.afnic.commons.beans.search.SearchCriterion;
import fr.afnic.commons.beans.search.SearchCriterionOperator;

public enum FormSearchCriterion implements SearchCriterion {

    FormId(SearchCriterionOperator.Equals),
    DomainName(SearchCriterionOperator.Equals),
    TicketId(SearchCriterionOperator.Equals),
    HolderHandle(SearchCriterionOperator.Equals),
    AdminHandle(SearchCriterionOperator.Equals),
    Registrar(SearchCriterionOperator.Equals);

    private SearchCriterionOperator operator;

    private FormSearchCriterion(SearchCriterionOperator operator) {
        this.operator = operator;
    }

    @Override
    public SearchCriterionOperator getOperator() {
        return this.operator;
    }

}
