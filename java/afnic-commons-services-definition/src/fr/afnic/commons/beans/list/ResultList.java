package fr.afnic.commons.beans.list;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import fr.afnic.commons.beans.export.IExportable;
import fr.afnic.commons.utils.Preconditions;
import fr.afnic.utils.ToStringHelper;

public abstract class ResultList<VIEW extends IView> implements IExportable {

    protected List<Column> columns = new ArrayList<Column>();
    protected List<Line> lines = new ArrayList<Line>();
    protected VIEW view;

    protected Column identifierColumn = null;

    public void setView(VIEW view) {
        this.view = view;
    }

    public VIEW getView() {
        return this.view;
    }

    public String getViewAsString() {
        if (this.view != null) {
            return this.view.toString();
        } else {
            return "";
        }
    }

    public List<Column> getColumns() {
        return this.columns;
    }

    public boolean hasColumns() {
        return this.columns != null && !this.columns.isEmpty();
    }

    public List<Line> getLines() {
        return this.lines;
    }

    public int getLineCount() {
        return this.lines.size();
    }

    public void addLine(Line line) {
        Preconditions.checkNotNull(line, "line");

        boolean foundIdentifierColumn = false;

        for (Column column : line.getColumns()) {
            if (!this.columns.contains(column)) {
                throw new IllegalArgumentException("Cannot add line with value for unknown column: " + column.getReference() + ". Call addColumn() before addLine()");
            }

            if (column.isIdentifier()) {
                String value = line.getValue(column);
                if (value == null) {
                    throw new IllegalArgumentException("Cannot not add line with null value for identifier column " + column.getReference() + ".");
                } else if (StringUtils.isEmpty(line.getValue(column))) {
                    throw new IllegalArgumentException("Cannot not add line with empty value for identifier column " + column.getReference() + ".");
                }
                foundIdentifierColumn = true;
            }
        }

        if (!foundIdentifierColumn) {
            throw new IllegalArgumentException("Cannot add line without adding column set as identifier.");
        }

        this.lines.add(line);
    }

    public void addColumn(Column column) {
        Preconditions.checkNotNull(column, "column");

        if (this.columns.contains(column)) {
            throw new IllegalArgumentException("Column " + column.getReference() + " is already added.");
        }

        if (column.isIdentifier()) {
            if (this.identifierColumn == null) {
                this.identifierColumn = column;
            } else {
                throw new IllegalArgumentException("Cannot add more than one identifier column. Already added " + this.identifierColumn.getReference());
            }
        }

        this.columns.add(column);
    }

    public String getIdentifierColumnName() {
        return this.view.getIdentifier();
    }

    @Override
    public String toString() {
        return new ToStringHelper().addAllObjectAttributes(this).toString();
    }

    public Column getIdentifierColumn() {
        return this.identifierColumn;
    }

    public int getMaxLenghtContent(Column column) {
        int max = 0;
        for (Line line : this.getLines()) {
            max = Math.max(max, line.getValue(column).length());
        }
        return max;

    }

    public boolean isNotEmpty() {
        return this.getLineCount() > 0;
    }

}
