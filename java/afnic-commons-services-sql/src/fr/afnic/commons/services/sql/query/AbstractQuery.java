/*
 * $Id: $ $Revision: $ $Author: $
 */

package fr.afnic.commons.services.sql.query;

import java.util.List;

import com.google.common.base.Joiner;

import fr.afnic.commons.beans.validatable.CompoundedInvalidDataDescriptionBuilder;
import fr.afnic.commons.beans.validatable.InvalidDataDescription;
import fr.afnic.commons.services.sql.condition.AbstractWhereCondition;

/**
 * Requete composé d'un select, un from et un to. La construction de ces éléments est délégués aux classes filles (Pattern template method)
 * 
 * @author ginguene
 * 
 */

public abstract class AbstractQuery extends AbstractSqlObject {

    /**
     * 
     */
    private static final long serialVersionUID = 2821035939678287005L;
    private Select<?> select;
    private From from;
    private List<AbstractWhereCondition> whereConditions;

    @Override
    public final String toSql() {
        this.checkInvalidData();

        final StringBuffer buffer = new StringBuffer();
        buffer.append("select " + this.select.toSql());
        buffer.append(" \nfrom " + this.from.toSql());

        if (!this.whereConditions.isEmpty()) {
            buffer.append(" \nwhere ");
            buffer.append(Joiner.on("\nand ").join(this.whereConditions));
        }
        return buffer.toString();
    }

    @Override
    public InvalidDataDescription checkInvalidData() {
        this.initialize();

        final CompoundedInvalidDataDescriptionBuilder builder = new CompoundedInvalidDataDescriptionBuilder(this);
        builder.checkValidatableField(this.select, "select");
        builder.checkValidatableField(this.from, "from");
        builder.checkValidatableCollectionField(this.whereConditions, "whereConditions");
        return builder.build();
    }

    private void initialize() {
        this.select = this.buildSelect();
        this.from = this.buildFrom();
        this.whereConditions = this.buildWhereConditions();
        this.validate();
    }

    public abstract Select<?> buildSelect();

    public abstract From buildFrom();

    public abstract List<AbstractWhereCondition> buildWhereConditions();

}
