/*
 * $Id: $ $Revision: $ $Author: $
 */

package fr.afnic.commons.services.sql.table.nicope;

import fr.afnic.commons.services.sql.query.Nicope;
import fr.afnic.commons.services.sql.query.TableField;

public class NomDeDomaineField extends TableField<NomDeDomaineField> {

    /**
     * 
     */
    private static final long serialVersionUID = 2533246600726759973L;
    public static final NomDeDomaineField ID = new NomDeDomaineField("ID");
    public static final NomDeDomaineField NOM = new NomDeDomaineField("NOM");

    public NomDeDomaineField(final String name) {
        super(name, Nicope.NOMDEDOMAINE_TABLE);
    }

}
