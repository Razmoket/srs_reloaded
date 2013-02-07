/*
 * $Id: //depot/main/java/afnic-commons-utils/src/fr/afnic/AfnicException.java#7 $ $Revision: #7 $ $Author: ginguene $
 */

package fr.afnic;


/**
 * Exception mère de toutes les exceptions relevée par une classe appartenant aux afnic-commons.
 * 
 * @author ginguene
 * 
 */
public class AfnicException extends Exception {

    private static final long serialVersionUID = 1L;

    /** 
     * Constructeur par défaut.
     * 
     */
    public AfnicException() {
        super();
    }

    /**
     * 
     * Constructeur avec l'exception mère et un message.
     * 
     * @param message
     *            Description de l'exception.
     * 
     * @param throwable
     *            Exception mère
     * 
     * 
     * 
     */
    public AfnicException(final String message, final Throwable throwable) {
        super(message, throwable);
    }

    /**
     * 
     * Constructeur avec message.
     * 
     * @param message
     *            Description de l'exception.
     * 
     */
    public AfnicException(final String message) {
        super(message);
    }

    /**
     * Retourne le message de la cause mère de toutes la chaine d'exception.
     * 
     * @return Le message de la première exception rencontréé
     */
    public String getFirstCauseMessage() {
        return this.getFirstCause().getMessage();
    }

    /**
     * Retourne la cause mère de toutes la chaine d'exception.
     */
    public Throwable getFirstCause() {
        if (this.getCause() != null) {
            return this.getFirstCause(this.getCause());
        } else {
            return this;
        }
    }

    /**
     * Retourne la cause mère de toutes la chaine d'exception <br/>
     * liée à l'exception passée en parametre.
     * 
     */
    private Throwable getFirstCause(final Throwable throwable) {
        if (throwable.getCause() != null) {
            return this.getFirstCause(throwable.getCause());
        } else {
            return throwable;
        }
    }
}
