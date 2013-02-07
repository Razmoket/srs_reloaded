package fr.afnic.commons.services.sql.converter.mapping;

public enum SqlColumnStatusUpdateMapping {

    idStatusUpdate("ID_STATUS_UPDATE"),
    idOperation("ID_OPERATION"),
    oldValue("OLD_VALUE"),
    newValue("NEW_VALUE"),
    objectVersion("OBJECT_VERSION"), ;

    private final String sqlColumnName;

    private SqlColumnStatusUpdateMapping(String desc) {
        this.sqlColumnName = desc;
    }

    @Override
    public String toString() {
        return this.sqlColumnName;
    }

}
