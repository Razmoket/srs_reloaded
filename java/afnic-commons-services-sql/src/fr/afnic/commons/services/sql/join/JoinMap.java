/*
 * $Id: $ $Revision: $ $Author: $
 */

package fr.afnic.commons.services.sql.join;

import java.util.HashMap;

import com.google.common.collect.Maps;

import fr.afnic.commons.services.sql.condition.Join;
import fr.afnic.commons.services.sql.query.Table;
import fr.afnic.commons.services.sql.query.TableField;

public class JoinMap {

    public static final JoinMap INSTANCE = new JoinMap();

    private final HashMap<TablePair, Join> joins = Maps.newHashMap();

    public JoinMap() {
    }

    public Join findJoin(final Table<?> table1, final Table<?> table2) {
        return this.joins.get(new TablePair(table1, table2));
    }

    protected void addJoin(final TableField<?> field1, final TableField<?> field2) {
        final TablePair pair = new TablePair(field1.getTable(), field2.getTable());

        if (this.joins.containsKey(pair)) {
            throw new IllegalArgumentException("There is already a join between " + field1.getTable().toSql() + " and " + field2.getName()
                                               + "\nnew join: " + field1.toSql() + " = " + field2.toSql() + "\nexisting join: " + this.joins.get(pair).toSql());

        } else {
            this.joins.put(pair, new Join(field1, field2));
        }

    }
}
