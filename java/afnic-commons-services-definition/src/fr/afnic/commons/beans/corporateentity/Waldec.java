/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/beans/corporateentity/Waldec.java#1 $
 * $Revision: #1 $
 * $Author: ginguene $
 */

package fr.afnic.commons.beans.corporateentity;

import fr.afnic.commons.services.exception.invalidformat.InvalidFormatException;

/**
 * 
 * Numéro Waldec d'une association: Web Associations Librement DEClarées
 * 
 * @author ginguene
 * 
 */
public class Waldec extends CorporateEntityIdentifier {

    private static final long serialVersionUID = 1L;

    public Waldec(String value) throws InvalidFormatException {
        super(value);
    }

}
