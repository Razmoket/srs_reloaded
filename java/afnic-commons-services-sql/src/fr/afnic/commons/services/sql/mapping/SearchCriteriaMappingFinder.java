/*
 * $Id: $ $Revision: $ $Author: $
 */

package fr.afnic.commons.services.sql.mapping;

import fr.afnic.commons.beans.search.SearchCriteria;
import fr.afnic.commons.beans.search.SearchCriterion;
import fr.afnic.commons.services.exception.NotFoundException;

/**
 * Permet de récupérer le mapping correspondant à un crière de recherche.<br/>
 * On aurait pu mettre cette logique dans le criteria via un getMapping() mais cela aurait lié le Criteria à la base de données, or <br/>
 * cette classe n'est pas sensé avoir de dépendences vis à vis de l'impléméntation de la récupération des données.
 * 
 * 
 * TODO optimisable en utilisant une map plutot qu'un tableau à parcourir à chaque appel.
 * 
 * @author ginguene
 * 
 */
public final class SearchCriteriaMappingFinder {

    private static AbstractSearchCriteriaMapping<?>[] mappings = new AbstractSearchCriteriaMapping<?>[] { new TicketSearchMapping() };

    @SuppressWarnings("unchecked")
    public static <E extends SearchCriterion> AbstractSearchCriteriaMapping<SearchCriteria<E>> getAbstractMapping(final SearchCriteria<E> searchCriteria)
                                                                                                                                                         throws NotFoundException {
        for (final AbstractSearchCriteriaMapping<?> mapping : SearchCriteriaMappingFinder.mappings) {
            if (mapping.getMatchingSearchCriteriaClass() == searchCriteria.getClass()) {
                return (AbstractSearchCriteriaMapping<SearchCriteria<E>>) mapping;
            }
        }
        throw new NotFoundException("cannot find mapping for search criteria " + searchCriteria.getClass().getSimpleName());
    }

    private SearchCriteriaMappingFinder() {

    }
}
