package fr.afnic.commons.services.sql.query.boa;

public class UserSqlQueries {

    public final static String GET_USER_CONTENT = "select boa_user.*, nicpers.login NICPERS_LOGIN from boa_user, nicope.nicpers where boa_user.id_nicpers=nicpers.id";
    public final static String GET_USER_CONTENT_WITH_ID = GET_USER_CONTENT + " and id_user=?";
    public final static String GET_USER_CONTENT_WITH_LOGIN = GET_USER_CONTENT + " and email=?";
    public final static String GET_USER_CONTENT_WITH_LOGIN_PWD = GET_USER_CONTENT + " and email=? and password=?";
    public final static String GET_USER_CONTENT_WITH_NICPERS = GET_USER_CONTENT + " and id_nicpers=?";

    public final static String GET_USERS = GET_USER_CONTENT + " order by id_user";

    public final static String UPDATE_PASSWORD = "update boa_user set password=? where id_user=?";

}
