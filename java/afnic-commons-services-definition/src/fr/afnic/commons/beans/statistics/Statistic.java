/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/beans/statistics/Statistic.java#2 $
 * $Revision: #2 $
 * $Author: barriere $
 */

package fr.afnic.commons.beans.statistics;

import java.io.Serializable;

/**
 * Représente une statistique quotidienne pour un projet Afnic.
 * 
 * @author ginguene
 * 
 */
public class Statistic implements Serializable {

    protected int id;
    protected Project project;
    protected String label;
    protected IStatisticValueFormatter valueFormatter;

    /**
     * Constructeur de statistique.
     * 
     * @param project
     *            Projet concerné par la statistique
     * 
     * @param label
     *            Nom de la statistique
     * 
     * @param valueFormatter
     *            Objet charger de transformer la valeur de la classe en String
     * 
     */
    public Statistic(Project project, String label, IStatisticValueFormatter valueFormatter) {
        if (project == null) {
            throw new IllegalArgumentException("project cannot be null");
        }

        if (label == null) {
            throw new IllegalArgumentException("project cannot be null");
        }

        if (valueFormatter == null) {
            throw new IllegalArgumentException("project cannot be null");
        }

        this.project = project;
        this.label = label;
        this.valueFormatter = valueFormatter;
    }

    /**
     * Constructeur sans formatter, dans ce cas on utilise un FloatFormatter.
     * 
     * @param project
     *            Projet concerné par la statistique
     * 
     * @param label
     *            Nom de la statistique
     */
    public Statistic(Project project, String label) {
        this(project, label, new FloatFormatter());
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Project getProject() {
        return this.project;
    }

    public String getLabel() {
        return this.label;
    }

    /**
     * 2 statistiques sont égales si elles ont le même projet et le même label.
     * 
     * @param obj
     *            Objet à comparer.
     * 
     * @return true si les 2 statistiques sont égales.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (!Statistic.class.isAssignableFrom(obj.getClass())) {
            return false;
        }

        Statistic projectStat = (Statistic) obj;

        if (this.project != null) {
            if (!this.project.equals(projectStat.getProject())) {
                return false;
            }
        } else {
            if (projectStat.getProject() != null) {
                return false;
            }
        }

        if (this.label != null) {
            if (!this.label.equals(projectStat.getLabel())) {
                return false;
            }
        } else {
            if (projectStat.getLabel() != null) {
                return false;
            }
        }

        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return (this.project + this.label).hashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "Statistic: project[" + this.project + "] label[" + this.label + "]";
    }

    /**
     * retourne une chaine de caractère représentant la valeur de la statistic.
     * 
     * @param value
     *            Valeur à formatter
     * @return Chaine de caractère.
     */
    public String formatValue(float value) {
        return this.valueFormatter.format(value);
    }

}
