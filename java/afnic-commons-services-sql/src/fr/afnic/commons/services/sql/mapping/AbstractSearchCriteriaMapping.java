/*
 * $Id: $ $Revision: $ $Author: $
 */

package fr.afnic.commons.services.sql.mapping;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;

import fr.afnic.commons.beans.search.SearchCriteria;
import fr.afnic.commons.beans.search.SearchCriterion;
import fr.afnic.commons.services.sql.query.TableField;

/**
 * Permet d'associer un critère au champs d'une table de la base de données.
 * 
 */
public class AbstractSearchCriteriaMapping<C extends SearchCriteria<?>> {

    HashMap<SearchCriterion, TableField<?>> map = new HashMap<SearchCriterion, TableField<?>>();

    protected void map(final SearchCriterion criterion, final TableField<?> field) {
        this.map.put(criterion, field);
    }

    public TableField<?> getField(final SearchCriterion criterion) {
        return this.map.get(criterion);
    }

    @SuppressWarnings("unchecked")
    protected Class<C> getMatchingSearchCriteriaClass() {
        final ParameterizedType var = (ParameterizedType) this.getClass().getGenericSuperclass();
        return (Class<C>) var.getActualTypeArguments()[0];
    }

}
