/*
 * $Id: $ $Revision: $ $Author: $
 */

package fr.afnic.commons.services.sql.operator;

public class ContainsOperator implements ISqlOperator {

    /**
     * 
     */
    private static final long serialVersionUID = 6145745044852744077L;

    @Override
    public String toSql(final String element1, final String element2) {
        return element1 + " like " + element2 + "";
    }

    @Override
    public Object getValue(final Object value) {
        if (value == null) {
            return "%%";
        } else {
            return "%" + value.toString() + "%";
        }
    }

}
