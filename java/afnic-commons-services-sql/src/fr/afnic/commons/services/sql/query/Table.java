/*
 * $Id: $ $Revision: $ $Author: $
 */

package fr.afnic.commons.services.sql.query;

import com.google.common.base.Objects;

import fr.afnic.commons.beans.validatable.CompoundedInvalidDataDescriptionBuilder;
import fr.afnic.commons.beans.validatable.InvalidDataDescription;
import fr.afnic.commons.services.exception.invalidformat.InvalidFormatException;
import fr.afnic.commons.services.sql.condition.Join;
import fr.afnic.commons.services.sql.join.JoinFinder;
import fr.afnic.commons.services.sql.join.JoinNotFoundException;
import fr.afnic.commons.utils.Preconditions;

public class Table<E extends TableField<?>> extends AbstractSqlObject {

    private static final long serialVersionUID = 1L;

    private final String name;
    private final TableSpace tableSpace;

    public Table(final String name, final TableSpace tableSpace) {
        this.name = Preconditions.checkNotNull(name, "name");
        this.tableSpace = Preconditions.checkNotNull(tableSpace, "tableSpace");
    }

    @Override
    public String toSql() {
        return this.tableSpace.getName() + "." + this.name;
    }

    @Override
    public boolean equals(final Object object) {
        if (object instanceof Table) {
            final Table<?> table = (Table<?>) object;
            return Objects.equal(this.name, table.name) && Objects.equal(this.tableSpace, table.tableSpace);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.toSql().hashCode();
    }

    /**
     * Retourne la condition de jointure entre cette table et une autre passée en parametre.
     * 
     * @param table
     * @return
     * @throws JoinNotFoundException
     * @throws InvalidFormatException
     */
    public Join findJoinWithTable(final Table<?> table) throws JoinNotFoundException {
        return JoinFinder.findJoin(this, table);
    }

    @Override
    public InvalidDataDescription checkInvalidData() {
        final CompoundedInvalidDataDescriptionBuilder builder = new CompoundedInvalidDataDescriptionBuilder(this);
        builder.addNotNullInvalidDataDescription(this.checkInvalidSqlName(this.name, this.name));
        builder.checkValidatableField(this.tableSpace, "tableSpace");
        return builder.build();
    }

}
