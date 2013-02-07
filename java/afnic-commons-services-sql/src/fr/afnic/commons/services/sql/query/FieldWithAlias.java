/*
 * $Id: $ $Revision: $ $Author: $
 */

package fr.afnic.commons.services.sql.query;

import fr.afnic.commons.utils.Preconditions;

/**
 * Champs avec un alias
 * 
 * @author ginguene
 * 
 * @param <E>
 */
public final class FieldWithAlias<E extends TableField<E>> extends TableField<E> {

    private static final long serialVersionUID = 8506153453024119151L;

    private final String alias;

    public static <E extends TableField<E>> FieldWithAlias<E> create(final TableField<E> field, final String alias) {
        return new FieldWithAlias<E>(field, alias);
    }

    private FieldWithAlias(final TableField<E> field, final String alias) {
        super(field.getName(), field.getTable());
        this.alias = Preconditions.checkNotBlank(alias, "alias");
    }

    public String getALias() {
        return this.alias;
    }

    @Override
    public String toSql() {
        return super.toSql() + " as " + this.alias;
    }

}
