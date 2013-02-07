/*
 * $Id: $ $Revision: $ $Author: $
 */

package fr.afnic.commons.services.sql.query;

import fr.afnic.commons.beans.validatable.CompoundedInvalidDataDescriptionBuilder;
import fr.afnic.commons.beans.validatable.InvalidDataDescription;
import fr.afnic.commons.utils.Preconditions;

/**
 * Correspond au champs d'une table à mettre dans le select
 * 
 * @author ginguene
 * 
 * @param <F>
 */
public abstract class TableField<F extends TableField<F>> extends Field {

    private static final long serialVersionUID = -3395008255564385602L;

    private final String name;
    private final Table<F> table;

    public TableField(final String name, final Table<F> table) {
        this.name = Preconditions.checkNotBlank(name, "name");
        this.table = Preconditions.checkNotNull(table, "table");
    }

    public String getName() {
        return this.name;
    }

    public Table<F> getTable() {
        return this.table;
    }

    @Override
    public String toSql() {
        return this.table.toSql() + "." + this.name;
    }

    @Override
    public InvalidDataDescription checkInvalidData() {
        final CompoundedInvalidDataDescriptionBuilder builder = new CompoundedInvalidDataDescriptionBuilder(this);
        builder.addNotNullInvalidDataDescription(this.checkInvalidSqlName(this.name, this.name));
        builder.checkValidatableField(this.table, "table");
        return builder.build();
    }
}
