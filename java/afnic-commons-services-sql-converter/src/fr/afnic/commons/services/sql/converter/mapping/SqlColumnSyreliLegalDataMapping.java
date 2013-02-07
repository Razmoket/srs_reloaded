package fr.afnic.commons.services.sql.converter.mapping;

public enum SqlColumnSyreliLegalDataMapping {

    reqCivility("req_civility"),
    reqSurname("req_surname"),
    reqFirstName("req_firstname"),
    reqOrganization("req_organization"),
    reqAdress1("req_address1"),
    reqAdress2("req_address2"),
    reqAdress3("req_address3"),
    reqZipCode("req_zipcode"),
    reqTown("req_town"),
    reqCountry("req_country"),
    reqPhone("req_phone"),
    reqFax("req_fax"),
    reqMail("req_mail"),
    reqVatNumber("req_vatnumber"),
    repCivility("rep_civility"),
    repSurname("rep_surname"),
    repFirstName("rep_firstname"),
    repOrganization("rep_organization"),
    repAdress1("rep_address1"),
    repAdress2("rep_address2"),
    repAdress3("rep_address3"),
    repZipCode("rep_zipcode"),
    repTown("rep_town"),
    repCountry("rep_country"),
    repPhone("rep_phone"),
    repFax("rep_fax"),
    repMail("rep_mail"),
    repVatNumber("rep_vatnumber"),
    domainName("domain_name"),
    askedAction("asked_action"),
    refAjpr("ref_ajpr"),
    createdAt("created_at"),
    answeredAt("answers_updated_at"),
    tituRights("titu_rights"),
    tituPosition("titu_position"),
    tituLegalProcedures("titu_legal_procedures"),
    violationReasonsPart1("violation_reasons_1"),
    violationReasonsPart2("violation_reasons_2"),
    violationReasonsPart3("violation_reasons_3"), ;

    private final String sqlColumnName;

    private SqlColumnSyreliLegalDataMapping(String desc) {
        this.sqlColumnName = desc;
    }

    @Override
    public String toString() {
        return this.sqlColumnName;
    }
}
