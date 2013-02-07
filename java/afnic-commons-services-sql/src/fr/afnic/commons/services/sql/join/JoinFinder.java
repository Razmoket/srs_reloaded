/*
 * $Id: $ $Revision: $ $Author: $
 */

package fr.afnic.commons.services.sql.join;

import fr.afnic.commons.services.exception.invalidformat.InvalidFormatException;
import fr.afnic.commons.services.sql.condition.Join;
import fr.afnic.commons.services.sql.query.Table;

/**
 * Classe utilitaire permettant de trouver les jointures entre 2 tables.
 * 
 * 
 * 
 */
public final class JoinFinder {

    private static ConstraintJoinMap constraintJoin;
    private static CustomJoinMap customJoin;

    private JoinFinder() {

    }

    /**
     * Retourne la jointure entre la table 1 et la table 2
     * 
     * @param table1
     * @return
     * @throws JoinNotFoundException
     * @throws InvalidFormatException
     */
    public static Join findJoin(final Table<?> table1, final Table<?> table2) throws JoinNotFoundException {

        Join join = JoinFinder.getCustomJoin().findJoin(table1, table2);
        if (join != null) {
            return join;
        }

        join = JoinFinder.getConstraintJoin().findJoin(table1, table2);
        if (join != null) {
            return join;
        }

        throw new JoinNotFoundException(table1, table2);
    }

    private static ConstraintJoinMap getConstraintJoin() {
        if (JoinFinder.constraintJoin == null) {
            JoinFinder.constraintJoin = new ConstraintJoinMap();
        }
        return JoinFinder.constraintJoin;
    }

    private static CustomJoinMap getCustomJoin() {
        if (JoinFinder.customJoin == null) {
            JoinFinder.customJoin = new CustomJoinMap();
        }
        return JoinFinder.customJoin;
    }
}
