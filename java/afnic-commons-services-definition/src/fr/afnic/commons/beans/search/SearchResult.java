/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.beans.search;

import java.util.ArrayList;
import java.util.List;

public class SearchResult<SEARCHED_OBJECT, SEARCH_CRITERIA extends SearchCriteria<?>> {

    private List<SEARCHED_OBJECT> pageResults = new ArrayList<SEARCHED_OBJECT>();

    private SEARCH_CRITERIA criteria;

    private Pagination pagination;

    private int totalResultCount;

    private int pageCount;

    public List<SEARCHED_OBJECT> getPageResults() {
        return this.pageResults;
    }

    public void setPageResults(List<SEARCHED_OBJECT> pageResults) {
        this.pageResults = pageResults;
    }

    public SEARCH_CRITERIA getCriteria() {
        return this.criteria;
    }

    public void setCriteria(SEARCH_CRITERIA criteria) {
        this.criteria = criteria;
    }

    public Pagination getPagination() {
        return this.pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    public int getTotalResultCount() {
        return this.totalResultCount;
    }

    public void setTotalResultCount(int totalResultCount) {
        this.totalResultCount = totalResultCount;
    }

    public int getPageCount() {
        return this.pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

}
