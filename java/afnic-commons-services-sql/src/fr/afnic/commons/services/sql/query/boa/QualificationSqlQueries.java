package fr.afnic.commons.services.sql.query.boa;

public class QualificationSqlQueries {

    public final static String GET_QUALIFICATION_CONTENT_WITH_OPERATION_ID = "select * from qualification where id_operation=?";
    public final static String GET_QUALIFICATION_CONTENT_WITH_QUALIFICATION_ID = "select * from qualification where id_qualification=?";
    public final static String GET_QUALIFICATION_CONTENT_WITH_NICHANDLE = "select * from qualification where contact_holder_handle=?";

    public final static String IS_EXISTING_QUALIFICATION = "select count(*) from qualification where id_qualification=?";

    /**
     * Fonction pkg_qualification.create_qualification</br>
     * Paramètres : <ul>
                <li>id_source_type in integer: source de la qualification</li>
                <li>id_create_user in integer: identifiant de l'utilisateur créant la qualification</li>
                <li>client in integer: identifiant du client lié </li>
                <li>nichandle in varchar2: nichandle du titulaire</li>
                <li>comments in varchar2: commentaire de création</li>
                <li>initiatorEmail in varchar2: email de l'emetteur du signalement ou de la plainte</li>
                </ul></br>
     * retourne l'entier correspondant à l'identifiant de la qualification créée
     */
    public final static String CREATE_QUALIFICATION_CONTENT = "select pkg_qualification.create_qualification(?, ?, ?, ?, ?, ?, ?) from dual";
    // public final static String UPDATE_QUALIFICATION_CONTENT = "";

    public final static String UPDATE_QUALIFICATION_TLO_STATUS = "begin pkg_qualification.update_tlo_status(?, ?, ?); end;";
    public final static String UPDATE_QUALIFICATION_REACH_STATUS = "begin pkg_qualification.update_reach_status(?, ?, ?); end;";
    public final static String UPDATE_QUALIFICATION_ELIGIBILITY_STATUS = "begin pkg_qualification.update_eligibility_status(?, ?, ?); end;";
    public final static String UPDATE_QUALIFICATION_PORTFOLIO_STATUS = "begin pkg_qualification.update_portfolio_status(?, ?, ?); end;";
    public final static String UPDATE_QUALIFICATION_CONTACT_SNAPSHOT = "begin pkg_qualification.update_contact_snapshot(?, ?, ?); end;";
    public final static String SET_QUALIFICATION_INCOHERENT = "begin pkg_qualification.set_incoherent(?, ?, ?); end;";

    public final static String GET_QUALIFICATION_IN_PROGRESS_COUNT = "select count(*) from qualification where id_top_level_operation_status != 6";
    public final static String GET_QUALIFICATION_IN_PROGRESS_WITH_NICHANDLE = "select id_operation from qualification where id_top_level_operation_status != 6 and CONTACT_HOLDER_HANDLE=?";
    /*
        PROCEDURE update_tlo_status (id_qualif integer, id_update_user integer, tlo_status integer);
        PROCEDURE update_reach_status (id_qualif in integer, id_update_user in integer, reach_status in integer);
        PROCEDURE update_eligibility_status (id_qualif in integer, id_update_user in integer, eligibility_status in integer);
        PROCEDURE update_portfolio_status (id_qualif in integer, id_update_user in integer, portfolio_status in integer);
        PROCEDURE set_incoherent (id_qualif in integer, id_update_user in integer, isIncoherent in boolean);
     */

    /* function create_qualification_snapshot(idQualification in integer, contactName in varchar2, organizationType in varchar2, sirenId in varchar2, siretId in varchar2, trademarkId in varchar2, waldecId in varchar2, dunsId in varchar2) return integer;*/
    public final static String CREATE_QUALIFICATION_SNAPSHOT = "select pkg_commons_operation.create_qualification_snapshot(?, ?, ?, ?, ?, ?, ?, ?) from dual";
    public final static String GET_QUALIFICATION_SNAPSHOT = "select * from qualification_snapshot where ID_QUALIFICATION_SNAPSHOT=?";

    public final static String GET_SELECT_VIEW = "select * from ";
    public final static String GET_SELECT_VIEW_COUNT = "select count(*) from ";
    public final static String GET_SELECT_VIEW_TIME_FILTER = " where PORTFOLIO_STATUS_UPDATE <= ?";

    public final static String GET_LIST_QUALIFICATION_WAITING_AUTO_REACHABILITY = "select id_qualification from qualification q, operation o where id_reach_status=1 and id_top_level_operation_status != 6 and id_portfolio_status = 1 and o.id_operation = q.id_operation and o.id_operation_status IN (4,5)";
    public final static String GET_LIST_QUALIFICATION_WAITING_AUTO_REMINDER_REACHABILITY = "select distinct id_qualification from qualification q, operation o, operation o2"
                                                                                           + " where q.id_reach_status = 2 and q.id_top_level_operation_status != 6"
                                                                                           + " and q.id_portfolio_status = 1 and o.id_parent = q.id_operation and o.id_operation_type = 40 and o.id_operation_status = 5 and o.update_date < sysdate - ?"
                                                                                           + " and q.id_operation = o2.id_operation and o2.id_operation_status NOT IN( 1,2,3)";
    public final static String GET_LIST_QUALIFICATION_AUTO_REMINDER_TIMEOUT_REACHABILITY = "select distinct id_qualification from qualification q, operation o, operation o2"
                                                                                           + " where q.id_reach_status = 3 and q.id_top_level_operation_status != 6"
                                                                                           + " and q.id_portfolio_status = 1 and o.id_parent = q.id_operation and o.id_operation_type = 41 and o.id_operation_status = 5 and o.update_date < sysdate - ?"
                                                                                           + " and q.id_operation = o2.id_operation and o2.id_operation_status NOT IN( 1,2,3)";

    public final static String GET_MAIL_REACHABILITY = "select mail_address, id_qualification, is_relance, is_valid from QUALIFICATION_AUTOMAIL where confirm_key = ?";
    public final static String SET_REACHABILITY = "update QUALIFICATION_AUTOMAIL set is_valid = 1, confirmed_date = SYSDATE where id_qualification = ?";

    public final static String CREATE_AUTOMAIL_ENTRY = "insert into QUALIFICATION_AUTOMAIL (ID_QUALIFICATION, IS_RELANCE, IS_VALID, MAIL_ADDRESS, SEND_DATE, CONFIRMED_DATE, CONFIRM_KEY) values (?, ?, 0, ?, SYSDATE, null, ?)";
    public final static String GET_AUTOMAIL_ENTRY = "select CONFIRM_KEY from QUALIFICATION_AUTOMAIL where ID_QUALIFICATION = ?";

    public final static String SET_COMMENT = "update QUALIFICATION set COMMENTS = ? where id_qualification = ?";

    public final static String UPDATE_EN_QUALIF = "select pkg_auto_qualif.add_qualif_to_day(?) from dual";
    public final static String GET_NB_QUALIF_LAUNCHED = "select pkg_auto_qualif.get_nb_qualif_for_day(?) from dual";

}
