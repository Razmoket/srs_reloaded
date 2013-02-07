/*
 * $Id: $ $Revision: $ $Author: $
 */

package fr.afnic.commons.services.sql.operator;

public class EqualsOperator implements ISqlOperator {

    private static final long serialVersionUID = -2594352320428128903L;

    @Override
    public String toSql(final String element1, final String element2) {
        return element1 + " = " + element2;
    }

    @Override
    public Object getValue(final Object value) {
        return value;
    }

}
