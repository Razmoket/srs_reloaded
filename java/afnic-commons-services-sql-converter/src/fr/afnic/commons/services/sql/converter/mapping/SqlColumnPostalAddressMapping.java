package fr.afnic.commons.services.sql.converter.mapping;

public enum SqlColumnPostalAddressMapping {

    idPostalAddress("ID_POSTALADDRESS"),
    organization("ORGANIZATION"),
    streetLine1("STREET_LINE_1"),
    streetLine2("STREET_LINE_2"),
    streetLine3("STREET_LINE_3"),
    postCode("POST_CODE"),
    city("CITY"),
    cityCedex("CITY_CEDEX"),
    countryCode("COUNTRY_CODE");

    private final String sqlColumnName;

    private SqlColumnPostalAddressMapping(String desc) {
        this.sqlColumnName = desc;
    }

    @Override
    public String toString() {
        return this.sqlColumnName;
    }
}
