/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/beans/statistics/ProjectFactory.java#2 $
 * $Revision: #2 $
 * $Author: ginguene $
 */

package fr.afnic.commons.beans.statistics;

/**
 * Classe chargée de la production des Projets.
 * 
 * @author ginguene
 * 
 */
public final class ProjectFactory {

    /**
     * Constructeur privé car il s'agit d'une classe utilitaire.
     */
    private ProjectFactory() {

    }

    public static final Project BOA = new Project("Boa");

}
