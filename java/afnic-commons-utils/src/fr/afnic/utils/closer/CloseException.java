/*
 * $Id: $ $Revision: $ $Author: $
 */

package fr.afnic.utils.closer;

/**
 * Handler contenant une autre exception qui s'est déroulée au moment de l'appel à une méthode close().
 * 
 * @author ginguene
 * 
 */
public class CloseException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public CloseException(final Exception exception) {
        super(exception);
    }

}
