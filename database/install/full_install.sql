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

col mdp_srs new_value mdp_rootsrs 
select '&mdp_rootsrs' as mdp_srs from dual;
def mdp_rootsrs

--ajout gestion de paramétrage du TLD
col new_tld new_value tld_name 
select '&tld_name' as new_tld from dual;
def tld_name

exec dbms_output.put_line('#######DEBUT FULL INSTALL#######');

@srs_reloaded/create_user.sql &mdp_sys &connect_identifier &path_table_space &mdp_rootsrs 
@srs_reloaded/srs_reloaded_global.ddl &mdp_rootsrs &connect_identifier 
@srs_reloaded/srs_reloaded_default_content.dml &mdp_rootsrs &connect_identifier 


set serveroutput on
exec dbms_output.put_line('#######FIN FULL INSTALL#######');

--prévoir la gestion de la configuration du nouveau TLD ici
exec dbms_output.put_line('#######DEMARRAGE DE LA CONFIGURATION DU REGISTRE#######');

spool off