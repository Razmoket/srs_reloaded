package fr.afnic.commons.services.sql.query.boa;

public class ProfileSqlQueries {

    public final static String GET_PROFILE_CONTENT = "select id_profile, name, desc_fr from user_profile";

    public final static String GET_PROFILE_CONTENT_WITH_ID = GET_PROFILE_CONTENT + " where id_profile=?";

    public final static String GET_PROFILE_RIGHT_WITH_PROFILE_ID = "select id_userright NAME from PROFILE_RIGHT where id_profile=?";

    public final static String GET_PROFILES = GET_PROFILE_CONTENT + " order by id_profile";
}
