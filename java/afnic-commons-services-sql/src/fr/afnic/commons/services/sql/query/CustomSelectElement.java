/*
 * $Id: $ $Revision: $ $Author: $
 */

package fr.afnic.commons.services.sql.query;

import fr.afnic.commons.beans.validatable.InvalidDataDescription;
import fr.afnic.commons.utils.Preconditions;

/**
 * Element de selection représenté par une String invariable
 * 
 * @author ginguene
 * 
 */
public class CustomSelectElement extends SelectElement {

    /**
     * 
     */
    private static final long serialVersionUID = -8606888949657544364L;
    private final String value;

    public CustomSelectElement(final String value) {
        this.value = Preconditions.checkNotBlank(value, "value");
    }

    @Override
    public String toSql() {
        return this.value;
    }

    @Override
    public InvalidDataDescription checkInvalidData() {
        // Test effectué dans le constructeur.
        return null;
    }

}
