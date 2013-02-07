/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/beans/contactdetails/PhoneNumber.java#9 $
 * $Revision: #9 $
 * $Author: barriere $
 */

package fr.afnic.commons.beans.contactdetails;

import fr.afnic.commons.checkers.IInternalChecker;
import fr.afnic.commons.checkers.PhoneNumberChecker;

/**
 * Représente un numéro de téléphone
 * 
 * @author ginguene
 * 
 */
public class PhoneNumber extends ContactDetail {

    private static final long serialVersionUID = 1L;

    public PhoneNumber() {
        super();
    }

    /**
     * Constucteur hérité de Detail
     * 
     * @param value
     *            Numéro de téléphone.
     */
    public PhoneNumber(String value) {
        super(value);
    }

    @Override
    protected IInternalChecker createChecker() {
        return new PhoneNumberChecker();
    }

}
