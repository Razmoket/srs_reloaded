package fr.afnic.commons.beans.statistics;

import java.io.Serializable;

public class RawStat implements Serializable {

    private static final long serialVersionUID = 1L;

    private String limit;
    private int nb;

    public String getLimit() {
        return this.limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public int getNb() {
        return this.nb;
    }

    public void setNb(int nb) {
        this.nb = nb;
    }
}
