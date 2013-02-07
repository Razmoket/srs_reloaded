/*
 * $Id: $ $Revision: $ $Author: $
 */

package fr.afnic.commons.services.sql.query;

import com.google.common.base.Objects;

import fr.afnic.commons.beans.validatable.AbstractValidatable;
import fr.afnic.commons.beans.validatable.InvalidDataDescription;
import fr.afnic.commons.beans.validatable.InvalidDataException;
import fr.afnic.commons.services.exception.invalidformat.InvalidFormatException;
import fr.afnic.commons.services.sql.checker.SqlNameChecker;

public abstract class AbstractSqlObject extends AbstractValidatable {

    private static final long serialVersionUID = 5939117723192564454L;

    private static final SqlNameChecker NAME_CHECKER = new SqlNameChecker();

    /**
     * Permet d'utiliser Joiner pour assembler des AbstractSqlObject.
     * 
     */
    @Override
    public String toString() {
        return this.toSql();
    }

    @Override
    public int hashCode() {
        if (this.toString() != null) {
            return this.toString().hashCode();
        } else {
            return 1;
        }
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;

        if (obj.getClass() == this.getClass()) {
            return Objects.equal(this.toString(), obj.toString());
        } else {
            return false;
        }
    }

    /**
     * Retourne le code sql de l'objet
     * 
     * @throw InvalidFormatException levée si les données de l'objet ne sont pas valide.
     * 
     * @return
     */
    public abstract String toSql() throws InvalidDataException;

    protected void validateSqlName(final String name) throws InvalidFormatException {
        AbstractSqlObject.NAME_CHECKER.check(name);
    }

    protected InvalidDataDescription checkInvalidSqlName(final String sqlName, final String fieldName) {
        try {
            AbstractSqlObject.NAME_CHECKER.check(sqlName);
            return null;
        } catch (final InvalidFormatException e) {
            return new InvalidDataDescription(fieldName + " does not contain a valid Sql name [" + sqlName + "]");
        }

    }
}
