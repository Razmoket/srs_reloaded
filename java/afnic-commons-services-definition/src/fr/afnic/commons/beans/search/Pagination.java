/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.beans.search;

import java.io.Serializable;

/**
 * 
 * Décrit une page de résultat de recherche.
 * 
 * @author ginguene
 * 
 */
public class Pagination implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final Pagination ONE_ELEMENT_PAGINATION = new Pagination(1, 1);

    private int maxResultCount;
    private int pageNumber;

    public static Pagination createOnePagePagination() {
        Pagination pagination = new Pagination();
        pagination.setMaxResultCount(1000000);
        pagination.setPageNumber(1);
        return pagination;

    }

    public Pagination() {

    }

    public Pagination(int pageNumber, int maxResultCount) {
        this.maxResultCount = maxResultCount;
        this.pageNumber = pageNumber;
    }

    public int getMaxResultCount() {
        return this.maxResultCount;
    }

    public void setMaxResultCount(int maxResultCount) {
        this.maxResultCount = maxResultCount;
    }

    public int getPageNumber() {
        return this.pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

}
