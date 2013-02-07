/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.beans.domain;

import java.io.Serializable;
import java.util.List;

public class DomainPortfolio implements Serializable {

    private static final long serialVersionUID = 1L;

    private int size = 0;

    private List<Domain> domains;

    public int getSize() {
        if (domains != null) {
            return domains.size();
        } else {
            return this.size;
        }

    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<Domain> getDomains() {
        return this.domains;
    }

    public void setDomains(List<Domain> domains) {
        this.domains = domains;
    }

    public boolean isEmpty() {
        return this.getSize() == 0;
    }
}
