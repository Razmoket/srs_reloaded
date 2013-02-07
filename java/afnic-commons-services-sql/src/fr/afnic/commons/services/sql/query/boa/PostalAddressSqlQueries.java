package fr.afnic.commons.services.sql.query.boa;

public class PostalAddressSqlQueries {

    public final static String CREATE_POSTAL_ADDRESS = "select pkg_postal_address.create_postal_address(?, ?, ?, ?, ?, ?, ?, ? ,?) from dual";

    public final static String GET_POSTAL_ADDRESS = "select ID_POSTALADDRESS,ORGANIZATION, STREET_LINE_1, STREET_LINE_2, STREET_LINE_3, POST_CODE, CITY, CITY_CEDEX,COUNTRY_CODE from postal_address where id_postaladdress = ?";

    public final static String UPDATE_POSTAL_ADDRESS = "begin pkg_postal_address.update_postal_address(?, ?, ?, ?, ?, ?, ?, ?, ? , ?) ; end;";

}
