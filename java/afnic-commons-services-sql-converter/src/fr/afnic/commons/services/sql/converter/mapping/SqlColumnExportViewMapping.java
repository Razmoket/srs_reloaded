package fr.afnic.commons.services.sql.converter.mapping;

public enum SqlColumnExportViewMapping {

    name("table_name"),
    comments("comments");

    private final String sqlColumnName;

    private SqlColumnExportViewMapping(String desc) {
        this.sqlColumnName = desc;
    }

    @Override
    public String toString() {
        return this.sqlColumnName;
    }

}
