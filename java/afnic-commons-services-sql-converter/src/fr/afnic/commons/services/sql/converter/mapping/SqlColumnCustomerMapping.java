package fr.afnic.commons.services.sql.converter.mapping;

public enum SqlColumnCustomerMapping {

    idClient("ID_CLIENT"),
    idOrganizationType("ID_ORGANIZATIONTYPE"),
    idMembreType("ID_MEMBRETYPE"),
    idClientStatus("ID_CLIENTSTATUS"),
    clientNumber("CLIENT_NUMBER"),
    phoneNumber("PHONE_NUMBER"),
    faxNumber("FAX_NUMBER"),
    emailAddress("EMAIL_ADDRESS"),
    url("URL"),
    redList("RED_LIST"),
    redListDate("RED_LIST_DATE"),
    tld("TLD"),
    sirenId("SIREN_ID"),
    siretId("SIRET_ID"),
    intraVatId("INTRACOMMUNITY_VAT_ID"),
    trademarkId("TRADEMARK_ID"),
    walderId("WALDEC_ID"),
    lastName("LAST_NAME"),
    firtName("FIRST_NAME"),
    birthDate("BIRTH_DATE"),
    birthPlace("BIRTH_PLACE"),
    isPerson("IS_PERSON"),
    refCode("REF_CODE"),
    refConvention("REF_CONVENTION"),
    magicCode("MAGIC_CODE"),
    refModPay("REF_MODPAY"),
    comments("COMMENTS"),
    refLogin("REF_LOGIN"),
    refPwd("REF_PWD"),
    legalStatus("LEGAL_STATUS"),
    createDate("CREATE_DATE"),
    updateDate("UPDATE_DATE"),
    updateUser("UPDATE_USER"),
    accountManager("ACCOUNT_MANAGER"), ;

    private final String sqlColumnName;

    private SqlColumnCustomerMapping(String desc) {
        this.sqlColumnName = desc;
    }

    @Override
    public String toString() {
        return this.sqlColumnName;
    }

}
