/*
 * $Id: $ $Revision: $ $Author: $
 */

package fr.afnic.commons.services.sql.condition;

import java.util.ArrayList;
import java.util.List;

import fr.afnic.commons.beans.search.SearchCriterionOperator;
import fr.afnic.commons.beans.validatable.CompoundedInvalidDataDescriptionBuilder;
import fr.afnic.commons.beans.validatable.InvalidDataDescription;
import fr.afnic.commons.services.sql.operator.GenericSqlOperator;
import fr.afnic.commons.services.sql.query.TableField;
import fr.afnic.commons.utils.Preconditions;

/**
 * Condition de comparaison
 * 
 * @author ginguene
 * 
 */
public class ComparisonCondition extends WhereCondition {

    private static final long serialVersionUID = 1L;

    protected TableField<?> field;
    protected Object value;
    protected GenericSqlOperator operator;

    public ComparisonCondition(final TableField<?> field, final Object value, final SearchCriterionOperator searchCriterionOperator) {
        this.field = field;
        this.value = value;

        Preconditions.checkParameter(searchCriterionOperator, "searchCriterionOperator cannot be null");
        this.operator = new GenericSqlOperator(searchCriterionOperator);
    }

    @Override
    public String toSql() {
        this.validate();
        return this.operator.toSql(this.field.toSql(), "?");
    }

    @Override
    public List<TableField<?>> getFields() {
        final List<TableField<?>> fields = new ArrayList<TableField<?>>();
        fields.add(this.field);
        return fields;
    }

    @Override
    public void populateParameters(final List<Object> parameters) {

        Object parameter = this.value;

        if (this.value != null) {
            if (this.value instanceof java.util.Date) {
                // Pour la beauté du code, ce test devrait etre effectué ailleurs.
                // Mais le problème ne se pose que pour les Date car dans la 'vrai' vie, on manipule des java.utils.Date
                // Alors que pour les requetes, il faut des java.sql.Date.
                parameter = new java.sql.Timestamp(((java.util.Date) this.value).getTime());
            } else if (this.value.getClass().isEnum()) {
                parameter = this.value.toString();
            }
        }

        parameters.add(this.operator.getValue(parameter));

    }

    @Override
    public InvalidDataDescription checkInvalidData() {
        final CompoundedInvalidDataDescriptionBuilder builder = new CompoundedInvalidDataDescriptionBuilder(this);
        builder.checkValidatableField(this.field, "field");
        builder.checkNotNullableField(this.value, "value");
        return builder.build();
    }
}
