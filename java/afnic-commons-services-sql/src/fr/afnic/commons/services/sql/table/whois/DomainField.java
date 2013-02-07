/*
 * $Id: $ $Revision: $ $Author: $
 */

package fr.afnic.commons.services.sql.table.whois;

import fr.afnic.commons.services.sql.query.TableField;
import fr.afnic.commons.services.sql.query.Whois;

public class DomainField extends TableField<DomainField> {

    /**
     * 
     */
    private static final long serialVersionUID = 6518505750587537931L;
    public static final DomainField ID = new DomainField("ID");
    public static final DomainField NAME = new DomainField("NAME");

    public DomainField(final String name) {
        super(name, Whois.DOMAIN_TABLE);
    }

}
