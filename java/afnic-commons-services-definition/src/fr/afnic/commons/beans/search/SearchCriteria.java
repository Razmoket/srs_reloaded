/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.beans.search;

import java.io.Serializable;
import java.util.HashMap;

import com.google.common.base.Preconditions;

/**
 * Liste de critere de recherche.<br/>
 * C'est une map de SearchCriterion, ce qui permet ensuite de traiter toutes les SearchCriteria de la meme manière sans passer par la reflexion.
 * 
 * @author ginguene
 * 
 * @param <E>
 */
public abstract class SearchCriteria<E extends SearchCriterion> implements Serializable {

    private static final long serialVersionUID = 1L;

    private HashMap<E, Object> criterionMap = new HashMap<E, Object>();

    private boolean exactMatch = true;
    private boolean ignoreCase = false;

    /** Nombre maximal de réponse attendue */
    private int maxResultCount = 1000;

    public void addCriterion(E criterion, Object value) {
        this.criterionMap.put(Preconditions.checkNotNull(criterion, "criteron cannot be null."), value);
    }

    protected Object getCriterionValue(E criterion) {
        return this.criterionMap.get(criterion);
    }

    public HashMap<E, Object> getMap() {
        return this.criterionMap;
    }

    @Override
    public String toString() {
        // Le toStringHelper ne fonctionne pas, on retourne donc le contenu de la map.
        return this.criterionMap.toString();
    }

    public int getMaxResultCount() {
        return this.maxResultCount;
    }

    public void setMaxResultCount(int maxResultCount) {
        if (maxResultCount <= 0) {
            throw new IllegalArgumentException("maxResult must be greater than 0");
        }
        this.maxResultCount = maxResultCount;
    }

    public boolean isNotExactMatch() {
        return !this.isExactMatch();
    }

    public boolean isExactMatch() {
        return this.exactMatch;
    }

    public void setExactMatch(boolean exactMatch) {
        this.exactMatch = exactMatch;
    }

    public boolean isNotIgnoreCase() {
        return !this.isIgnoreCase();
    }

    public boolean isIgnoreCase() {
        return this.ignoreCase;
    }

    public void setIgnoreCase(boolean ignoreCase) {
        this.ignoreCase = ignoreCase;
    }

    public boolean hasNoCriteronFilled() {
        return this.criterionMap == null || this.criterionMap.isEmpty();
    }

}
