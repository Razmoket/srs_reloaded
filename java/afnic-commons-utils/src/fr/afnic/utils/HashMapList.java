/*
 * $Id: $ $Revision: $ $Author: $
 */

package fr.afnic.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Gère une map contenant des liste d'élément F.
 * 
 * 
 * 
 * @author ginguene
 * 
 * @param <E>
 * @param <F>
 */
public final class HashMapList<E, F> {

    private final HashMap<E, List<F>> map = new HashMap<E, List<F>>();

    /**
     * Méthode factory évitant de répeter le parametrage de la classe.<br/>
     * on construit via:<br/>
     * HashMapList<String, Integer> map = HashMapList.create();<br/>
     * au lieu de:<br/>
     * HashMapList<String, Integer> map = new HashMapList<String, Integer>();<br/>
     * <br/>
     * ça fait plus propre.
     * 
     */
    public static <E, F> HashMapList<E, F> create() {
        return new HashMapList<E, F>();
    }

    /**
     * Il faut passer par la méthode static create pour créer un HashMapList.
     * 
     */
    private HashMapList() {
    }

    /**
     * Ajoute un élément à la liste associé à la clé
     * 
     * @param key
     * @param value
     */
    public void add(final E key, final F value) {
        if (!this.map.containsKey(key)) {
            this.map.put(key, new ArrayList<F>());
        }
        this.map.get(key).add(value);

    }

    /**
     * Retourne la liste associé à la clé.<br/>
     * Si aucun élément n'a été ajouté pour cette clé, une liste vide est retournée.
     * 
     * @param key
     * @return
     */
    public List<F> get(final E key) {
        if (!this.map.containsKey(key)) {
            return Collections.emptyList();
        } else {
            return this.map.get(key);
        }
    }

}
