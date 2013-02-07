/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/beans/corporateentity/CorporateEntityIdentifier.java#1 $
 * $Revision: #1 $
 * $Author: ginguene $
 */

package fr.afnic.commons.beans.corporateentity;

import org.apache.commons.lang.StringUtils;

import fr.afnic.commons.beans.validatable.ObjectValue;
import fr.afnic.commons.checkers.IInternalChecker;
import fr.afnic.commons.checkers.NotNullChecker;

/**
 * Identifiant d'une structure légale. Ces identifiant sont référencés dans des bases publiques et permettent d'obtenir des informations sur la
 * structure légale qu'ils identifient.
 * 
 * @author ginguene
 * 
 */
public abstract class CorporateEntityIdentifier extends ObjectValue {

    private static final long serialVersionUID = 1L;

    protected String value;

    public CorporateEntityIdentifier(String value) {
        super(value);
    }

    /**
     * Retourne une description de l'identifiant.<br/>
     * ex: pour un Siren on aura "Siren 123456789"
     * 
     * 
     * @return Une description de l'identifiant.
     */
    public String getDescription() {
        return this.getName() + " " + this.getValue();
    }

    @Override
    protected IInternalChecker createChecker() {
        return new NotNullChecker();
    }

    public boolean hasNotEmptyValue() {
        return !this.hasEmptyValue();
    }

    public boolean hasEmptyValue() {
        return StringUtils.isNotEmpty(this.value);
    }

}
