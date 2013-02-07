package fr.afnic.commons.services.sql.query.boa;

public class ResultListSqlQueries {

    public final static String GET_ALL_EXPORT_VIEW_NAME = "select table_name, comments from USER_TAB_COMMENTS where table_type ='VIEW' and table_name like 'EXP_%'";

    public final static String BEGIN_RESULT_QUERY = "select * from ";
    public final static String BEGIN_GET_COUNT_RESULT_QUERY = "select count(*) from ";

    public final static String RESULT_QUERY_STATS = "select calculation_limit, valeur, total, decode(valeur,total,100,to_char(valeur *100 / total,'FM90D09')) as taux from QUALIFICATION_STATS "
                                                    + " where id_type_stat = ? and begin_date = ? order by calculation_limit";
    public final static String RESULT_QUERY_STATS2 = "select calculation_limit as calculation_limit_be, valeur, total, decode(valeur,total,100,to_char(valeur *100 / total,'FM90D09')) as taux from QUALIFICATION_STATS "
                                                     + " where id_type_stat = ? and begin_date = ? order by calculation_limit";
    public final static String RESULT_QUERY_DATE_STATS = "select distinct begin_date from QUALIFICATION_STATS where id_type_stat = ?";
    public final static String RESULT_QUERY_LAST_DATE_STATS = "select max(begin_date) from QUALIFICATION_STATS where id_type_stat = ?";
    public final static String RESULT_QUERY_PERIODICITY_STATS = "select periodicity from QUALIFICATION_STATS_TYPE where id_type_stat = ?";

    public final static String COMPUTE_VALO_TAG_TOTAL = "select count(id_qualification), SUBSTR(whois.domain.name, INSTR(whois.domain.name, '.',-1))"
                                                        + " from qualification, whois.nh, whois.object_contact_r, whois.domain"
                                                        + " where qualification.id_top_level_operation_status = 6"
                                                        + " and portfolio_status_update >= ?"
                                                        + " and portfolio_status_update <= ?"
                                                        + " and qualification.contact_holder_handle = whois.nh.prefix || TO_CHAR(whois.nh.num) || '-' || whois.nh.suffix"
                                                        + " and whois.nh.object_id = whois.object_contact_r.contact_id"
                                                        + " and whois.object_contact_r.CONTACT_TYPE='HOLDER'"
                                                        + " and whois.object_contact_r.object_id =  whois.domain.id"
                                                        + " group by SUBSTR(whois.domain.name, INSTR(whois.domain.name, '.',-1))";

    public final static String COMPUTE_VALO_TAG_NB = "select count(id_qualification), SUBSTR(whois.domain.name, INSTR(whois.domain.name, '.',-1))"
                                                     + " from qualification, whois.nh, whois.object_contact_r, whois.domain"
                                                     + " where qualification.id_top_level_operation_status = 6"
                                                     + " and (id_reach_status = 5 or id_reach_status = 6)"
                                                     + " and id_eligibility_status = 2"
                                                     + " and portfolio_status_update >= ?"
                                                     + " and portfolio_status_update <= ?"
                                                     + " and qualification.contact_holder_handle = whois.nh.prefix || TO_CHAR(whois.nh.num) || '-' || whois.nh.suffix"
                                                     + " and whois.nh.object_id = whois.object_contact_r.contact_id"
                                                     + " and whois.object_contact_r.CONTACT_TYPE='HOLDER'"
                                                     + " and whois.object_contact_r.object_id =  whois.domain.id"
                                                     + " group by SUBSTR(whois.domain.name, INSTR(whois.domain.name, '.',-1))";

    public final static String COMPUTE_JUSTIF_BLOCK_TOTAL = "select count(id_qualification), SUBSTR(whois.domain.name, INSTR(whois.domain.name, '.',-1))"
                                                            + " from qualification q , whois.nh, whois.object_contact_r, whois.domain"
                                                            + " where q.id_operation in (select q.id_operation from operation o "
                                                            + "     where o.id_parent = q.id_operation and o.id_operation_type = 4 and o.id_operation_status = 5 and o.update_date > ? -30 and o.update_date < ? - 30)"
                                                            + " and q.contact_holder_handle = whois.nh.prefix || TO_CHAR(whois.nh.num) || '-' || whois.nh.suffix"
                                                            + " and whois.nh.object_id = whois.object_contact_r.contact_id"
                                                            + " and whois.object_contact_r.CONTACT_TYPE='HOLDER'"
                                                            + " and whois.object_contact_r.object_id =  whois.domain.id"
                                                            + " group by SUBSTR(whois.domain.name, INSTR(whois.domain.name, '.',-1))";

    public final static String COMPUTE_JUSTIF_BLOCK_NB = "select count(id_qualification), SUBSTR(whois.domain.name, INSTR(whois.domain.name, '.',-1))"
                                                         + " from qualification q, whois.nh, whois.object_contact_r, whois.domain"
                                                         + " where q.id_operation in (select q.id_operation from operation o "
                                                         + "     where o.id_parent = q.id_operation and o.id_operation_type = 4 and o.id_operation_status = 5 and o.update_date > ? - 30 and o.update_date < ? - 30)"
                                                         + " and q.id_operation in (select q.id_operation from operation o2 "
                                                         + "     where o2.id_parent = q.id_operation and o2.id_operation_type = 24 and o2.id_operation_status = 5 and o2.update_date > ? - 30 and o2.update_date < ?)"
                                                         + " and q.contact_holder_handle = whois.nh.prefix || TO_CHAR(whois.nh.num) || '-' || whois.nh.suffix"
                                                         + " and whois.nh.object_id = whois.object_contact_r.contact_id"
                                                         + " and whois.object_contact_r.CONTACT_TYPE='HOLDER'"
                                                         + " and whois.object_contact_r.object_id =  whois.domain.id"
                                                         + " group by SUBSTR(whois.domain.name, INSTR(whois.domain.name, '.',-1))";

    public final static String COMPUTE_JUSTIF_DELETE_TOTAL = "select count(id_qualification), SUBSTR(whois.domain.name, INSTR(whois.domain.name, '.',-1))"
                                                             + " from qualification q, whois.nh, whois.object_contact_r, whois.domain"
                                                             + " where q.id_top_level_operation_status = 6"
                                                             + " and portfolio_status_update >= ?"
                                                             + " and portfolio_status_update <= ?"
                                                             + " and q.id_operation in (select q.id_operation  from operation o "
                                                             + "     where o.id_parent = q.id_operation and o.id_operation_type = 4 and o.id_operation_status = 5)"
                                                             + " and q.contact_holder_handle = whois.nh.prefix || TO_CHAR(whois.nh.num) || '-' || whois.nh.suffix"
                                                             + " and whois.nh.object_id = whois.object_contact_r.contact_id"
                                                             + " and whois.object_contact_r.CONTACT_TYPE='HOLDER'"
                                                             + " and whois.object_contact_r.object_id =  whois.domain.id"
                                                             + " group by SUBSTR(whois.domain.name, INSTR(whois.domain.name, '.',-1))";

    public final static String COMPUTE_JUSTIF_DELETE_NB = "select count(id_qualification), SUBSTR(whois.domain.name, INSTR(whois.domain.name, '.',-1))"
                                                          + " from qualification q, whois.nh, whois.object_contact_r, whois.domain"
                                                          + " where q.id_top_level_operation_status = 6"
                                                          + " and portfolio_status_update >= ?"
                                                          + " and portfolio_status_update <= ?"
                                                          + " and q.id_operation in (select q.id_operation from operation o "
                                                          + "     where o.id_parent = q.id_operation and o.id_operation_type = 28 and o.id_operation_status = 5)"
                                                          + " and q.contact_holder_handle = whois.nh.prefix || TO_CHAR(whois.nh.num) || '-' || whois.nh.suffix"
                                                          + " and whois.nh.object_id = whois.object_contact_r.contact_id"
                                                          + " and whois.object_contact_r.CONTACT_TYPE='HOLDER'"
                                                          + " and whois.object_contact_r.object_id =  whois.domain.id"
                                                          + " group by SUBSTR(whois.domain.name, INSTR(whois.domain.name, '.',-1))";

    public final static String COMPUTE_BE_WHOIS_TOTAL = "SELECT /*+ parallel(domain, 5) */ count( distinct whois.contact.id), nicope.adherent.nom"
                                                        + " FROM whois.domain, nicope.adherent, whois.object_contact_r, whois.contact"
                                                        + " WHERE  whois.domain.REF_REGISTRAR = nicope.adherent.ID"
                                                        + " AND whois.object_contact_r.CONTACT_ID = whois.contact.ID"
                                                        + " AND whois.object_contact_r.CONTACT_TYPE='HOLDER'"
                                                        + " AND whois.object_contact_r.object_id =  whois.domain.ID"
                                                        + " group by nicope.adherent.nom";

    public final static String COMPUTE_BE_WHOIS_NB = "select count(id_qualification), trademark_id"
                                                     + " from qualification q, gerico.client c"
                                                     + " where q.id_operation in (SELECT q.id_operation"
                                                     + "  FROM operation o"
                                                     + "  WHERE o.id_operation_type = 16 and o.id_operation_status = 5"
                                                     + " and update_date >= ?"
                                                     + " and update_date <= ?"
                                                     + "  START WITH o.Id_parent = q.id_operation"
                                                     + "  CONNECT BY PRIOR o.id_operation = o.Id_Parent)"
                                                     + "and q.id_client = c.id_client"
                                                     + " group by trademark_id";

    public final static String INSERT_QUALIFICATION_STAT = "insert into QUALIFICATION_STATS(ID_TYPE_STAT, BEGIN_DATE, CALCULATION_LIMIT, VALEUR, TOTAL) VALUES (?, ?, ?, ?, ?)";

}
