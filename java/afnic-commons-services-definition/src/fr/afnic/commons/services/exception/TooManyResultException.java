/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/services/exception/TooManyResultException.java#3 $
 * $Revision: #3 $
 * $Author: ginguene $
 */

package fr.afnic.commons.services.exception;

public class TooManyResultException extends ServiceException {

    private static final long serialVersionUID = 1L;
    private int resultCount;

    public TooManyResultException(int resultCount, Exception exception) {
        super(resultCount + " results found", exception);
        this.resultCount = resultCount;
    }

    /**
     * @param nbResult
     * 
     */
    public TooManyResultException(int resultCount) {
        super("more than " + resultCount + " results found");
        this.resultCount = resultCount;
    }

    public int getResultCount() {
        return resultCount;
    }

}
