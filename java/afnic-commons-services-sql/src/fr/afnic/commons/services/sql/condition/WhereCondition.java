/*
 * $Id: $ $Revision: $ $Author: $
 */

package fr.afnic.commons.services.sql.condition;

import java.util.List;

import fr.afnic.commons.services.sql.query.TableField;

/**
 * représente une condifiont du where
 * 
 * @author ginguene
 * 
 */
public abstract class WhereCondition extends AbstractWhereCondition {

    /**
     * 
     */
    private static final long serialVersionUID = -4604511330421002615L;

    /**
     * Retourne les champs utilisés dans la condition
     * 
     * @return
     */
    public abstract List<TableField<?>> getFields();

    public abstract void populateParameters(List<Object> parameters);

}
