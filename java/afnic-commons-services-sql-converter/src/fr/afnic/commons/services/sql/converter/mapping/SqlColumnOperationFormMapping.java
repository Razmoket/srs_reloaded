package fr.afnic.commons.services.sql.converter.mapping;

public enum SqlColumnOperationFormMapping {

    formId("formId"),
    domainName("domainName"),
    ticketType("ticketType"), ;
    private final String sqlColumnName;

    private SqlColumnOperationFormMapping(String desc) {
        this.sqlColumnName = desc;
    }

    @Override
    public String toString() {
        return this.sqlColumnName;
    }
}
