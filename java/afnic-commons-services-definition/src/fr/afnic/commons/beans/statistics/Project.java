/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/beans/statistics/Project.java#2 $
 * $Revision: #2 $
 * $Author: barriere $
 */

package fr.afnic.commons.beans.statistics;

import java.io.Serializable;

/**
 * Représente un projet.<br/>
 * Le nom ne peut en aucun etre null.
 * 
 * @author ginguene
 * 
 */
public class Project implements Serializable {

    private final String name;

    /**
     * Constructeur permettant d'initialiser le nom du projet.<br/>
     * 
     * @param name
     *            Nom du projet.
     */
    public Project(String name) {
        if (name == null) {
            throw new IllegalArgumentException("name cannot be null");
        }

        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj instanceof Project) {
            return this.name.equals(((Project) obj).getName());
        } else {
            return false;
        }

    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return this.name.hashCode();
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return (this.getClass().getSimpleName() + ": name[" + this.name + "]");
    }
}
