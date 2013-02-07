/*
 * $Id: $ $Revision: $ $Author: $
 */

package fr.afnic.commons.services.sql.condition;

import java.util.ArrayList;
import java.util.List;

import fr.afnic.commons.beans.search.SearchCriterionOperator;
import fr.afnic.commons.services.sql.query.TableField;

/**
 * Condition de jointures entre 2 tables.
 */
public class Join extends ComparisonCondition {

    private static final long serialVersionUID = -7211498223571140658L;

    public Join(final TableField<?> field1, final TableField<?> field2) {
        super(field1, field2, SearchCriterionOperator.Equals);
    }

    @Override
    public List<TableField<?>> getFields() {
        final List<TableField<?>> fields = new ArrayList<TableField<?>>();
        fields.add(this.field);
        fields.add((TableField<?>) this.value);
        return fields;
    }

    @Override
    public String toSql() {
        return this.operator.toSql(this.field.toSql(), ((TableField<?>) this.value).toSql());
    }

    @Override
    public void populateParameters(final List<Object> parameters) {
    }

}