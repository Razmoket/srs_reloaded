package fr.afnic.commons.services.sql.converter.mapping;

public enum SqlColumnCustomerContactMapping implements IColumnMapping {

    idContact("ID_CONTACT"),
    idContactStatus("ID_CONTACTSTATUS"),
    phoneNumber("PHONE_NUMBER"),
    faxNumber("FAX_NUMBER"),
    emailAddress("EMAIL_ADDRESS"),
    comments("COMMENTS"),
    refNicId("REF_NIC_ID"),
    refNicAdh("REF_NIC_IDADH"),
    createDate("CREATE_DATE"),
    updateDate("LAST_UPDATE");

    private final String sqlColumnName;

    private SqlColumnCustomerContactMapping(String desc) {
        this.sqlColumnName = desc;
    }

    @Override
    public String toString() {
        return this.sqlColumnName;
    }

    @Override
    public String getColumnName() {
        return this.sqlColumnName;
    }

}
