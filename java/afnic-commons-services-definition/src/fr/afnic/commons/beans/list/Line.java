package fr.afnic.commons.beans.list;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Set;

import fr.afnic.utils.ToStringHelper;

public class Line implements Serializable {

    private final HashMap<Column, String> map = new HashMap<Column, String>();

    public void addValue(Column column, String value) {
        this.map.put(column, value);
    }

    public String getValue(Column column) {
        if (this.map.containsKey(column)) {
            return this.map.get(column);
        } else {
            return "";
        }
    }

    public Set<Column> getColumns() {
        return this.map.keySet();
    }

    public Column getIdentifier() {
        for (Column column : this.getColumns()) {
            if (column.isIdentifier()) {
                return column;
            }
        }
        throw new IllegalArgumentException("No identifier");
    }

    public String getIdentifierValue() {
        return this.getValue(this.getIdentifier());
    }

    @Override
    public String toString() {
        return new ToStringHelper().addAllObjectAttributes(this).toString();
    }
}
