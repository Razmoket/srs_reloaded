-- TRIGGER FOR SEQUENCE SEQ_ID_HOST FOR COLUMN ID_HOST IN TABLE HOSTS ---------

CREATE OR REPLACE TRIGGER "TS_HOSTS_SEQ_ID_HOST" BEFORE INSERT
ON "HOSTS" FOR EACH ROW
BEGIN
	:NEW."ID_HOST" := "SEQ_ID_HOST".NEXTVAL;
END;
/
CREATE OR REPLACE TRIGGER "TSU_HOSTS_SEQ_ID_HOST" AFTER UPDATE OF "ID_HOST"
ON "HOSTS" FOR EACH ROW
BEGIN
	RAISE_APPLICATION_ERROR(-20010,'CANNOT UPDATE COLUMN "ID_HOST" IN TABLE "HOSTS" AS IT USES SEQUENCE.');
END;
/