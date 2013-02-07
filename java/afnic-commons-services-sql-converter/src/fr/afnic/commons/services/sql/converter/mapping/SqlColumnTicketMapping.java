package fr.afnic.commons.services.sql.converter.mapping;

public enum SqlColumnTicketMapping {

    status("statusFromString"),
    requester("originalRequesterName"),
    createDate("createDate"),
    registrarCode("registrarCode"),
    formId("formId"),
    operation("operationFromString"),
    domainName("domainName"),
    ticketId("id"), ;
    private final String sqlColumnName;

    private SqlColumnTicketMapping(String desc) {
        this.sqlColumnName = desc;
    }

    @Override
    public String toString() {
        return this.sqlColumnName;
    }
}
