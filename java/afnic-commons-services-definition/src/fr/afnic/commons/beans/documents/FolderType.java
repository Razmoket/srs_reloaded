/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/beans/documents/FolderType.java#6 $
 * $Revision: #6 $
 * $Author: ginguene $
 */

package fr.afnic.commons.beans.documents;

/**
 * 
 * Sous répertoire du répertoire GDD dans la Ged
 * 
 * 
 * 
 * @author ginguene
 * 
 * Remplacée par la notion de Tree et de Path
 * 
 */
@Deprecated
public enum FolderType {

    Request,
    Running,
    Operation,
    Root,
    Archive,
    Inbox,
    Delayed,
    Inclassable,
    Unknown,
    TradeWithoutTicket;

}
