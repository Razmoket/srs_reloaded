/*
 * $Id: $ $Revision: $ $Author: $
 */

package fr.afnic.commons.services.sql.query;

import java.util.HashSet;
import java.util.Set;

import com.google.common.base.Joiner;

import fr.afnic.commons.beans.validatable.CompoundedInvalidDataDescriptionBuilder;
import fr.afnic.commons.beans.validatable.InvalidDataDescription;

public class Select<E extends SelectElement> extends AbstractSqlObject {

    /**
     * 
     */
    private static final long serialVersionUID = -6771431167800358208L;
    private final Set<E> elements = new HashSet<E>();

    protected void add(final E element) {
        this.elements.add(element);
    }

    @Override
    public String toSql() {
        this.validate();
        return Joiner.on(",").join(this.elements).toString();
    }

    @Override
    public InvalidDataDescription checkInvalidData() {
        final CompoundedInvalidDataDescriptionBuilder builder = new CompoundedInvalidDataDescriptionBuilder(this);
        builder.checkValidatableCollectionField(this.elements, "elements");
        return builder.build();
    }
}
