/*
 * $Id: GeneratorException.java,v 1.1 2010/07/08 09:39:28 alaphil Exp $
 * $Revision: 1.1 $
 * $Author: alaphil $
 */

package fr.afnic.commons.test.generator.exception;

import fr.afnic.commons.services.exception.ServiceException;

/**
 * Exception retournée lors de la generations de jeux de tests<br/>
 * 
 * @author ginguene
 * 
 */
public class GeneratorException extends ServiceException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructeur avec l'exception mère.
     * 
     * @param throwable
     *            Exception mère
     * 
     * 
     */
    public GeneratorException(Throwable throwable) {
        super("Generation failed", throwable);
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
    public GeneratorException(String message, Throwable throwable) {
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
    public GeneratorException(String message) {
        super(message);
    }

}
