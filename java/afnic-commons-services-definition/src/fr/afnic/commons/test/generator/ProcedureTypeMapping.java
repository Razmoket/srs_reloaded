/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.test.generator;

/**
 * Copie du ProcedureTypeMapping se trouvant dans le projet Thrift.<br/>
 * Evite les référence cyclique. Pas propres car on aura des problème en cas de modification dans l'original mais on trouvera une solution propre et
 * définitive plus tard, le je suis en plein gros refactoring (JGI)
 * 
 */
public final class ProcedureTypeMapping {

    public static final int RESILIATION_DEFAUT_PAIEMENT = 1;
    public static final int RESILIATION_ADHESION = 2;
    public static final int LIQUIDATION_JUDICIAIRE = 3;
    public static final int COMPTE_BLOQUE = 4;
    public static final int LISTE_ROUGE = 5;
    public static final int LISTE_VERTE = 6;
    public static final int PRE_ENREGISTREMENT = 7;
    public static final int PROCEDURE_SAUVEGARDE = 8;
    public static final int REDRESSEMENT_JUDICIAIRE = 9;
    public static final int TRANSFERT_ACTIVITE_EN_COURS = 10;
    public static final int FR_DRP = 11;
    public static final int NOUVEL_ENTRANT_2008 = 12;
    public static final int CHANGEMENT_OPTION = 13;
    public static final int NOUVEL_ENTRANT_2009 = 14;
    public static final int NOUVEL_ENTRANT_2010 = 15;
    public static final int NOUVEL_ENTRANT_2011 = 16;

    private ProcedureTypeMapping() {

    }
}
