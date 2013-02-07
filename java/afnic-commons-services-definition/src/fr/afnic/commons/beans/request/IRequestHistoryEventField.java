/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/beans/request/IRequestHistoryEventField.java#2 $
 * $Revision: #2 $
 * $Author: ginguene $
 */

package fr.afnic.commons.beans.request;

/**
 * Interface désignant le champs dont le changement de valeur doit être historisé.
 * 
 * @author ginguene
 * 
 */
public interface IRequestHistoryEventField {

    /**
     * Retourne le nom du champs dont le changement de valeur doit être historisé
     * 
     * @return
     */
    public String getName();

}
