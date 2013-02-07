/*
 * $Id: $ $Revision: $ $Author: $
 */

package fr.afnic.commons.services.sql.query;

import fr.afnic.commons.beans.validatable.CompoundedInvalidDataDescriptionBuilder;
import fr.afnic.commons.beans.validatable.InvalidDataDescription;

/**
 * From qui inclu une requete.
 * 
 * ex: from ( select person.name from person where person.id = 1 ) c
 * 
 * @author ginguene
 * 
 */
public class FromQuery extends From {

    /**
     * 
     */
    private static final long serialVersionUID = 7772955237662159619L;
    private final AbstractQuery query;

    public FromQuery(final AbstractQuery query) {
        this.query = query;
    }

    @Override
    public String toSql() {
        this.validate();
        return "(\n" + this.query.toSql() + "\n) c ";
    }

    @Override
    public InvalidDataDescription checkInvalidData() {
        final CompoundedInvalidDataDescriptionBuilder builder = new CompoundedInvalidDataDescriptionBuilder(this);
        builder.checkValidatableField(this.query, "query");
        return builder.build();
    }
}
