/*
 * $Id: $ $Revision: $ $Author: $
 */

package fr.afnic.commons.services.sql.table.whois;

import fr.afnic.commons.services.sql.query.TableField;
import fr.afnic.commons.services.sql.query.Whois;

public class Identification2Field extends TableField<Identification2Field> {

    /**
     * 
     */
    private static final long serialVersionUID = -6850802722767650054L;
    public static final Identification2Field CONTACT_ID = new Identification2Field("CONTACT_ID");
    public static final Identification2Field DATA = new Identification2Field("DATA");

    public Identification2Field(final String name) {
        super(name, Whois.IDENTIFICATION2_TABLE);
    }

}
