/*
 * $Id: $ $Revision: $ $Author: $
 */

package fr.afnic.commons.services.sql.join;

import com.google.common.base.Preconditions;

import fr.afnic.commons.services.sql.query.Table;

/**
 * Association de 2 tables. <br/>
 * 2 Pairs inversés sont égales et retournent le meme hashcode.
 * 
 * @author ginguene
 * 
 */
public class TablePair {
    private final Table<?> table1;
    private final Table<?> table2;

    TablePair(final Table<?> table1, final Table<?> table2) {
        this.table1 = Preconditions.checkNotNull(table1, "table1 cannot be null.");
        this.table2 = Preconditions.checkNotNull(table2, "table2 cannot be null.");
    }

    /**
     * 2 pairs inverséses doivent avoir le même code.
     * 
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        final int hashCodeTable1 = ((this.table1 == null) ? 0 : this.table1.hashCode());
        final int hashCodeTable2 = ((this.table2 == null) ? 0 : this.table2.hashCode());

        int result = 1;
        result = prime * result + Math.min(hashCodeTable1, hashCodeTable2);
        result = prime * result + Math.max(hashCodeTable1, hashCodeTable2);
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;

        final TablePair other = (TablePair) obj;

        return (this.table1.equals(other.table1) && this.table2.equals(other.table2))
                || (this.table1.equals(other.table2) && this.table2.equals(other.table1));
    }
}
