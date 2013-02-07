package fr.afnic.commons.beans.list;

import java.io.Serializable;

/**
 * Vue rattachée à un resultList
 *
 */
public interface IView extends Serializable {

    /**
     * Retourne l'identifiant de la vue
     */
    public String getIdentifier();

    public ResultList<?> createResultList();
}
