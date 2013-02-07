package fr.afnic.commons.services.sql.converter.mapping;

public enum SqlColumnAutoMailReachabilityMapping {

    mailAddress("MAIL_ADDRESS"),
    idQualification("ID_QUALIFICATION"),
    isRelance("IS_RELANCE"),
    isValid("IS_VALID"), ;

    private final String sqlColumnName;

    private SqlColumnAutoMailReachabilityMapping(String desc) {
        this.sqlColumnName = desc;
    }

    @Override
    public String toString() {
        return this.sqlColumnName;
    }

}
