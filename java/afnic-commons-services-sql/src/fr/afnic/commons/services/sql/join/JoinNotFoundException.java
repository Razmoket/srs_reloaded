/*
 * $Id: $ $Revision: $ $Author: $
 */

package fr.afnic.commons.services.sql.join;

import fr.afnic.commons.services.exception.NotFoundException;
import fr.afnic.commons.services.sql.query.Table;

/**
 * Exception levée si aucune jointure n'est trouvée entre 2 table
 * 
 * @author ginguene
 * 
 */
public class JoinNotFoundException extends NotFoundException {

    private static final long serialVersionUID = 1L;

    private final Table<?> table1;
    private final Table<?> table2;

    public JoinNotFoundException(final Table<?> table1, final Table<?> table2) {
        super("not join found between table " + table1.toString() + " and table " + table2.toString());
        this.table1 = table1;
        this.table2 = table2;
    }

    public Table<?> getTable1() {
        return this.table1;
    }

    public Table<?> getTable2() {
        return this.table2;
    }

}
