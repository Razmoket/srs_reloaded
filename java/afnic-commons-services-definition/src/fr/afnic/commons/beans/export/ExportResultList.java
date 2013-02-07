package fr.afnic.commons.beans.export;

import fr.afnic.commons.beans.list.Column;
import fr.afnic.commons.beans.list.ResultList;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.utils.Preconditions;

public class ExportResultList extends ResultList<ExportView> implements IExportable {

    public ExportResultList(ExportView view) {
        this.view = Preconditions.checkNotNull(view, "view");
    }

    @Override
    public void addColumn(Column column) {

        //Pour un export resultlist, la première colonne est l'identifiant
        if (this.identifierColumn == null) {
            super.addColumn(new Column(column.getDescription(), column.getReference(), true));
        } else {
            super.addColumn(column);
        }

    }

    public String toCsv() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < this.getColumns().size(); i++) {
            if (i != 0) {
                builder.append(";");
            }
            builder.append(this.getColumns().get(i).getDescription());
        }
        builder.append("\n");

        for (int j = 0; j < this.getLineCount(); j++) {
            for (int i = 0; i < this.getColumns().size(); i++) {
                if (i != 0) {
                    builder.append(";");
                }
                builder.append(this.getLines().get(j).getValue(this.getColumns().get(i)));
            }
            builder.append("\n");
        }

        return builder.toString();
    }

    public String getViewComments() {
        return this.view.getComments();
    }

    public String getViewName() {
        return this.view.getName();
    }

    @Override
    public CharSequence createStream() throws ServiceException {
        return this.toCsv();
    }
}
