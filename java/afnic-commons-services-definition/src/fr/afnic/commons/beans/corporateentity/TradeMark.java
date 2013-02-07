/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/beans/corporateentity/TradeMark.java#1 $
 * $Revision: #1 $
 * $Author: ginguene $
 */

package fr.afnic.commons.beans.corporateentity;

import fr.afnic.commons.services.exception.invalidformat.InvalidFormatException;

/**
 * 
 * Identifiant d'une structure légale
 * 
 * @author ginguene
 * 
 */
public class TradeMark extends CorporateEntityIdentifier {

    private static final long serialVersionUID = 1L;

    public TradeMark(String value) throws InvalidFormatException {
        super(value);
    }

}
