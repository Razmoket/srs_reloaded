/*
 * $Id: $ $Revision: $ $Author: $
 */

package fr.afnic.commons.services.sql.operator;

import java.io.Serializable;

public interface ISqlOperator extends Serializable {

    public String toSql(String element1, String element2);

    public Object getValue(Object value);
}
