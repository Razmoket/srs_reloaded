/*
 * $Id: $ $Revision: $ $Author: $
 */

package fr.afnic.commons.services.sql.query;

/**
 * Requete qui a des parametres (? dans la close where)
 * 
 * @author ginguene
 * 
 */
public abstract class ParameterizedQuery extends AbstractQuery {

    /**
     * 
     */
    private static final long serialVersionUID = -2061029104612434715L;

    public abstract Object[] getParameters();
}
