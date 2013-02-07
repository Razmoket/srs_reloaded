/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.beans.search;

public class ResultError {

    private ResultErrorType type;
    private String description;

    public ResultErrorType getType() {
        return this.type;
    }

    public void setType(ResultErrorType type) {
        this.type = type;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
