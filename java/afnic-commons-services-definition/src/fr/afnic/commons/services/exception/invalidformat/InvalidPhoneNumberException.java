/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.services.exception.invalidformat;

public class InvalidPhoneNumberException extends InvalidFormatException {

    private static final long serialVersionUID = 1L;

    private String phoneNumber;

    public InvalidPhoneNumberException(String phoneNumber) {
        super("'" + phoneNumber + "' is not a valid phoneNumber");
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

}
