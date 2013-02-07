/*
 * $Id: $ $Revision: $ $Author: $
 */

package fr.afnic.commons.services.sql.query;

import java.util.ArrayList;
import java.util.List;

import fr.afnic.commons.services.sql.condition.AbstractWhereCondition;
import fr.afnic.commons.services.sql.condition.CustomWhereCondition;
import fr.afnic.commons.utils.Preconditions;

/**
 * Retourne que les maxResultCount résultats d'une requete.
 * 
 * @author ginguene
 * 
 */
public class MaxResultCountQuery extends ParameterizedQuery {

    /**
     * 
     */
    private static final long serialVersionUID = -2078788812230027178L;
    private final SimpleQuery query;
    private final FromQuery from;
    private final List<AbstractWhereCondition> conditions;

    private static final MaxResultCountQuerySelect SELECT = new MaxResultCountQuerySelect();

    public MaxResultCountQuery(final SimpleQuery query, final int maxResultCount) {
        this.query = Preconditions.checkNotNull(query, "query");
        this.from = new FromQuery(this.query);

        // On demande 1 élément de plus que le maximum. ainsi si l'on récupère maxResultCount + 1, c'est qu'il y a plus
        // de résultat que ce à quoi on s'attendait (au moins 1 de plus)
        final CustomWhereCondition condition = new CustomWhereCondition("rownum <= " + maxResultCount + 1);
        this.conditions = new ArrayList<AbstractWhereCondition>();
        this.conditions.add(condition);
    }

    @Override
    public Select<?> buildSelect() {
        return MaxResultCountQuery.SELECT;
    }

    @Override
    public From buildFrom() {
        return this.from;
    }

    @Override
    public List<AbstractWhereCondition> buildWhereConditions() {
        return this.conditions;
    }

    @Override
    public Object[] getParameters() {
        return this.query.getParameters();
    }
}
