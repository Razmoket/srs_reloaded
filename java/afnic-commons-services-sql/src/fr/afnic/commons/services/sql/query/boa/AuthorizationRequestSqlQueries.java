package fr.afnic.commons.services.sql.query.boa;

public class AuthorizationRequestSqlQueries {

    public final static String GET_SUNRISE_PERIOD = "select START_DATE, STOP_DATE from BOA.PERIOD where PERIOD_NAME = 'SUNRISE'";

}
