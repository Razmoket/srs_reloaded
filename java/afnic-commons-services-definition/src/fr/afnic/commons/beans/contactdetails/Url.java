/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/beans/contactdetails/Url.java#7 $
 * $Revision: #7 $
 * $Author: barriere $
 */

package fr.afnic.commons.beans.contactdetails;

import fr.afnic.commons.checkers.IInternalChecker;
import fr.afnic.commons.checkers.TrueChecker;

/**
 * Représente l'adressse d'un site internet
 * 
 * @author ginguene
 * 
 */
public class Url extends ContactDetail {

    private static final long serialVersionUID = 1L;

    public Url() {
    }

    public Url(String value) {
        super(value);
    }

    @Override
    protected IInternalChecker createChecker() {
        return new TrueChecker();
    }

}
