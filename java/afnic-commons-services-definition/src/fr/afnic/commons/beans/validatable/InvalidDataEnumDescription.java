/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.beans.validatable;

public class InvalidDataEnumDescription extends InvalidDataDescription {

    private static final long serialVersionUID = 1L;

    private InvalidDataDescriptionEnum descriptionEnum = null;

    public InvalidDataEnumDescription(InvalidDataDescriptionEnum descriptionEnum) {
        super(descriptionEnum.toString());
        this.descriptionEnum = descriptionEnum;
    }

    @Override
    public String getDictionaryKey() {
        return this.descriptionEnum.toString();
    }

}
