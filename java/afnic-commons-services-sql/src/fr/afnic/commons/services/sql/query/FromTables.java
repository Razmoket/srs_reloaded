/*
 * $Id: $ $Revision: $ $Author: $
 */

package fr.afnic.commons.services.sql.query;

import java.util.HashSet;
import java.util.Set;

import com.google.common.base.Joiner;

import fr.afnic.commons.beans.validatable.CompoundedInvalidDataDescriptionBuilder;
import fr.afnic.commons.beans.validatable.InvalidDataDescription;

/**
 * From qui contient une liste de tables
 * 
 * @author ginguene
 * 
 */
public class FromTables extends From {
    /**
     * 
     */
    private static final long serialVersionUID = -5646657902622375202L;
    private final Set<Table<?>> tables = new HashSet<Table<?>>();

    public void addTable(final Table<?> table) {
        this.tables.add(table);
    }

    public Set<Table<?>> getTables() {
        return this.tables;
    }

    @Override
    public String toSql() {
        return Joiner.on("\n,").join(this.tables);
    }

    @Override
    public InvalidDataDescription checkInvalidData() {
        final CompoundedInvalidDataDescriptionBuilder builder = new CompoundedInvalidDataDescriptionBuilder(this);
        builder.checkNotEmpty(this.tables, "tables");
        return builder.build();

    }
}
