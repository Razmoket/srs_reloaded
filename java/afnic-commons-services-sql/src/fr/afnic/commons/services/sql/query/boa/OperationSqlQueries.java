package fr.afnic.commons.services.sql.query.boa;

public class OperationSqlQueries {

    public final static String GET_OPERATION_CONTENT = "select * from operation where id_operation=?";

    public final static String GET_STATUS_UPDATE_CONTENT = "select * from status_update where id_operation=?";

    public final static String GET_EMAIL_TO_SEND_CONTENT = "select * from email_to_send where id_operation=?";

    public final static String GET_SUB_OPERATION_CONTENT = "select * from operation where id_parent=? order by id_operation";

    /**
     * méthode PL/SQL <code>create_operation</code> </br>
     * param: <ul>
     * <li>id_createUser in INTEGER</li>
     * <li>id_nextOperation in integer</li>
     * <li>id_previousOperation in integer</li>
     * <li>id_parentField in integer</li>
     * <li>id_operationType in integer</li>
     * <li>commentsField in varchar2</li>
     * <li>operationDetails in varchar2</li>
     * <li>isBlocking in boolean</li>
     * </ul>
     * retourne: <code>l'identifiant de l'opération créée sous forme d'entier</code>
     */
    public final static String CREATE_OPERATION = "select pkg_operation.create_operation(?, ?, ?, ?, ?, ?, ?, ?) from dual";

    /*
       id_operation_type in integer,
       is_blocking in char,
       id_user in integer, 
       idPrevious in integer, 
       idParent in integer, 
       comments in varchar2, 
       oldValue in integer, 
       newValue in integer
      */
    public final static String CREATE_STATUS_UPDATE = "select pkg_commons_operation.create_status_update(?, ?, ?, ?, ?, ?, ?, ?) from dual";

    /** PROCEDURE update_status (id_ope in integer, id_update_user in integer, id_operationStatus in integer); */
    public final static String UPDATE_OPERATION_STATUS = "begin pkg_operation.update_status(?, ?, ?); end;";

    /** PROCEDURE update_comments (id_ope in integer, id_update_user in integer, commentsField in varchar2); */
    public final static String UPDATE_OPERATION_COMMENTS = "begin pkg_operation.update_comments(?, ?, ?); end;";

    /** PROCEDURE update_details (id_ope in integer, id_update_user in integer, operationDetails in varchar2); */
    public final static String UPDATE_OPERATION_DETAILS = "begin pkg_operation.update_details(?, ?, ?); end;";

    /** PROCEDURE update_user (id_ope in integer, id_updateUser in integer); */
    public final static String UPDATE_OPERATION_USER = "begin pkg_operation.update_user(?, ?); end;";

    /** PROCEDURE lock_operation (id_ope in integer, id_lockingUser in integer); */
    public final static String LOCK_OPERATION = "begin pkg_operation.lock_operation(?, ?); end;";

    /** PROCEDURE unlock_operation (id_ope in integer); */
    public final static String UNLOCK_OPERATION = "begin pkg_operation.unlock_operation(?); end;";

    public final static String IS_EXISTING_OPERATION = "select count(*) from operation where id_operation=?";

    /** PKG_COMMONS_OPERATION.create_email_to_send(idOperation in integer, fromField in varchar2, toField in varchar2, ccField in varchar2, 
     * cciField in varchar2, subjectField in varchar2, bodyField in clob) RETURN INTEGER;*/
    public final static String CREATE_EMAIL_TO_SEND = "select PKG_COMMONS_OPERATION.create_email_to_send(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) from dual";

    //attach_document(idOperation in integer, id_user in integer, docushare in varchar2)
    public final static String ATTACH_OPERATION_DOCUMENT = "select PKG_COMMONS_OPERATION.attach_document(?, ?, ?) from dual";

    public final static String GET_OPERATION_DOCUMENTS = "select * from document where id_operation=? order by id_document";

    public final static String GET_OPERATION_ATTACHED_TO_DOCUMENT = "select id_operation from document where docushare_handle=? ";
}
