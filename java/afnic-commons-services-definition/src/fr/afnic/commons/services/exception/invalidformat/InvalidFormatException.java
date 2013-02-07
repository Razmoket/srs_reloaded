/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/services/exception/invalidformat/InvalidFormatException.java#6 $
 * $Revision: #6 $
 * $Author: ginguene $
 */

package fr.afnic.commons.services.exception.invalidformat;

/**
 * Exception levé en cas de données ne possédant pas un format correcte.
 * 
 * TODO revoir la classe car elle devient vraiment n'importequoi. Elle devrait etre abstaite et on ne devrait utiliser que ses classes filles.
 * 
 * 
 * @author ginguene
 * 
 */
public class InvalidFormatException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private Class<?> dataClass;
    private String name;
    private String expectedFormat;

    public InvalidFormatException(String message) {
        super(message);
    }

    public InvalidFormatException(Exception exception) {
        super(exception.getMessage(), exception);
    }

    public InvalidFormatException(String name, Class<?> dataClass, Exception exception) {
        super(getMessage(name, dataClass, null), exception);
        this.name = name;
        this.dataClass = dataClass;

    }

    public InvalidFormatException(String name, Class<?> dataClass, String expectedFormat, Exception exception) {
        super(getMessage(name, dataClass, null), exception);
        this.name = name;
        this.dataClass = dataClass;
        this.expectedFormat = expectedFormat;

    }

    public InvalidFormatException(String name, Class<?> dataClass) {
        super(getMessage(name, dataClass, null));
        this.name = name;
        this.dataClass = dataClass;
    }

    public InvalidFormatException(String name, Class<?> dataClass, String expectedFormat) {
        super(getMessage(name, dataClass, expectedFormat));
        this.name = name;
        this.dataClass = dataClass;
        this.expectedFormat = expectedFormat;
    }

    private static String getMessage(String name, Class<?> dataClass, String expectedFormat) {
        String message = "Invalid " + dataClass.getSimpleName() + " format for '" + name + "'";
        if (expectedFormat != null) {
            message += " [Expected format: " + expectedFormat + "]";
        }
        return message;

    }

    public Class<?> getDataClass() {
        return this.dataClass;
    }

    public String getName() {
        return this.name;
    }

    public String getExpectedFormat() {
        return this.expectedFormat;
    }
}
