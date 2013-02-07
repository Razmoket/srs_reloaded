/*
 * $Id: $ $Revision: $ $Author: $
 */

package fr.afnic.commons.services.sql.query;

import com.google.common.base.Preconditions;

import fr.afnic.commons.beans.validatable.InvalidDataDescription;

/**
 * Représentation d'un tablespace d'une base de donnée.
 * 
 * @author ginguene
 * 
 */
public class TableSpace extends AbstractSqlObject {

    /** 
     * 
     */
    private static final long serialVersionUID = 2524055583708286953L;
    private final String name;

    public TableSpace(final String name) {
        this.name = Preconditions.checkNotNull(name, "name cannot be null");
    }

    public String getName() {
        return this.name;
    }

    /**
     * Créer une table pour ce tablespace.
     * 
     * @param <E>
     * @param tableName
     * @return
     */
    public <E extends TableField<E>> Table<E> createTable(final String tableName) {
        final Table<E> table = new Table<E>(tableName, this);
        return table;
    }

    @Override
    public String toSql() {
        return this.name;
    }

    @Override
    public InvalidDataDescription checkInvalidData() {
        return this.checkInvalidSqlName(this.name, this.name);
    }
}
