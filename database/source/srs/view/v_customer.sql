create or replace view v_customer as 
select 
    ID_CUSTOMER ,
    (cast (
        case WHEN customer.ID_CUSTOMERSTATUS <= 2 
                THEN 'OPENED' 
            ELSE 'CLOSED'  
        end  as varchar2(8))) as status,
    CONTRACT_LEGAL_ID as ID_TIERS, 
    ID_POSTALADDRESS,
    (cast (
        case WHEN last_name is not null 
              THEN  individual_entity.first_name || ' ' || individual_entity.last_name 
        ELSE corporate_entity.ORGANIZATION_NAME
        end  as varchar2(256))) as label
from grc.customer, 
    grc.individual_entity, 
    grc.corporate_entity
where CONTRACT_LEGAL_ID is not null
    and customer.id_individualentity = individual_entity.id_individualentity (+)
    and customer.id_corporateentity = corporate_entity.id_corporateentity (+); 
