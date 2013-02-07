/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/beans/OperationFormVersion.java#2 $
 * $Revision: #2 $
 * $Author: ginguene $
 */

package fr.afnic.commons.beans;

import fr.afnic.commons.beans.application.Version;

/**
 * Décrit les versions majeurs du formulaire
 * 
 * @author ginguene
 */
public final class OperationFormVersion {

    public static final Version V1_7_4 = new Version(1, 7, 4);
    public static final Version V2_0_0 = new Version(2, 0, 0);
    public static final Version V2_5_0 = new Version(2, 5, 0);
    public static final Version LAST = V2_5_0;

    /**
     * Constucteur privé pour empecher l'instanciation de la classe utilitaire.
     */
    private OperationFormVersion() {
    }

}
