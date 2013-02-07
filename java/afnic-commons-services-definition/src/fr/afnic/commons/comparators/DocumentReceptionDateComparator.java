/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/comparators/DocumentReceptionDateComparator.java#1 $
 * $Revision: #1 $
 * $Author: alaphilippe $
 */

package fr.afnic.commons.comparators;

import java.util.Comparator;

import fr.afnic.commons.beans.documents.Document;

/**
 * Classe contenant un singleton permettant de comparé des documents.<br/>
 * Permet de trier les documents du plus récent au plus vieux.<br/>
 * Appliqué à une liste, on aura comme premier élément, le document recu le plus recemment.<br/>
 * 
 * @author ginguene
 * 
 */
public final class DocumentReceptionDateComparator implements Comparator<Document> {

    /** instance du singleton */
    private static DocumentReceptionDateComparator instance = null;

    public static DocumentReceptionDateComparator getInstance() {
        if (instance == null) {
            instance = new DocumentReceptionDateComparator();
        }
        return instance;
    }

    /**
     * Constucteur privé pour empecher l'instanciation extérieur de l'objet
     */
    private DocumentReceptionDateComparator() {
    }

    @Override
    public int compare(Document arg0, Document arg1) {

        // Un document ou une date null est considéré comme inferieur à du non null
        if (arg0 == null) return -1;
        if (arg1 == null) return 1;
        if (arg0.getReceptionDate() == null) return -1;
        if (arg1.getReceptionDate() == null) return 1;

        // Pas de valeurs nulles, on retourne l'ordre indiqué par les dates de reception
        return arg1.getReceptionDate().compareTo(arg0.getReceptionDate());
    }

}
