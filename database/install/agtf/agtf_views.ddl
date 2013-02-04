connect agtf/&1@&2

set serveroutput on
exec dbms_output.put_line('#######agtf_views');


/* Formatted on 30/01/2013 17:34:29 (QP5 v5.215.12089.38647) */
--
-- DOMAINE_NON_ATTRIBUABLE  (View)
--

CREATE OR REPLACE FORCE VIEW DOMAINE_NON_ATTRIBUABLE
(
   NDD,
   NOM,
   EXTENSION,
   ID_CATEG,
   LIB_CATEG,
   ID_DOSSIER,
   LIB_DOSSIER,
   ID_PUBLICATION,
   LIB_PUBLICATION
)
AS
   SELECT t.graphie_terme || NVL (t.extension_terme, ''),
          t.graphie_terme,
          t.extension_terme,
          t.id_categ,
          c.lib_categ,
          t.id_dossier,
          d.lib_dossier,
          t.id_publication,
          p.lib_publication
     FROM terme t,
          categ c,
          dossier d,
          publication p,
          r_statut rs
    WHERE     rs.id_statut = 'VALID'
          AND rs.id_terme = t.id_terme
          AND t.id_categ = c.id_categ
          AND t.id_dossier = d.id_dossier
          AND t.id_publication = p.id_publication;


/* Formatted on 30/01/2013 17:34:30 (QP5 v5.215.12089.38647) */
--
-- FONDAMENTAUX_PUBLIC  (View)
--

CREATE OR REPLACE FORCE VIEW FONDAMENTAUX_PUBLIC
(
   LIBELLE,
   CATEGORIE,
   CATEG_EN
)
AS
   SELECT graphie_terme, lib_aff_categ, lib_aff_categ_en
     FROM terme t, categ c, r_statut rs
    WHERE     rs.id_statut = 'VALID'
          AND rs.id_terme = t.id_terme
          AND t.id_dossier = 'RESERVE'
          AND t.id_publication = 'PUBLIC'
          AND t.id_categ = c.id_categ;


/* Formatted on 30/01/2013 17:34:30 (QP5 v5.215.12089.38647) */
--
-- MALEK_DOMAINE_NON_ATTRIBUABLE  (View)
--

CREATE OR REPLACE FORCE VIEW MALEK_DOMAINE_NON_ATTRIBUABLE
(
   NDD,
   NOM,
   EXTENSION,
   ID_CATEG,
   LIB_CATEG,
   ID_DOSSIER,
   LIB_DOSSIER,
   ID_PUBLICATION,
   LIB_PUBLICATION
)
AS
   SELECT /*+ FIRST_ROWS */
         t.graphie_terme || NVL (t.extension_terme, ''),
          t.graphie_terme,
          t.extension_terme,
          t.id_categ,
          c.lib_categ,
          t.id_dossier,
          d.lib_dossier,
          t.id_publication,
          p.lib_publication
     FROM terme t,
          categ c,
          dossier d,
          publication p,
          r_statut rs
    WHERE     rs.id_statut = 'VALID'
          AND rs.id_terme = t.id_terme
          AND t.id_categ = c.id_categ
          AND t.id_dossier = d.id_dossier
          AND t.id_publication = p.id_publication;


/* Formatted on 30/01/2013 17:34:30 (QP5 v5.215.12089.38647) */
--
-- VIEWMALEK  (View)
--

CREATE OR REPLACE FORCE VIEW VIEWMALEK
(
   NDD,
   NOM,
   EXTENSION,
   ID_CATEG,
   LIB_CATEG,
   ID_DOSSIER,
   LIB_DOSSIER,
   ID_PUBLICATION,
   LIB_PUBLICATION
)
AS
   SELECT t.graphie_terme || t.extension_terme,
          t.graphie_terme,
          t.extension_terme,
          t.id_categ,
          c.lib_categ,
          t.id_dossier,
          d.lib_dossier,
          t.id_publication,
          p.lib_publication
     FROM terme t,
          categ c,
          dossier d,
          publication p,
          r_statut rs
    WHERE     rs.id_statut = 'VALID'
          AND rs.id_terme = t.id_terme
          AND t.id_categ = c.id_categ
          AND t.id_dossier = d.id_dossier
          AND t.id_publication = p.id_publication;
