package fr.afnic.commons.services.sql.converter.sql.tocommons;

public enum SqlColumnProfileMapping {

    idProfile("ID_PROFILE"),
    name("NAME"),
    descFr("DESC_FR");

    private final String sqlColumnName;

    private SqlColumnProfileMapping(String desc) {
        this.sqlColumnName = desc;
    }

    @Override
    public String toString() {
        return this.sqlColumnName;
    }

}
