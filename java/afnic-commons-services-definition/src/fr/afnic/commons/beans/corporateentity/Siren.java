/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/beans/corporateentity/Siren.java#1 $
 * $Revision: #1 $
 * $Author: ginguene $
 */

package fr.afnic.commons.beans.corporateentity;

import fr.afnic.commons.checkers.IInternalChecker;
import fr.afnic.commons.checkers.SirenChecker;

/**
 * Siren (Système d'Identification du Répertoire des ENtreprises) d'une entreprise ou d'une association.
 * 
 * 
 * @author ginguene
 * 
 */
public class Siren extends CorporateEntityIdentifier {

    private static final long serialVersionUID = 1L;

    public Siren(String value) {
        super(value);
    }

    @Override
    protected IInternalChecker createChecker() {
        return new SirenChecker();
    }

}
