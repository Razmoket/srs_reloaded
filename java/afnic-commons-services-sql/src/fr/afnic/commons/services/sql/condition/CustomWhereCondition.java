/*
 * $Id: $ $Revision: $ $Author: $
 */

package fr.afnic.commons.services.sql.condition;

import org.apache.commons.lang.StringUtils;

import fr.afnic.commons.beans.validatable.InvalidDataDescription;
import fr.afnic.commons.beans.validatable.InvalidDataException;

/**
 * Element de selection représenté par une String invariable
 * 
 * @author ginguene
 * 
 */
public class CustomWhereCondition extends AbstractWhereCondition {

    /**
     * 
     */
    private static final long serialVersionUID = -5612522205981129677L;
    private final String value;

    public CustomWhereCondition(final String value) {
        this.value = value;
    }

    @Override
    public String toSql() throws InvalidDataException {
        this.validate();
        return this.value;
    }

    @Override
    public InvalidDataDescription checkInvalidData() {
        if (StringUtils.isBlank(this.value)) {
            return new InvalidDataDescription("CustomWhereCondition.value cannot be null");
        } else {
            return null;
        }
    }
}
