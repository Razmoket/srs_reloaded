package fr.afnic.commons.beans.export;

import fr.afnic.commons.beans.list.IView;
import fr.afnic.commons.beans.list.ResultList;

public class ExportView implements IView {
    private final String name;
    private final String comments;

    public ExportView(String name, String comments) {
        this.name = name;
        this.comments = comments;
    }

    @Override
    public String getIdentifier() {
        return "view_name";
    }

    @Override
    public ResultList<?> createResultList() {
        return new ExportResultList(this);
    }

    public String getName() {
        return this.name;
    }

    public String getComments() {
        return this.comments;
    }

}
