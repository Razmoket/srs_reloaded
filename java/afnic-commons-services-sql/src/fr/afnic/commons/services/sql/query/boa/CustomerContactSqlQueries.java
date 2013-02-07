package fr.afnic.commons.services.sql.query.boa;

import fr.afnic.commons.services.sql.converter.mapping.QueryUtils;
import fr.afnic.commons.services.sql.converter.mapping.SqlColumnCustomerContactMapping;

public class CustomerContactSqlQueries {

    public final static String CREATE_CONTACT = "select pkg_customer.create_customer_contact(?, ?, ?, ?, ?, ?, ?, ?, ?, ?) from dual";

    public final static String GET_CONTACT = QueryUtils.buildQuery("contact", SqlColumnCustomerContactMapping.class, SqlColumnCustomerContactMapping.idContact);

    public final static String UPDATE_CONTACT = "begin pkg_postal_address.update_postal_address(?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ; end;";

}
