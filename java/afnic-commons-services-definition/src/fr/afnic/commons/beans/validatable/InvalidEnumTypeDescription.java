/*
 * $Id: $ $Revision: $ $Author: $
 */

package fr.afnic.commons.beans.validatable;

import fr.afnic.commons.beans.enumtype.IInvalidEnumType;

/**
 * Description d'une valeur invalide pour un enumType.
 * 
 * @author ginguene
 * 
 */
public class InvalidEnumTypeDescription extends InvalidDataDescription {

    private static final long serialVersionUID = 7179288469222928150L;
    private final IInvalidEnumType invalidEnumType;

    public InvalidEnumTypeDescription(IInvalidEnumType invalidEnumType) {
        super("Invalid value for enumType " + invalidEnumType.getClass().getSuperclass().getSimpleName() + " " + invalidEnumType.getOriginalValue());
        this.invalidEnumType = invalidEnumType;
    }

    public IInvalidEnumType getInvalidEnumType() {
        return this.invalidEnumType;
    }
}
