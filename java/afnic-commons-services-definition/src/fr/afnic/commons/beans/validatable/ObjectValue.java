/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/beans/validatable/ObjectValue.java#11 $
 * $Revision: #11 $
 * $Author: ginguene $
 */

package fr.afnic.commons.beans.validatable;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

import fr.afnic.commons.checkers.IInternalChecker;
import fr.afnic.commons.services.exception.invalidformat.InvalidFormatException;
import fr.afnic.utils.ToStringHelper;

/**
 * Représente un objet simple caractérisé par une valeur immuable qui est passé au constructeur.<br/>
 * Exemple: addresse mail, code pays, nichandle.
 * 
 * 
 * @author ginguene
 * 
 */
public abstract class ObjectValue extends AbstractValidatable implements Serializable {

    private static final long serialVersionUID = 1L;

    protected final String value;

    private IInternalChecker checker;

    public ObjectValue() {
        this.value = null;
    }

    public ObjectValue(String value) {
        this.value = value;
        this.checker = this.createChecker();
    }

    public String getName() {
        return this.getClass().getSimpleName();
    }

    public String getValue() {
        return this.value;
    }

    @Override
    public int hashCode() {
        if (this.value != null) {
            return this.value.hashCode();
        } else {
            return super.hashCode();
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !this.getClass().equals(obj.getClass())) {
            return false;
        } else {
            ObjectValue objectValue = (ObjectValue) obj;
            return StringUtils.equals(this.value, objectValue.getValue());
        }
    }

    @Override
    public InvalidDataDescription checkInvalidData() {
        if (this.checker == null) {
            CompoundedInvalidDataDescriptionBuilder builder = new CompoundedInvalidDataDescriptionBuilder(this);
            builder.checkNotNullableField(this.value, "value");

            return builder.build();
        } else {
            try {
                this.checker.check(this.value);
                return null;
            } catch (InvalidFormatException e) {
                return new InvalidDataDescription("Invalid value " + this.value + " for " + this.getClass().getSimpleName());
            }
        }
    }

    @Override
    public String toString() {
        return new ToStringHelper(this.getClass()).add("value", this.getValue()).toString();
    }

    /**
     * Permet aux classes filles d'indiquer un checker pour valider les données.
     * 
     * @return
     */
    protected IInternalChecker createChecker() {
        return null;
    }

}
