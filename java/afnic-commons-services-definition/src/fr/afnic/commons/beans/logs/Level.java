/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/beans/logs/Level.java#2 $
 * $Revision: #2 $
 * $Author: ginguene $
 */

package fr.afnic.commons.beans.logs;

/**
 * Niveau de trace d'un log
 * 
 * @author ginguene
 * 
 */
public enum Level {

    TRACE(0),
    DEBUG(1),
    INFO(2),
    WARN(3),
    ERROR(4);

    private int intValue;

    private Level(int intValue) {
        this.intValue = intValue;
    }

    public int intValue() {
        return this.intValue;
    }

}
