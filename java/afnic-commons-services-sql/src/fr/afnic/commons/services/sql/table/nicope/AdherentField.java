/*
 * $Id: $ $Revision: $ $Author: $
 */

package fr.afnic.commons.services.sql.table.nicope;

import fr.afnic.commons.services.sql.query.Nicope;
import fr.afnic.commons.services.sql.query.TableField;

public class AdherentField extends TableField<AdherentField> {

    /**
     * 
     */
    private static final long serialVersionUID = -972070641592335998L;
    public static final AdherentField ID = new AdherentField("ID");
    public static final AdherentField CODE = new AdherentField("CODE");
    public static final AdherentField NOM = new AdherentField("NOM");
    public static final AdherentField PRESTATION = new AdherentField("PRESTATION");

    public AdherentField(final String name) {
        super(name, Nicope.ADHERENT_TABLE);
    }

}
