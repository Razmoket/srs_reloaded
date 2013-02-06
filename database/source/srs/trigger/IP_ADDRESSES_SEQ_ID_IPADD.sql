-- TRIGGER FOR SEQUENCE SEQ_ID_IPADDRESS FOR COLUMN ID_IPADDRESS IN TABLE IP_ADDRESSES ---------

CREATE OR REPLACE TRIGGER "TS_IP_ADDRESSES_SEQ_ID_IPADD_0" BEFORE INSERT
ON "IP_ADDRESSES" FOR EACH ROW
BEGIN
	:NEW."ID_IPADDRESS" := "SEQ_ID_IPADDRESS".NEXTVAL;
END;
/
CREATE OR REPLACE TRIGGER "TSU_IP_ADDRESSES_SEQ_ID_IPAD_0" AFTER UPDATE OF "ID_IPADDRESS"
ON "IP_ADDRESSES" FOR EACH ROW
BEGIN
	RAISE_APPLICATION_ERROR(-20010,'CANNOT UPDATE COLUMN "ID_IPADDRESS" IN TABLE "IP_ADDRESSES" AS IT USES SEQUENCE.');
END;
/
