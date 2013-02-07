/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/beans/request/RequestHistoryEventField.java#2 $
 * $Revision: #2 $
 * $Author: ginguene $
 */

package fr.afnic.commons.beans.request;

/**
 * Champs historisé pour une requete.
 * 
 * @author ginguene
 * 
 */
public enum RequestHistoryEventField implements IRequestHistoryEventField {

    /** Status de la requete */
    Status,
    /** Commentaire de la requete */
    Comment,
    /** Login de l 'opérateur ayant posé un verrou */
    LockedBy;

    @Override
    public String getName() {
        return this.toString();
    }

}
