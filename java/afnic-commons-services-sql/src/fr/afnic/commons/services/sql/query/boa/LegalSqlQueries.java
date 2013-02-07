package fr.afnic.commons.services.sql.query.boa;

public class LegalSqlQueries {

    /* recherche si un code état existe dans la base juridique*/
    public final static String EXIST_CODE = "SELECT label FROM juridique.records_state WHERE id = ?";

    /* recherche si un numéro de dossier existe dans la base juridique*/
    public final static String EXIST_DOSSIER = "SELECT ref_ajpr FROM juridique.syreli_records WHERE ref_ajpr = ?";

    /* recherche si un code état existe dans la base juridique*/
    public final static String UPDATE_LEGAL_STATE = "UPDATE juridique.syreli_records SET records_state_id = ? WHERE ref_ajpr = ?";

    public final static String GET_LEGAL_DATA_LIST = "SELECT CI_REQ.civility as req_civility, CI_REQ.name as req_surname, CI_REQ.surname as req_firstname, CI_REQ.organization as req_organization, CI_REQ.address1 as req_address1, "
                                                     + "CI_REQ.address2 as req_address2, CI_REQ.address3 as req_address3, CI_REQ.zipcode as req_zipcode, CI_REQ.town as req_town, CI_REQ.country as req_country, "
                                                     + "CI_REQ.phone as req_phone, CI_REQ.fax as req_fax, CI_REQ.mail as req_mail, CI_REQ.vatnumber as req_vatnumber, "
                                                     + "CI_REP.civility as rep_civility, CI_REP.name as rep_surname, CI_REP.surname as rep_firstname, CI_REP.organization as rep_organization, CI_REP.address1 as rep_address1, "
                                                     + "CI_REP.address2 as rep_address2, CI_REP.address3 as rep_address3, CI_REP.zipcode as rep_zipcode, CI_REP.town as rep_town, CI_REP.country as rep_country, "
                                                     + "CI_REP.phone as rep_phone, CI_REP.fax as rep_fax, CI_REP.mail as rep_mail, CI_REP.vatnumber as rep_vatnumber, "
                                                     + "SRE.domain_name, SRE.asked_action, SRE.ref_ajpr, SRE.created_at, "
                                                     + "SAN.updated_at as answers_updated_at, SAN.titu_rights, SAN.titu_position, SAN.titu_legal_procedures, "
                                                     + "SRE.violation_reasons_1, SRE.violation_reasons_2, SRE.violation_reasons_3 "
                                                     + "FROM juridique.syreli_records SRE, juridique.contacts_info CI_REQ, juridique.contacts_info CI_REP, juridique.syreli_answers SAN "
                                                     + "WHERE SRE.requester_contact_info_id = CI_REQ.ID "
                                                     + "AND SRE.rep_contact_info_id = CI_REP.ID (+) " + " AND SRE.domain_name = ?"
                                                     + "AND SRE.answers_id = SAN.ID (+) ";

    /* si l'on veut la liste des codes dossiers, la jointure se fait sur agtf.dossier.id_dossier*/
    public final static String GET_STATUT_AGTF = "SELECT id_dossier FROM terme TER, r_statut RST WHERE graphie_terme = ? AND  extension_terme = ? and TER.id_terme = RST.id_terme and RST.id_statut = 'VALID'";

    /* recherche si un ndd existe dans la base agtf*/
    public final static String EXIST_NDD_AGTF = "SELECT graphie_terme FROM terme TER, r_statut RST WHERE graphie_terme = ? AND extension_terme = ? and TER.id_terme = RST.id_terme and RST.id_statut = 'VALID'";

    /* remonte un résultat sur un ndd est gelé dans syreli*/
    public final static String IS_AGTF_FROZEN = "SELECT \"GRAPHIE_TERME\"||NVL(\"EXTENSION_TERME\",'') "
                                                + "FROM terme TER, r_statut RST "
                                                + "WHERE \"GRAPHIE_TERME\"||NVL(\"EXTENSION_TERME\",'') = ? and TER.id_terme = RST.id_terme "
                                                + "and id_dossier in ('GEL, PARL_CMAP', 'PARL_OMPI', 'PARL_PREDEC', 'GEL_FOR_RECOVER', 'GEL_FOR_DELETE', 'PARL_SYRELI')";

    /** FUNCTION GEL_SYRELI (p_graphie_terme IN char, p_extension_terme IN char, p_origine IN number) RETURN number */
    public final static String CREATE_GEL_SYRELI = "select gel_syreli(?, ?, ?) from dual";

    /** PROCEDURE  SUPP_SYRELI ( p_graphie_terme IN char , p_extension_terme IN char) */
    public final static String REMOVE_GEL_SYRELI = "begin supp_syreli(?, ?); end;";

}
