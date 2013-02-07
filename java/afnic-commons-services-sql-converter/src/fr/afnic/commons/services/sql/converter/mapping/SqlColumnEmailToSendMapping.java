package fr.afnic.commons.services.sql.converter.mapping;

public enum SqlColumnEmailToSendMapping {

    from("FROM_FIELD"),
    to("TO_FIELD"),
    cc("CC_FIELD"),
    cci("CCI_FIELD"),
    subject("SUBJECT"),
    body("BODY"),
    objectVersion("OBJECT_VERSION"),
    emailFormat("EMAIL_FORMAT"), ;

    private final String sqlColumnName;

    private SqlColumnEmailToSendMapping(String desc) {
        this.sqlColumnName = desc;
    }

    @Override
    public String toString() {
        return this.sqlColumnName;
    }

}
