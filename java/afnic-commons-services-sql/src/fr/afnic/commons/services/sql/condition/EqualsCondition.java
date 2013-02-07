/*
 * $Id: $ $Revision: $ $Author: $
 */

package fr.afnic.commons.services.sql.condition;

import fr.afnic.commons.beans.search.SearchCriterionOperator;
import fr.afnic.commons.services.exception.invalidformat.InvalidFormatException;
import fr.afnic.commons.services.sql.query.TableField;

public class EqualsCondition extends ComparisonCondition {

    /**
     * 
     */
    private static final long serialVersionUID = -2913013317531336417L;

    public EqualsCondition(final TableField<?> field, final Object value) throws InvalidFormatException {
        super(field, value, SearchCriterionOperator.Equals);
    }

}
