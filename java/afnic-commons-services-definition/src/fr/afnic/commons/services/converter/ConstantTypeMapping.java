/*
 * $Id: $
 * $Revision: $
 */

package fr.afnic.commons.services.converter;

import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.utils.DoubleMap;

/**
 * Permet de convertir des type static d'un modele vers le modèle commons et inversement.<br/>
 * Surtout utilisé pour convertir des entiers en enumeration du modèle commons.
 * 
 * @param <OTHER_MODEL_CLASS>
 * @param <COMMONS_CLASS>
 */
public abstract class ConstantTypeMapping<OTHER_MODEL_CLASS, COMMONS_CLASS> {

    private final DoubleMap<OTHER_MODEL_CLASS, COMMONS_CLASS> map = new DoubleMap<OTHER_MODEL_CLASS, COMMONS_CLASS>();

    private final Class<OTHER_MODEL_CLASS> otherModelClass;
    private final Class<COMMONS_CLASS> commonsClass;

    protected ConstantTypeMapping(Class<OTHER_MODEL_CLASS> otherModelClass, Class<COMMONS_CLASS> commonsClass) {
        this.commonsClass = commonsClass;
        this.otherModelClass = otherModelClass;
        try {
            this.populateMap();
        } catch (Exception e) {
            throw new IllegalArgumentException(this.getClass().getSimpleName() + ".populateMap() failed", e);
        }
    }

    protected void addMapping(OTHER_MODEL_CLASS otherModelValue, COMMONS_CLASS commonsValue) {
        this.map.put(otherModelValue, commonsValue);
    }

    public OTHER_MODEL_CLASS getOtherModelValue(COMMONS_CLASS commonsValue) {
        OTHER_MODEL_CLASS ret = this.map.getWithValue(commonsValue);
        if (ret == null) {
            throw new IllegalArgumentException(this.getClass().getSimpleName() + ": no other model value for " + commonsValue);
        }
        return ret;
    }

    public COMMONS_CLASS getCommonsValue(OTHER_MODEL_CLASS otherModelValue, UserId userId, TldServiceFacade tld) {
        COMMONS_CLASS ret = this.map.getWithKey(otherModelValue);
        if (ret == null) {
            throw new IllegalArgumentException("no commons value for " + otherModelValue + " in " + this.getClass().getSimpleName());
        }
        return ret;
    }

    /**
     * Méthode à implémenter dans les classe filles pour remplir la map en appelant addMapping().
     * 
     */
    protected abstract void populateMap();

    public Class<OTHER_MODEL_CLASS> getOtherModelClass() {
        return this.otherModelClass;
    }

    public Class<COMMONS_CLASS> getCommonsClass() {
        return this.commonsClass;
    }

}
