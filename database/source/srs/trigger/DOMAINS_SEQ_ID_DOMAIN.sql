
CREATE OR REPLACE TRIGGER "TS_DOMAINS_SEQ_ID_DOMAIN" BEFORE INSERT
ON "DOMAINS" FOR EACH ROW
BEGIN
	:NEW."ID_DOMAIN" := "SEQ_ID_DOMAIN".NEXTVAL;
END;
/
CREATE OR REPLACE TRIGGER "TSU_DOMAINS_SEQ_ID_DOMAIN" AFTER UPDATE OF "ID_DOMAIN"
ON "DOMAINS" FOR EACH ROW
BEGIN
	RAISE_APPLICATION_ERROR(-20010,'CANNOT UPDATE COLUMN "ID_DOMAIN" IN TABLE "DOMAINS" AS IT USES SEQUENCE.');
END;
/
