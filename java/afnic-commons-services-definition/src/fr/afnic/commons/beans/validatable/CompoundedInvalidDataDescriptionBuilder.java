/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.beans.validatable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CompoundedInvalidDataDescriptionBuilder {

    private final String description;
    private final IValidatable validatable;

    public CompoundedInvalidDataDescriptionBuilder(IValidatable validatable) {
        this("Invalid " + validatable.getClass().getSimpleName(), validatable);

    }

    public CompoundedInvalidDataDescriptionBuilder(String description, IValidatable validatable) {
        this.description = description;
        this.validatable = validatable;
    }

    private List<InvalidDataDescription> invalidDataDescriptions = new ArrayList<InvalidDataDescription>();

    public <E extends AbstractValidatable> void checkValidatableListField(List<E> fieldValues, String fieldName) {
        if (fieldValues == null) {
            addNotNullInvalidDataDescription(new NullObjectFieldValueDescription(validatable, fieldName));
        } else {
            for (int i = 0; i < fieldValues.size(); i++) {
                checkValidatableField(fieldValues.get(i), fieldName + "(" + i + ")");
            }
        }
    }

    /**
     * Vérifie tous les élémént d'une collections. <br/>
     * Si la collection est une liste, il vaut mieux utilisé checkValidatableListField, qui est plus précis dans l'utilisation de l'index des éléménts
     * non valides dans la liste (utilisation du get(i) au lieu de l'iterator qui ne garanti pas l'ordre).
     * 
     * @param <E>
     * @param fieldValues
     * @param fieldName
     */
    public <E extends AbstractValidatable> void checkValidatableCollectionField(Collection<E> fieldValues, String fieldName) {
        if (fieldValues == null) {
            addNotNullInvalidDataDescription(new NullObjectFieldValueDescription(validatable, fieldName));
        } else {
            int i = 0;
            for (E fieldValue : fieldValues) {
                checkValidatableField(fieldValue, fieldName + "(" + i + ")");
                i++;
            }
        }
    }

    public void checkValidatableField(AbstractValidatable fieldValue, String fieldName) {

        if (fieldValue == null) {
            addNotNullInvalidDataDescription(new NullObjectFieldValueDescription(validatable, fieldName));
        } else {
            addNotNullInvalidDataDescription(fieldValue.checkInvalidData());
        }
    }

    /**
     * Vérifie qu'une collection n'est pas null ou vide.
     * 
     * @param fieldValue
     * @param fieldName
     */
    public void checkNotEmpty(Collection<?> fieldValue, String fieldName) {
        if (fieldValue == null) {
            addNotNullInvalidDataDescription(new NullObjectFieldValueDescription(validatable, fieldName));
        } else if (fieldValue.isEmpty()) {
            addNotNullInvalidDataDescription(new EmptyListFieldValueDescription(validatable, fieldName));
        }
    }

    public void checkNotEmpty(String fieldValue, String fieldName) {
        if (fieldValue == null) {
            addNotNullInvalidDataDescription(new NullObjectFieldValueDescription(validatable, fieldName));
        } else if (fieldValue.isEmpty()) {
            addNotNullInvalidDataDescription(new InvalidDataDescription(validatable.getClass().getSimpleName() + "." + fieldName + " is empty."));
        }
    }

    public void checkNotNullableField(Object fieldValue, String fieldName) {
        if (fieldValue == null) {
            addNotNullInvalidDataDescription(new NullObjectFieldValueDescription(validatable, fieldName));
        }

    }

    public void addNotNullInvalidDataDescription(InvalidDataDescription invalidDataDescription) {
        if (invalidDataDescription != null) {
            invalidDataDescriptions.add(invalidDataDescription);
        }
    }

    public CompoundedInvalidDataDescription build() {
        if (invalidDataDescriptions.isEmpty()) {
            return null;
        } else {
            return new CompoundedInvalidDataDescription(this.description, invalidDataDescriptions);
        }
    }

}
