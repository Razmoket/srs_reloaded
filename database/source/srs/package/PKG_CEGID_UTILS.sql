
CREATE OR REPLACE PACKAGE PKG_CEGID_UTILS AS

	procedure wait_mvt_processed(id_tiers in integer);
	
	function check_statut_mvt(statutmvt in varchar2) return boolean;

	procedure delete_cegid_mvt(ZMI_ID_to_delete in integer);
	
	FUNCTION create_tiers_mvt(id_tiers in integer, user_login in varchar2) RETURN INTEGER;
	FUNCTION create_operation_mvt(id_ticket in varchar2, domaine_name in varchar2, ref_commande in varchar2, article in varchar2,
                                    tld in varchar2, billed_customer in varchar2, payers_customer in varchar2, command_date in date, user_login in varchar2) RETURN INTEGER;
	FUNCTION create_article_mvt(id_article in integer, user_login in varchar2) RETURN INTEGER;
	FUNCTION create_tarif_mvt(id_tarif in integer, user_login in varchar2) RETURN INTEGER;

END PKG_CEGID_UTILS;
/

CREATE OR REPLACE PACKAGE BODY PKG_CEGID_UTILS AS

    --========================================
    -- Declaration des attributs et methodes prives 
    --========================================    
    c_typemvt_tiers CONSTANT varchar2(50) :=  'TIERS'; -- grc.customer
    c_typemvt_operation CONSTANT varchar2(50) :=  'OPERATION'; -- nicope.ticket ?
    c_typemvt_article CONSTANT varchar2(50) :=  'ARTICLE'; -- on y met quoi ?
    c_typemvt_tarif CONSTANT varchar2(50) :=  'TARIF'; -- gestion des tarifs.. ?

    c_statutmvt_default constant varchar2(100) := '0';
    
    c_wait_value constant number := 1;
    
    FUNCTION priv_create_cegid_mvt_intkey(type_mvt in varchar2, id_object in integer, user_login in varchar2, statut_mvt in varchar2) RETURN INTEGER;
    
    FUNCTION priv_create_cegid_mvt_charkey(type_mvt in varchar2, id_object in varchar2, user_login in varchar2, statut_mvt in varchar2) RETURN INTEGER;
    
    procedure priv_create_operation_item (id_ticket in varchar2, domaine_name in varchar2, ref_commande in varchar2, article in varchar2,
                                    tld in varchar2, billed_customer in varchar2, payers_customer in varchar2, command_date in date);


    --========================================
	procedure wait_mvt_processed(id_tiers in integer) is
		id_statut varchar2(100);
	begin
		select zmi_statut into id_statut from zmvtinterface where zmi_keynumber1 = id_tiers;
        while id_statut = '0'
		loop
            dbms_lock.sleep(c_wait_value);
            select zmi_statut into id_statut from zmvtinterface where zmi_keynumber1 = id_tiers;
		end loop;
	end wait_mvt_processed;
	--========================================
	function  check_statut_mvt(statutmvt in varchar2) return boolean is
	begin
		return case statutmvt --0 - insérer / 1 - traité avec succès / -1 traité avec warning / -2 traité avec erreur';
			when '0' then true
			when '1' then true
			when '-1' then true
			when '-2' then true
			else false
		end;
	end check_statut_mvt;
	--========================================
	procedure delete_cegid_mvt(ZMI_ID_to_delete in integer) is
	begin
		delete from ZMVTINTERFACE where ZMI_ID = ZMI_ID_to_delete;
		commit;
	end delete_cegid_mvt;
	--========================================
	FUNCTION priv_create_cegid_mvt_intkey(type_mvt in varchar2, id_object in integer, user_login in varchar2, statut_mvt in varchar2) RETURN INTEGER IS
        PRAGMA AUTONOMOUS_TRANSACTION;
        id_mvt integer;
    BEGIN
        insert into ZMVTINTERFACE
            (ZMI_TYPEMVT,
            ZMI_KEYNUMBER1,
            ZMI_USERCR,
            ZMI_STATUT)
        values
            (type_mvt,
            id_object,
            user_login,
            statut_mvt);
        select ZMVTINTERFACE_ZMI_ID_SEQ.currval into id_mvt from dual;
        commit;
        return id_mvt;
    END priv_create_cegid_mvt_intkey;
    --========================================
    FUNCTION priv_create_cegid_mvt_charkey(type_mvt in varchar2, id_object in varchar2, user_login in varchar2, statut_mvt in varchar2) RETURN INTEGER IS
        PRAGMA AUTONOMOUS_TRANSACTION;
        id_mvt integer;
    BEGIN
        insert into ZMVTINTERFACE
            (ZMI_TYPEMVT,
            ZMI_KEYVARCHAR1,
            ZMI_USERCR,
            ZMI_STATUT)
        values
            (type_mvt,
            id_object,
            user_login,
            statut_mvt);
        select ZMVTINTERFACE_ZMI_ID_SEQ.currval into id_mvt from dual;
        commit;
        return id_mvt;
    END priv_create_cegid_mvt_charkey;
    --========================================
    FUNCTION create_tiers_mvt(id_tiers in integer, user_login in varchar2) RETURN INTEGER IS
		PRAGMA AUTONOMOUS_TRANSACTION;
	BEGIN
		return priv_create_cegid_mvt_intkey(c_typemvt_tiers, id_tiers, user_login, c_statutmvt_default);
	END create_tiers_mvt;
	--========================================
	FUNCTION create_operation_mvt(id_ticket in varchar2, domaine_name in varchar2, ref_commande in varchar2, article in varchar2,
                                    tld in varchar2, billed_customer in varchar2, payers_customer in varchar2, command_date in date, user_login in varchar2) RETURN INTEGER IS
		PRAGMA AUTONOMOUS_TRANSACTION;
	BEGIN
		priv_create_operation_item(id_ticket, domaine_name, ref_commande, article, tld, billed_customer, payers_customer, command_date);
        return priv_create_cegid_mvt_charkey(c_typemvt_operation, id_ticket, user_login, c_statutmvt_default);
	END create_operation_mvt;
	--========================================
	FUNCTION create_article_mvt(id_article in integer, user_login in varchar2) RETURN INTEGER IS
		PRAGMA AUTONOMOUS_TRANSACTION;
	BEGIN
		return priv_create_cegid_mvt_intkey(c_typemvt_article, id_article, user_login, c_statutmvt_default);
	END create_article_mvt;
	--========================================
	FUNCTION create_tarif_mvt(id_tarif in integer, user_login in varchar2) RETURN INTEGER IS
		PRAGMA AUTONOMOUS_TRANSACTION;
	BEGIN
		return priv_create_cegid_mvt_intkey(c_typemvt_tarif, id_tarif, user_login, c_statutmvt_default);
	END create_tarif_mvt;
	--========================================
	procedure priv_create_operation_item (id_ticket in varchar2, domaine_name in varchar2, ref_commande in varchar2, article in varchar2,
                                    tld in varchar2, billed_customer in varchar2, payers_customer in varchar2, command_date in date) is
	begin
        insert into BILLABLE_TICKETS
            (ID_TICKET,
            DOMAINE_NAME,
            REF_COMMANDE,
            ARTICLE,
            TLD,
            BILLED_CUSTOMER,
            PAYERS_CUSTOMER,
            COMMAND_DATE)
        values
            (id_ticket, 
            domaine_name, 
            ref_commande, 
            article,
            tld, 
            billed_customer, 
            payers_customer, 
            command_date);
		commit;
	end priv_create_operation_item;
	

END PKG_CEGID_UTILS;
/
