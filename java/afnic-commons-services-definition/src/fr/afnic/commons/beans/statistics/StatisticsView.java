package fr.afnic.commons.beans.statistics;

import fr.afnic.commons.beans.list.IView;
import fr.afnic.commons.beans.list.QualificationStatResultList;
import fr.afnic.commons.beans.list.ResultList;

public enum StatisticsView implements IView {

    ValoTag("VALO_TAG"),
    JustifBlock("JUSTIF_BLOCK"),
    JustifDelete("JUSTIF_DELETE"),
    BeWhois("BE_WHOIS"),
    None("");

    private String identifier = null;

    private StatisticsView(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public String getIdentifier() {
        if (this.identifier != null) {
            return this.identifier;
        } else {
            throw new IllegalArgumentException("no identifierColumnName defined for view " + this);
        }
    }

    @Override
    public ResultList<?> createResultList() {
        return new QualificationStatResultList(this);
    }

}
