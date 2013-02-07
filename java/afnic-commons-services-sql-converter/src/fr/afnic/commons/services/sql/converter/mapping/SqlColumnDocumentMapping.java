package fr.afnic.commons.services.sql.converter.mapping;

public enum SqlColumnDocumentMapping {

    idDocument("ID_DOCUMENT"),
    idOperation("ID_OPERATION"),
    docuShareHandle("DOCUSHARE_HANDLE"),
    objectVersion("OBJECT_VERSION"), ;

    private final String sqlColumnName;

    private SqlColumnDocumentMapping(String desc) {
        this.sqlColumnName = desc;
    }

    @Override
    public String toString() {
        return this.sqlColumnName;
    }
}
