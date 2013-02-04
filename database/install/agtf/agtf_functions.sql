connect agtf/&1@&2

set serveroutput on
exec dbms_output.put_line('#######agtf_functions');


--
-- SUPP_SYRELI  (Procedure) 
--
/* Formatted on 30/01/2013 17:38:03 (QP5 v5.215.12089.38647) */
CREATE OR REPLACE PROCEDURE "SUPP_SYRELI" (p_graphie_terme     IN CHAR,
                                           p_extension_terme   IN CHAR)
IS
   PRAGMA AUTONOMOUS_TRANSACTION;
   v_terme             terme%ROWTYPE;
   v_id_histo          VARCHAR2 (15);
   v_extension_terme   VARCHAR2 (15);
BEGIN
   SELECT t.*
     INTO v_terme
     FROM terme t, r_statut rs
    WHERE     t.graphie_terme || NVL (t.extension_terme, '') =
                 p_graphie_terme || p_extension_terme
          AND t.id_terme = rs.id_terme
          AND rs.id_statut = 'VALID';

   UPDATE r_statut
      SET id_statut = 'SUPP', date_pose = SYSDATE
    WHERE id_terme = v_terme.id_terme;

   SELECT seq_agtf.NEXTVAL INTO v_id_histo FROM DUAL;

   INSERT INTO r_histo_terme (id_histo,
                              qui_histo,
                              quand_histo,
                              quoi_histo,
                              cmt_histo,
                              id_terme)
        VALUES (v_id_histo,
                'syreli',
                SYSDATE,
                'Levee pour Syreli ' || v_terme.id_terme,
                v_terme.id_dossier,
                v_terme.id_terme);

   COMMIT;
EXCEPTION
   WHEN OTHERS
   THEN
      ROLLBACK;
      DBMS_OUTPUT.put_line (SQLCODE || SQLERRM);
END SUPP_SYRELI;
/

--
-- CREATE_GEL_SYRELI  (Function) 
--
/* Formatted on 30/01/2013 17:36:59 (QP5 v5.215.12089.38647) */
CREATE OR REPLACE FUNCTION "CREATE_GEL_SYRELI" (
   p_graphie_terme     IN CHAR,
   p_extension_terme   IN CHAR,
   p_origine           IN NUMBER)
   RETURN NUMBER
IS
   PRAGMA AUTONOMOUS_TRANSACTION;
   v_id_terme     VARCHAR2 (15);
   v_id_dossier   VARCHAR2 (15);
   v_id_histo     VARCHAR2 (15);
   rc             NUMBER;
BEGIN
   IF (p_origine = 1)
   THEN
      v_id_dossier := 'PARL_SYRELI';
      rc := 1;
   ELSE
      IF (p_origine = 2)
      THEN
         v_id_dossier := 'GEL_FOR_RECOVER';
         rc := 1;
      ELSE
         IF (p_origine = 3)
         THEN
            v_id_dossier := 'GEL_FOR_DELETE';
            rc := 1;
         ELSE
            rc := -9;
         END IF;
      END IF;
   END IF;


   SELECT seq_agtf.NEXTVAL INTO v_id_terme FROM DUAL;

   INSERT INTO terme (id_terme,
                      graphie_terme,
                      extension_terme,
                      cmt_terme,
                      id_categ,
                      id_dossier,
                      id_publication)
        VALUES (v_id_terme,
                p_graphie_terme,
                p_extension_terme,
                'via procedure GEL_TOTAL',
                'UNDEF',
                v_id_dossier,
                'RESERVE');

   INSERT INTO r_statut (id_terme, id_statut, date_pose)
        VALUES (v_id_terme, 'VALID', SYSDATE);

   SELECT seq_agtf.NEXTVAL INTO v_id_histo FROM DUAL;

   INSERT INTO r_histo_terme (id_histo,
                              qui_histo,
                              quand_histo,
                              quoi_histo,
                              cmt_histo,
                              id_terme)
        VALUES (v_id_histo,
                'syreli',
                SYSDATE,
                'Gel pour Syreli ' || v_id_terme,
                v_id_dossier,
                v_id_terme);

   COMMIT;

   RETURN rc;
EXCEPTION
   WHEN OTHERS
   THEN
      ROLLBACK;
      DBMS_OUTPUT.put_line (SQLCODE || SQLERRM);
      RETURN -1;
END CREATE_GEL_SYRELI;
/

--
-- EXIST_PARL  (Function) 
--
/* Formatted on 30/01/2013 17:36:59 (QP5 v5.215.12089.38647) */
CREATE OR REPLACE FUNCTION "EXIST_PARL" (p_graphie_terme     IN CHAR,
                                         p_extension_terme   IN CHAR)
   RETURN NUMBER
IS
   v_id_dossier   VARCHAR2 (15);
   rc             NUMBER;
BEGIN
   IF (p_extension_terme IS NOT NULL)
   THEN
      SELECT t.id_dossier
        INTO v_id_dossier
        FROM terme t, r_statut rs
       WHERE     t.graphie_terme = p_graphie_terme
             AND t.extension_terme = p_extension_terme
             AND t.id_terme = rs.id_terme
             AND rs.id_statut = 'VALID';
   ELSE
      SELECT t.id_dossier
        INTO v_id_dossier
        FROM terme t, r_statut rs
       WHERE     t.graphie_terme = p_graphie_terme
             AND t.extension_terme IS NULL
             AND t.id_terme = rs.id_terme
             AND rs.id_statut = 'VALID';
   END IF;

   IF (v_id_dossier = 'PARL_CMAP')
   THEN
      rc := 1;
   ELSE
      IF (v_id_dossier = 'PARL_OMPI')
      THEN
         rc := 2;
      ELSE
         rc := 0;                            -- autre type de dossier non PARL
      END IF;
   END IF;

   RETURN rc;
EXCEPTION
   WHEN NO_DATA_FOUND
   THEN
      RETURN -9;
   WHEN OTHERS
   THEN
      RETURN -1;
END EXIST_PARL;
/

--
-- GEL_PARL  (Function) 
--
/* Formatted on 30/01/2013 17:36:59 (QP5 v5.215.12089.38647) */
CREATE OR REPLACE FUNCTION "GEL_PARL" (p_graphie_terme     IN CHAR,
                                       p_extension_terme   IN CHAR,
                                       p_origine           IN NUMBER)
   RETURN NUMBER
IS
   v_id_terme          VARCHAR2 (15);
   v_id_dossier        VARCHAR2 (15);
   v_id_histo          VARCHAR2 (15);
   v_extension_terme   VARCHAR2 (15);
   rc                  NUMBER;
BEGIN
   IF (p_origine = 1)
   THEN
      v_id_dossier := 'PARL_CMAP';
      rc := 1;
   ELSE
      IF (p_origine = 2)
      THEN
         v_id_dossier := 'PARL_OMPI';
         rc := 1;
      ELSE
         IF (p_origine = 99)
         THEN
            v_id_dossier := 'GEL_TRANSMISS';
            rc := 1;
         ELSE
            rc := -9;
         END IF;
      END IF;
   END IF;

   IF (rc > 0)
   THEN
      IF (p_extension_terme IS NOT NULL)
      THEN
         v_extension_terme := p_extension_terme;
      ELSE
         v_extension_terme := NULL;
      END IF;
   END IF;

   IF (rc > 0)
   THEN
      SAVEPOINT PS_GEL_PARL;

      SELECT seq_agtf.NEXTVAL INTO v_id_terme FROM DUAL;

      INSERT INTO terme (id_terme,
                         graphie_terme,
                         extension_terme,
                         cmt_terme,
                         id_categ,
                         id_dossier,
                         id_publication)
           VALUES (v_id_terme,
                   p_graphie_terme,
                   v_extension_terme,
                   'via procedure GEL_PARL',
                   'UNDEF',
                   v_id_dossier,
                   'RESERVE');

      INSERT INTO r_statut (id_terme, id_statut, date_pose)
           VALUES (v_id_terme, 'VALID', SYSDATE);

      SELECT seq_agtf.NEXTVAL INTO v_id_histo FROM DUAL;

      INSERT INTO r_histo_terme (id_histo,
                                 qui_histo,
                                 quand_histo,
                                 quoi_histo,
                                 cmt_histo,
                                 id_terme)
           VALUES (v_id_histo,
                   'parl',
                   SYSDATE,
                   'Gel pour PARL ' || v_id_terme,
                   v_id_dossier,
                   v_id_terme);
   END IF;

   RETURN rc;
EXCEPTION
   WHEN OTHERS
   THEN
      ROLLBACK TO SAVEPOINT PS_GEL_PARL;
      RETURN -1;
END GEL_PARL;
/

--
-- GEL_SYRELI  (Function) 
--
/* Formatted on 30/01/2013 17:36:59 (QP5 v5.215.12089.38647) */
CREATE OR REPLACE FUNCTION "GEL_SYRELI" (p_graphie_terme     IN CHAR,
                                         p_extension_terme   IN CHAR,
                                         p_origine           IN NUMBER)
   RETURN NUMBER
IS
   PRAGMA AUTONOMOUS_TRANSACTION;
   v_id_terme     VARCHAR2 (15);
   v_id_dossier   VARCHAR2 (15);
   v_id_histo     VARCHAR2 (15);
   rc             NUMBER;
BEGIN
   BEGIN
      SELECT t.id_terme
        INTO v_id_terme
        FROM terme t, r_statut rs
       WHERE     t.id_terme = rs.id_terme
             AND rs.id_statut = 'VALID'
             AND t.graphie_terme = p_graphie_terme
             AND t.extension_terme = p_extension_terme;
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         v_id_terme := -1;
   END;

   IF (v_id_terme > 0)
   THEN
      RETURN UPDATE_GEL_SYRELI (v_id_terme, p_origine);
   ELSE
      RETURN CREATE_GEL_SYRELI (p_graphie_terme,
                                p_extension_terme,
                                p_origine);
   END IF;
EXCEPTION
   WHEN OTHERS
   THEN
      DBMS_OUTPUT.put_line (SQLCODE || SQLERRM);
      RETURN -1;
END GEL_SYRELI;
/

--
-- LEVEE_PARL  (Function) 
--
/* Formatted on 30/01/2013 17:36:59 (QP5 v5.215.12089.38647) */
CREATE OR REPLACE FUNCTION "LEVEE_PARL" (p_graphie_terme     IN CHAR,
                                         p_extension_terme   IN CHAR,
                                         p_origine           IN NUMBER)
   RETURN NUMBER
IS
   v_id_terme          VARCHAR2 (15);
   v_id_dossier        VARCHAR2 (15);
   v_id_histo          VARCHAR2 (15);
   v_extension_terme   VARCHAR2 (15);
   rc                  NUMBER;
BEGIN
   IF (p_origine = 1)
   THEN
      v_id_dossier := 'PARL_CMAP';
      rc := 1;
   ELSE
      IF (p_origine = 2)
      THEN
         v_id_dossier := 'PARL_OMPI';
         rc := 1;
      ELSE
         rc := -9;
      END IF;
   END IF;

   IF (rc > 0)
   THEN
      IF (p_extension_terme IS NOT NULL)
      THEN
         v_extension_terme := p_extension_terme;
      ELSE
         v_extension_terme := NULL;
      END IF;
   END IF;

   IF (rc > 0)
   THEN
      SAVEPOINT PS_LEVEE_PARL;

      SELECT t.id_terme
        INTO v_id_terme
        FROM terme t, r_statut rs
       WHERE     t.graphie_terme || NVL (t.extension_terme, '') =
                    p_graphie_terme || v_extension_terme
             AND t.id_dossier = v_id_dossier
             AND t.id_terme = rs.id_terme
             AND rs.id_statut = 'VALID';

      UPDATE r_statut
         SET id_statut = 'SUPP', date_pose = SYSDATE
       WHERE id_terme = v_id_terme;

      SELECT seq_agtf.NEXTVAL INTO v_id_histo FROM DUAL;

      INSERT INTO r_histo_terme (id_histo,
                                 qui_histo,
                                 quand_histo,
                                 quoi_histo,
                                 cmt_histo,
                                 id_terme)
           VALUES (v_id_histo,
                   'parl',
                   SYSDATE,
                   'Levee pour PARL ' || v_id_terme,
                   v_id_dossier,
                   v_id_terme);
   END IF;

   RETURN rc;
EXCEPTION
   WHEN OTHERS
   THEN
      ROLLBACK TO SAVEPOINT PS_LEVEE_PARL;
      RETURN -1;
END LEVEE_PARL;
/

--
-- UPDATE_GEL_SYRELI  (Function) 
--
/* Formatted on 30/01/2013 17:36:59 (QP5 v5.215.12089.38647) */
CREATE OR REPLACE FUNCTION "UPDATE_GEL_SYRELI" (p_id_terme   IN NUMBER,
                                                p_origine    IN NUMBER)
   RETURN NUMBER
IS
   PRAGMA AUTONOMOUS_TRANSACTION;
   v_id_terme     VARCHAR2 (15);
   v_id_dossier   VARCHAR2 (15);
   v_id_histo     VARCHAR2 (15);
   rc             NUMBER;
BEGIN
   IF (p_origine = 1)
   THEN
      v_id_dossier := 'PARL_SYRELI';
      rc := 1;
   ELSE
      IF (p_origine = 2)
      THEN
         v_id_dossier := 'GEL_FOR_RECOVER';
         rc := 1;
      ELSE
         IF (p_origine = 3)
         THEN
            v_id_dossier := 'GEL_FOR_DELETE';
            rc := 1;
         ELSE
            rc := -9;
         END IF;
      END IF;
   END IF;

   UPDATE terme
      SET id_dossier = v_id_dossier
    WHERE id_terme = p_id_terme;


   SELECT seq_agtf.NEXTVAL INTO v_id_histo FROM DUAL;

   INSERT INTO r_histo_terme (id_histo,
                              qui_histo,
                              quand_histo,
                              quoi_histo,
                              cmt_histo,
                              id_terme)
        VALUES (v_id_histo,
                'syreli',
                SYSDATE,
                'Gel pour Syreli ' || p_id_terme,
                v_id_dossier,
                p_id_terme);

   COMMIT;

   RETURN rc;
EXCEPTION
   WHEN OTHERS
   THEN
      ROLLBACK;
      DBMS_OUTPUT.put_line (SQLCODE || SQLERRM);
      RETURN -1;
END UPDATE_GEL_SYRELI;
/

GRANT EXECUTE ON EXIST_PARL TO NICOPE;

GRANT EXECUTE ON GEL_PARL TO NICOPE;

GRANT EXECUTE ON LEVEE_PARL TO NICOPE;
