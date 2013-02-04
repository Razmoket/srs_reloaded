SPOOL log_full_install_with_param.log
set serveroutput on
exec dbms_output.put_line('#######DEMARRAGE DU SCRIPT D''INSTALL#######');

--ajout gestion de paramétrage des script
exec dbms_output.put_line('#######PARAMETRE DU SCRIPT');

-- path pour les creations des fichiers de tablespace
-- path par defaut : DATAFILE '+DATA/NEXT/TB_NICOPE01' SIZE 100M
-- path local j.alaphilippe : C:\app-oracle\oradata\LocalAFNIC\DATA\NEXT\
-- path from.dev.database.prive.sqy.nic.fr: /usr/local/oracle/oradata/ORANEXT/NEXT/
clear columns

col tbsp new_value path_table_space 
select '&path_table_space' as tbsp from dual;
def path_table_space

--alias du serveur pour faire le connect
col cnx_identifier new_value connect_identifier 
select '&connect_identifier' as cnx_identifier from dual;
def connect_identifier

col mdp_dba new_value mdp_sys 
select '&mdp_sys' as mdp_dba from dual;
def mdp_sys

col mdp_nic new_value mdp_nicope 
select '&mdp_nicope' as mdp_nic from dual;
def mdp_nicope

col mdp_ger new_value mdp_gerico 
select '&mdp_gerico' as mdp_ger from dual;
def mdp_gerico

col mdp_bo new_value mdp_boa 
select '&mdp_boa' as mdp_bo from dual;
def mdp_boa

col mdp_who new_value mdp_whois 
select '&mdp_whois' as mdp_who from dual;
def mdp_whois

col mdp_ral new_value mdp_rali 
select '&mdp_rali' as mdp_ral from dual;
def mdp_rali

col mdp_agt new_value mdp_agtf 
select '&mdp_agtf' as mdp_agt from dual;
def mdp_agtf

col mdp_ficp new_value mdp_ficpostaux 
select '&mdp_ficpostaux' as mdp_ficp from dual;
def mdp_ficpostaux

--ajout gestion de paramétrage du TLD
col new_tld new_value tld_name 
select '&tld_name' as new_tld from dual;
def tld_name

exec dbms_output.put_line('#######DEBUT FULL INSTALL#######');

@common/common_user_tbs.ddl &path_table_space &mdp_nicope &mdp_whois &mdp_gerico &mdp_boa &mdp_rali
@gerico/gerico_global.ddl &mdp_gerico &connect_identifier 
@whois/whois_global.ddl &mdp_whois &connect_identifier 
@whois/whois_seq.ddl &mdp_whois &connect_identifier 
@nicope/nicope_global_includes_vm.ddl &mdp_nicope &connect_identifier 
@nicope/nicope_seq.ddl &mdp_nicope &connect_identifier 
@boa/boa_global.ddl &mdp_boa &connect_identifier 
@boa/boa_packages.ddl &mdp_boa &connect_identifier 
@boa/boa_packages.ddl &mdp_boa &connect_identifier 
@common/after_full_database_install/nicope_functions.ddl &mdp_nicope &connect_identifier 
@common/after_full_database_install/nicope_pkg.ddl &mdp_nicope &connect_identifier 
-- 2 passes de compilation sur les fonctions et packages de nicope
@common/after_full_database_install/nicope_functions.ddl &mdp_nicope. &connect_identifier 
@common/after_full_database_install/nicope_pkg.ddl &mdp_nicope &connect_identifier 
@common/after_full_database_install/nicope_procedures.ddl &mdp_nicope &connect_identifier 
@common/after_full_database_install/whois_synonyms.ddl &mdp_whois &connect_identifier 
@common/after_full_database_install/grants.ddl &mdp_sys &connect_identifier 
@common/after_full_database_install/gerico_triggers_after_full_databases_install.ddl &mdp_gerico &connect_identifier 
@common/after_full_database_install/gerico_triggers_after_full_databases_install.ddl &mdp_gerico &connect_identifier 
@rali/rali_global.ddl &mdp_rali &connect_identifier 

-- ajout J.Alaphilippe
@gerico/gerico_views.ddl &mdp_gerico &connect_identifier 
@gerico/gerico_default_content.dml &mdp_gerico &connect_identifier 
@boa/boa_default_content.dml &mdp_boa &connect_identifier 
@nicope/nicope_default_content.dml &mdp_nicope &connect_identifier 
@whois/whois_default_content.dml &mdp_whois &connect_identifier 

-- ajout AGTF
@agtf/create_user_agtf.sql &mdp_sys &connect_identifier &path_table_space &mdp_agtf 
@agtf/agtf_global.ddl &mdp_agtf &connect_identifier 
@agtf/agtf_views.ddl &mdp_agtf &connect_identifier 
@agtf/agtf_functions.sql &mdp_agtf &connect_identifier 
@agtf/agtf_functions.sql &mdp_agtf &connect_identifier 
@agtf/agtf_default_content.dml &mdp_agtf &connect_identifier 

-- ajout ficpostaux
@ficpostaux/create_user_ficpostaux.sql &mdp_sys &connect_identifier &path_table_space &mdp_ficpostaux 
@ficpostaux/ficpostaux_global.ddl &mdp_ficpostaux &connect_identifier 
--@ficpostaux/ficpostaux_content.dml &mdp_ficpostaux &connect_identifier --trop couteux


set serveroutput on
exec dbms_output.put_line('#######FIN FULL INSTALL#######');

--prévoir la gestion de la configuration du nouveau TLD ici
exec dbms_output.put_line('#######DEMARRAGE DE LA CONFIGURATION DU REGISTRE#######');

spool off