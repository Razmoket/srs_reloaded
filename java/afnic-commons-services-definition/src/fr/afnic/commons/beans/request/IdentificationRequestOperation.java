/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/beans/request/IdentificationRequestOperation.java#2 $
 * $Revision: #2 $
 * $Author: ginguene $
 */

package fr.afnic.commons.beans.request;

/**
 * Classe utilisée dans shiva lors du rechargement d'une fiche d'identification. Cette classe doit disparaitre dès que l'action de validation ou de
 * problem sera effectuée proprement depuis une servlet ou via Wicket
 * 
 * @author ginguene
 * 
 */
public enum IdentificationRequestOperation {
    Validate,
    Problem

}
