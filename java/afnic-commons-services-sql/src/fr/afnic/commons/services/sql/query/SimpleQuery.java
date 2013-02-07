/*
 * $Id: $ $Revision: $ $Author: $
 */

package fr.afnic.commons.services.sql.query;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.base.Objects;

import fr.afnic.commons.beans.search.SearchCriteria;
import fr.afnic.commons.beans.search.SearchCriterion;
import fr.afnic.commons.services.exception.NotFoundException;
import fr.afnic.commons.services.exception.invalidformat.InvalidFormatException;
import fr.afnic.commons.services.sql.condition.AbstractWhereCondition;
import fr.afnic.commons.services.sql.condition.ComparisonCondition;
import fr.afnic.commons.services.sql.condition.Join;
import fr.afnic.commons.services.sql.condition.WhereCondition;
import fr.afnic.commons.services.sql.join.JoinNotFoundException;
import fr.afnic.commons.services.sql.mapping.AbstractSearchCriteriaMapping;
import fr.afnic.commons.services.sql.mapping.SearchCriteriaMappingFinder;
import fr.afnic.commons.utils.Preconditions;

/**
 * Requete simple sans imbrication sur le modèle select [Fields] from [Tables] where [Conditions]
 * 
 * @author ginguene
 * 
 */
public class SimpleQuery extends ParameterizedQuery {

    /**
     * 
     */
    private static final long serialVersionUID = -4557000565241713919L;
    private FromTables from = new FromTables();
    private FieldSelect fieldSelect = new FieldSelect();
    private List<WhereCondition> whereConditions = new ArrayList<WhereCondition>();

    private int maxResultCount = 1000;

    /** Table principale de la requete */
    private Table<?> table;

    /**
     * Constructeur prenant en parametre la table principale de la requete
     * 
     * @param table
     * @throws InvalidFormatException
     */
    public SimpleQuery(final Table<?> table) {
        this.table = Preconditions.checkNotNull(table, "table");
        this.from.addTable(table);
    }

    /**
     * Constructeur par copie
     * 
     * @param query
     */
    public SimpleQuery(final SimpleQuery query) {
        Preconditions.checkNotNull(query, "query");
        this.from = query.from;
        this.fieldSelect = query.fieldSelect;
        this.whereConditions = query.whereConditions;
    }

    public void addSelectedFields(final List<Field> fields) {
        for (final Field field : fields) {
            this.addSelectedField(field);
        }
    }

    public void addSelectedField(final Field field) {
        this.fieldSelect.add(field);
        if (field instanceof TableField<?>) {
            final TableField<?> tableField = (TableField<?>) field;
            this.from.addTable(tableField.getTable());
        }
    }

    public void addWhereCondition(final WhereCondition condition) {
        this.whereConditions.add(condition);
        for (final TableField<?> field : condition.getFields()) {
            this.from.addTable(field.getTable());
        }
    }

    @Override
    public Object[] getParameters() {
        if (this.whereConditions == null || this.whereConditions.isEmpty()) {
            return new Object[0];
        }

        final List<Object> parameters = new ArrayList<Object>();
        for (final WhereCondition condition : this.whereConditions) {
            condition.populateParameters(parameters);
        }
        return parameters.toArray();
    }

    public <E extends SearchCriterion> void filterResultWithCriteria(final SearchCriteria<E> criteria) throws InvalidFormatException,
            NotFoundException {

        final AbstractSearchCriteriaMapping<SearchCriteria<E>> mapping = SearchCriteriaMappingFinder.getAbstractMapping(criteria);

        this.setMaxResultCount(criteria.getMaxResultCount());
        for (final Entry<E, Object> entry : criteria.getMap().entrySet()) {
            if (entry.getValue() != null) {
                if (mapping.getField(entry.getKey()) == null) {
                    throw new RuntimeException("No mapping defined for criterion " + entry.getKey());
                }

                this.addWhereCondition(new ComparisonCondition(mapping.getField(entry.getKey()), entry.getValue(), entry.getKey().getOperator()));
            }
        }

    }

    @Override
    public List<AbstractWhereCondition> buildWhereConditions() {
        final List<AbstractWhereCondition> conditions = new ArrayList<AbstractWhereCondition>();
        conditions.addAll(this.createJoins());
        conditions.addAll(this.whereConditions);
        return conditions;
    }

    /***
     * Crée un set de jointures pour la requete en fonction des differentes tables utilisées.
     * 
     * @return
     * @throws JoinNotFoundException
     */

    protected Set<Join> createJoins() {
        final Set<Join> joins = new HashSet<Join>();
        for (final Table<?> fromTable : this.from.getTables()) {
            if (!Objects.equal(this.table, fromTable)) {
                try {
                    joins.add(this.table.findJoinWithTable(fromTable));
                } catch (final JoinNotFoundException e) {
                    throw new InvalidFormatException(e);
                }
            }
        }
        return joins;
    }

    @Override
    public From buildFrom() {
        return this.from;
    }

    @Override
    public Select<?> buildSelect() {
        return this.fieldSelect;
    }

    public MaxResultCountQuery createMaxResultCountQuery() {
        return new MaxResultCountQuery(this, this.maxResultCount);
    }

    public int getMaxResultCount() {
        return this.maxResultCount;
    }

    public void setMaxResultCount(final int maxResultCount) {
        if (maxResultCount <= 0) {
            throw new IllegalArgumentException("maxResult must be greater than 0");
        }
        this.maxResultCount = maxResultCount;
    }

}
