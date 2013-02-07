/*
 * $Id: //depot/main/java/afnic-commons-utils/src/fr/afnic/utils/ClassCastException.java#4 $ $Revision: #4 $ $Author: alaphilippe $
 */

package fr.afnic.utils;

/**
 * Exception de type classCast mais permettant d'avoir une throwable, contrairement à l'exception ClassCastException classique.
 * 
 * @author ginguene
 * 
 */
public class ClassCastException extends Exception {

    private static final long serialVersionUID = 1L;

    public ClassCastException(final String message, final Throwable throwable) {
        super(message, throwable);
    }

    public ClassCastException(final String message) {
        super(message);
    }
}
