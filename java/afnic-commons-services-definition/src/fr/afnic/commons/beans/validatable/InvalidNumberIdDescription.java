/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.beans.validatable;

public class InvalidNumberIdDescription extends InvalidDataDescription {

    private static final long serialVersionUID = 1L;

    private String numberId;
    private Error error;

    public InvalidNumberIdDescription(String numberId, Error error) {
        super(getDefaultDescription(numberId, error));

        this.numberId = numberId;
        this.error = error;
    }

    private static String getDefaultDescription(String numberId, Error error) {
        switch (error) {

        case NOT_NUMBER:
            return "Id value [" + numberId + "] must be a number";

        case SMALLER_THAN_ZERO:
            return "Id value [" + numberId + "] must be greater than 0";

        default:
            return " Id value [" + numberId + "] is invalid";

        }
    }

    public String getNumberId() {
        return this.numberId;
    }

    public Error getError() {
        return this.error;
    }

    public enum Error {
        NOT_NUMBER,
        SMALLER_THAN_ZERO;
    }

}
