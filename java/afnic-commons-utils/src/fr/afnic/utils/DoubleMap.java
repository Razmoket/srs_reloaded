/*
 * $Id: //depot/main/java/afnic-commons-utils/src/fr/afnic/utils/DoubleMap.java#4 $ $Revision: #4 $ $Author: ginguene $
 */

package fr.afnic.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Map à double ses, a partir de la clé on peut obtenir la valeur et à partir de la valeur on peut obtenir la clé. aucune valeur ou clé ne peut etre
 * ajoutée 2 fois, sinon on récupére une IllegalArgumentException
 * 
 * @author ginguene
 * 
 * @param <U>
 * @param <T>
 */
public class DoubleMap<U, T> {

    private final Map<U, T> mapKey = new HashMap<U, T>();
    private final Map<T, U> mapValue = new HashMap<T, U>();

    public void put(final U key, final T value) {
        if (this.mapKey.containsKey(key)) throw new IllegalArgumentException("key " + key + " is already in map");
        if (this.mapValue.containsKey(value)) throw new IllegalArgumentException("value " + value + " is already in map");

        this.mapKey.put(key, value);
        this.mapValue.put(value, key);
    }

    public T getWithKey(final U key) {
        return this.mapKey.get(key);
    }

    public U getWithValue(final T value) {
        return this.mapValue.get(value);
    }
}
