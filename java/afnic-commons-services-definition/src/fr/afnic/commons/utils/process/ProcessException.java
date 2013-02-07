/*
 * $Id: //depot/main/java/app-injector/src/fr/afnic/application/injector/InjectorException.java#2 $
 * $Revision: #2 $
 * $Author: ginguene $
 */

package fr.afnic.commons.utils.process;

import fr.afnic.AfnicException;

/**
 * Exception levee par l'injector.
 * 
 * @author ginguene
 * 
 */
public class ProcessException extends AfnicException {

    private static final long serialVersionUID = 1L;

    public ProcessException(String message) {
        super(message);
    }

    public ProcessException(String message, Exception e) {
        super(message, e);
    }

}
