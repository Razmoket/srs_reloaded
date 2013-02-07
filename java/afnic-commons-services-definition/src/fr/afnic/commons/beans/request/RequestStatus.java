/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/beans/request/RequestStatus.java#9 $
 * $Revision: #9 $
 * $Author: barriere $
 */

package fr.afnic.commons.beans.request;

import fr.afnic.commons.beans.description.IDescribedInternalObject;

/**
 * 
 * Représente le status d'une requete.
 * 
 * @author ginguene
 * 
 */
public interface RequestStatus extends IDescribedInternalObject {

    /**
     * Indique si un statut est un statut final ou nom
     * 
     * @return True si le statut est final
     */
    public boolean isFinalStatus();

}
