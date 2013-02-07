/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/beans/RegistrarList.java#4 $
 * $Revision: #4 $
 * $Author: ginguene $
 */

package fr.afnic.commons.beans;

/**
 * Les noms de status sont foireux, je l'ai ai pris directement dans la base sachant qu'ils ont un lourd historique. A l'occasion il faudra
 * entièrement les revoir
 * 
 * @author ginguene
 * 
 * @deprecated utilisé la base client
 * 
 */
public enum RegistrarList {

    DQ("Résiliation adhésion"),
    LN("Compte bloqué"),
    LV("Liste verte"),
    LR("Liste rouge"),
    PE("Pré-enregistrement"),
    DP("Résiliation adhésion pour défaut de paiement"),
    RJ("Redressement judiciaire"),
    LJ("Liquidation judiciaire"),
    TA("Transfert activité en cours"),
    X1("FR-DRP"),
    PS("Procédure de sauvegarde"),
    E1("Dev. Extension 1"),
    E2("Dev. Extension 2"),
    Z9("Nouvel Entrant 2015"),
    Z8("Nouvel Entrant 2014"),
    Z7("Nouvel Entrant 2013"),
    Z6("Nouvel Entrant 2012"),
    Z5("Nouvel Entrant 2011"),
    Z4("Nouvel Entrant 2010"),
    Z3("Nouvel Entrant 2009"),
    Z1("Nouvel Entrant 2008"),
    Z2("Changement Option"),
    M1("Bureau Enregistrement"),
    M2("Utilisateur Personne Morale"),
    M3("Utilisateur Personne Physique"),
    M4("Correspondant-Collège International"),
    M5("Membre Honneur"),
    M6("Membre Conseil Administration"),
    M7("Membre Fondateur"),
    C1("Option 1"),
    C2("Option 2"),
    P1("SQUAW"),
    P2("PARL"),
    P3("Institutionnel");

    private String description;

    /**
     * Constructeur permettant d'associé une description au code d'une liste.
     * 
     * @param description
     */
    private RegistrarList(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

}
