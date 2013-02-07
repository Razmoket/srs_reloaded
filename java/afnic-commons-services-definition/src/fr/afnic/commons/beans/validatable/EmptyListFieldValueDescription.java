/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.beans.validatable;

public class EmptyListFieldValueDescription extends InvalidObjectFieldValueDescription {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public EmptyListFieldValueDescription(Object object, String field) {
        super(object, field, null);
    }

}
