/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/beans/domain/DomainStatus.java#5 $
 * $Revision: #5 $
 * $Author: ginguene $
 */

package fr.afnic.commons.beans.domain;

/**
 * Statut dans lequel peut etre un nom de domaine
 * 
 * 
 * @author ginguene
 * 
 */
public enum DomainStatus {

    Active,
    PARL,
    Blocked,
    Deleted,
    Frozen,
    PendingLegal,
    Registred,
    Unregistrable,
    Free; // Domaine qui n'est plus attribué mais qui l'a été par le passé ou qui a eu une requete Boa le concernant.

    public static boolean isDomainStatusOkForW4(DomainStatus status) {
        if (status == Active)
            return true;
        if (status == PARL)
            return true;
        if (status == Registred)
            return true;
        return false;
    }
}
