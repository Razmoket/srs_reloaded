/*
 * $Id: $ $Revision: $ $Author: $
 */

package fr.afnic.utils.filters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.common.base.Preconditions;

/**
 * Classe dont les filles peuvent filtrer des listes d'éléments
 * 
 * @author ginguene
 * 
 * @param <E>
 */
public abstract class Filter<E> {

    public List<E> findElements(final Collection<E> elements) {
        Preconditions.checkNotNull(elements);
        final List<E> filteredElements = new ArrayList<E>();

        for (final E element : elements) {
            if (this.test(element)) {
                filteredElements.add(element);
            }
        }
        return filteredElements;
    }

    public E findFirstElement(final Collection<E> elements) {
        Preconditions.checkNotNull(elements);

        for (final E element : elements) {
            if (this.test(element)) {
                return element;
            }
        }
        return null;
    }

    /**
     * Indique si l'element doit etre retourné par le filtre.
     * 
     * @param element
     * @return
     */
    public abstract boolean test(E element);

}
