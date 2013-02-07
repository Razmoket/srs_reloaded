/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.beans.validatable;

import fr.afnic.commons.checkers.IInternalChecker;
import fr.afnic.commons.services.exception.invalidformat.InvalidFormatException;
import fr.afnic.utils.ToStringHelper;

/**
 * Object validable dont on valide la valeur via un checker.<br/>
 * La valeur est immuable.
 * 
 * 
 * 
 * @author ginguene
 * 
 */

public abstract class AbstractValidatableValue extends AbstractValidatable {

    protected final String value;

    protected IInternalChecker checker;

    public AbstractValidatableValue(String value) {
        this.value = value;
        this.checker = this.createChecker();
        if (this.checker == null) {
            throw new RuntimeException(this.getClass().getName() + ".createChecker() returned null.");
        }
    }

    @Override
    public InvalidDataDescription checkInvalidData() {
        try {
            this.checker.check(this.value);
            return null;
        } catch (InvalidFormatException e) {
            return new InvalidObjectFieldValueDescription(this, "value", this.value);
        }
    }

    public String getValue() {
        return value;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.checker == null) ? 0 : this.checker.hashCode());
        result = prime * result + ((this.value == null) ? 0 : this.value.hashCode());
        return result;
    }

    /**
     * Deux AbstractValidatableValue sont égaux si ils utilisent le même type de checker et ont la même valeur.
     * 
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;

        AbstractValidatableValue other = (AbstractValidatableValue) obj;
        if (this.checker == null) {
            if (other.checker != null) return false;
        } else if (!this.checker.getClass().equals(other.checker.getClass())) return false;
        if (this.value == null) {
            if (other.value != null) return false;
        } else if (!this.value.equals(other.value)) return false;
        return true;
    }

    protected abstract IInternalChecker createChecker();

    @Override
    public String toString() {
        return new ToStringHelper(this.getClass().getSimpleName()).add("value", this.value).toString();
    }

}
