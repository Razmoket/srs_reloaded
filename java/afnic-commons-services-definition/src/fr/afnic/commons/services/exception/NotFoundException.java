/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/services/exception/NotFoundException.java#6 $
 * $Revision: #6 $
 * $Author: ginguene $
 */

package fr.afnic.commons.services.exception;

/**
 * Exception retournée lorsqu'une recherche n'aboutit pas
 * 
 * 
 * TODO remplacer par des implémentations plus précise de type DomainNotFoundException puis passer en abstract
 * 
 * @author ginguene
 * 
 */
public class NotFoundException extends ServiceException {

    private static final long serialVersionUID = 1L;

    private Class<?> notFoundObjectClass;

    /**
     * Constructeur
     * 
     * 
     * @param message
     *            Message de exception
     * @param exception
     *            Exception à l'origine de l'exception
     */
    public NotFoundException(String message, Exception exception) {
        super(FaultCode.NotFound, message, exception);
    }

    /**
     * Constructeur
     * 
     * 
     * @param message
     *            Message de exception
     */
    public NotFoundException(String message) {
        super(FaultCode.NotFound, message);
    }

    /**
     * Constructeur
     * 
     * 
     * @param message
     *            Message de exception
     * @param notFoundObjectClass
     *            Classe de l'objert non trouvé
     * 
     * @param exception
     *            Exception à l'origine de l'exception
     */
    public NotFoundException(String message, Class<?> notFoundObjectClass, Exception exception) {
        super(FaultCode.NotFound, message, exception);
        this.notFoundObjectClass = notFoundObjectClass;
    }

    /**
     * Constructeur
     * 
     * @param message
     *            Message de exception
     * @param notFoundObjectClass
     *            Classe de l'objert non trouvé
     */
    public NotFoundException(String message, Class<?> notFoundObjectClass) {
        super(FaultCode.NotFound, message);
        this.notFoundObjectClass = notFoundObjectClass;
    }

    public Class<?> getNotFoundObjectClass() {
        return this.notFoundObjectClass;
    }

}
