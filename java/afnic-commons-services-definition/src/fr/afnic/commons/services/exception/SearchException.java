/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.services.exception;

import fr.afnic.commons.beans.search.ResultError;

public class SearchException extends ServiceException {

    private static final long serialVersionUID = 1L;

    private ResultError error;

    public SearchException(ResultError error) {
        super(error.getDescription());
        this.error = error;
    }

    public SearchException(ResultError error, Exception exception) {
        super(error.getDescription(), exception);
        this.error = error;
    }

    public ResultError getError() {
        return this.error;
    }

    public void setError(ResultError error) {
        this.error = error;
    }

}
