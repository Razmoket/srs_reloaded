/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.beans.validatable;

public class NullObjectFieldValueDescription extends InvalidObjectFieldValueDescription {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public NullObjectFieldValueDescription(Object object, String field) {
        super(object, field, null);
    }

}
