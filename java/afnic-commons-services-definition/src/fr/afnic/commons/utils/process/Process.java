/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/utils/process/Process.java#2 $
 * $Revision: #2 $
 * $Author: ginguene $
 */

package fr.afnic.commons.utils.process;

/**
 * Représente un process
 * 
 * @author ginguene
 * 
 */
public abstract class Process {

    /**
     * Demarre le process
     * 
     */
    public abstract void start() throws ProcessException;

    /**
     * Demande au process de s'arreter.
     * 
     */
    public abstract void stop() throws ProcessException;

    /**
     * Demande le reveille du process si celui-ci est en sommeil
     * 
     */
    public abstract void wakeUp() throws ProcessException;

    /**
     * Demande le redémarrage de l'application
     * 
     * @throws ProcessException
     */
    public void restart() throws ProcessException {
        this.stop();
        this.start();
    }

    /**
     * Retourne le nom de l'application, par défaut, il s'agit du nom de la classe de Process.
     * 
     * @return
     */
    public String getName() {
        return this.getClass().getSimpleName();
    }

}
