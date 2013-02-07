/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/beans/corporateentity/IntracommunityVat.java#1 $
 * $Revision: #1 $
 * $Author: ginguene $
 */

package fr.afnic.commons.beans.corporateentity;

import fr.afnic.commons.services.exception.invalidformat.InvalidFormatException;

/**
 * Représente la TVA intracommunaitaire qui est un identifiant des entreprises au niveau européen.
 * 
 * @author ginguene
 * 
 */
public class IntracommunityVat extends CorporateEntityIdentifier {

    private static final long serialVersionUID = 1L;

    public IntracommunityVat(String value) throws InvalidFormatException {
        super(value);
    }

}
