/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/beans/request/TradeRequestOperation.java#2 $
 * $Revision: #2 $
 * $Author: ginguene $
 */

package fr.afnic.commons.beans.request;

/**
 * Operation possible sur une requete de Trade. Utilisé dans Shiva pour effectuee l'operation sur le reload de la page après avoir cliqué sur le
 * bouton correspondant
 * 
 * Cela doit etre nettoyer en utilisant une Servlet ou Wicket, ensuite cette classe sera obsolete.
 * 
 * 
 * @author ginguene
 * 
 */
public enum TradeRequestOperation {
    Approve,
    Cancel,
    SendMail

}
