/*
 * $Id: $ $Revision: $ $Author: $
 */

package fr.afnic.commons.services.sql.operator;

import java.io.Serializable;

import com.google.common.base.Preconditions;

import fr.afnic.commons.beans.search.SearchCriterionOperator;

/**
 * Handler permettant d'associé un code Sql à un operateur de critère.<br/>
 * Assure le mapping entre les crières et les opérateurs Sql.
 * 
 * @author ginguene
 * 
 */
public class GenericSqlOperator implements ISqlOperator, Serializable {

    private static final long serialVersionUID = 1L;

    protected SearchCriterionOperator operator;
    protected ISqlOperator sqlOperator;

    public GenericSqlOperator(final SearchCriterionOperator operator) {
        this.operator = Preconditions.checkNotNull(operator, "operator cannot be null");

        switch (operator) {
        case Equals:
            this.sqlOperator = new EqualsOperator();
            break;

        case Superior:
            this.sqlOperator = new SuperiorOperator();
            break;

        case Inferior:
            this.sqlOperator = new InferiorOperator();
            break;

        case Contains:
            this.sqlOperator = new ContainsOperator();
            break;

        default:
            throw new RuntimeException("No ISqlOperator for SearchCriterionOperator  " + operator);
        }

    }

    @Override
    public String toSql(final String element1, final String element2) {
        return this.sqlOperator.toSql(element1, element2);
    }

    @Override
    public Object getValue(final Object value) {
        return this.sqlOperator.getValue(value);
    }
}
