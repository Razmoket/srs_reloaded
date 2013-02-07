/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/beans/ContactIdentificationStatus.java#1 $
 * $Revision: #1 $
 * $Author: alaphilippe $
 */

package fr.afnic.commons.beans;

/**
 * Statut d'identification d'un contact.
 * 
 * @author ginguene
 * 
 */
public enum ContactIdentificationStatus {
    NotIdentified("Non identifié"),
    Deprecated("Identification périmée"),
    Pending("En attente d'identification"),
    Control("En controle"),
    Delayed("Identification retardée"),
    Problem("En problème"),
    Ok("OK"),
    Ko("KO");

    private String description;

    /**
     * Constructeur permettant d'assigné une description au status.
     * 
     * @param description
     *            Description du statut en français.
     */
    private ContactIdentificationStatus(String description) {
        this.description = description;
    }

    /**
     * Retourne la description du statut.
     * 
     * @return Description du statut en français.
     */
    public String getDescription() {
        return this.description;
    }

}
