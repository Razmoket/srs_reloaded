/*
 * $Id: $ $Revision: $ $Author: $
 */

package fr.afnic.commons.services.sql.query;

import fr.afnic.commons.services.sql.table.whois.DomainField;
import fr.afnic.commons.services.sql.table.whois.Identification2Field;

/**
 * 
 * Description des tables du schéma Whois.
 * 
 * 
 * 
 */
public final class Whois extends TableSpace {


    private static final long serialVersionUID = 1L;

    private static final String TABLE_SPACE = "whois";

    private static final TableSpace INSTANCE = new TableSpace(Whois.TABLE_SPACE);

    public static final Table<DomainField> DOMAIN_TABLE = Whois.INSTANCE.createTable("domain");
    public static final Table<Identification2Field> IDENTIFICATION2_TABLE = Whois.INSTANCE.createTable("identification2");

    private Whois() {
        super(Whois.TABLE_SPACE);
    }
}
