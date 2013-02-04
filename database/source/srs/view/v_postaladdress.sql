
create or replace view v_postaladdress as
select 
    ID_POSTALADDRESS,
    ORGANIZATION,
    STREET_LINE_1,
    STREET_LINE_2,
    STREET_LINE_3,
    POST_CODE ,
    CITY,
    CITY_CEDEX,
    COUNTRY_CODE 
from grc.postal_address;
