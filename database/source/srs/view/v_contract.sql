
create or replace view  v_contract as
with billing_contact as (
    select 
        id_contract, 
        id_postaladdress 
    from grc.contract_contact_role_r, 
        GRC.customer_contact
    where contract_contact_role_r.id_contact = customer_contact.id_contact 
        and contract_contact_role_r.ID_ROLE = 5 )
select 
    CONTRACT_LEGAL_ID as ID_TIERS,
    Profil as CONTRACT_CATEGORY, 
    value as TLD, 
    contract_type.id_dictionary  as CONTRACT_TYPE,
    (cast (case WHEN contract.ID_contractSTATUS != 2 
          THEN 'OPENED' 
          ELSE 'CLOSED'  
      end  as varchar2(8))) as status,  
     (case when  billing_contact.id_postaladdress is not null
     then billing_contact.id_postaladdress
     else customer.id_postaladdress end)  as id_postaladdress 
from grc.contract,
    grc.contracttype_tld_r, 
    grc.tld, 
    grc.contract_type, 
    grc.customer, 
    grc.billing_contact
where contract.id_contracttypetld = contracttype_tld_r.id_contracttypetld
    and contracttype_tld_r.id_tld = tld.id_tld 
    and contracttype_tld_r.id_contracttype = contract_type.id_contracttype
    and contract.id_customer = customer.id_customer
    and  contract.id_contract = billing_contact.id_contract (+);    




