package fr.afnic.commons.beans.operations.qualification;

import java.text.DecimalFormat;

import fr.afnic.commons.beans.WhoisContact;

/**
 * Snapshot utilisé coté chaine (contient des statuts publiques)
 * @author ginguene
 *
 */
public class PublicQualificationSnapshot {

    private int id;

    private WhoisContact contact = null;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPublicId() {
        DecimalFormat formatter = new DecimalFormat("##000000000");
        return "SNAP_Q" + formatter.format(this.id);

    }

    public WhoisContact getContact() {
        return this.contact;
    }

    public void setContact(WhoisContact contact) {
        this.contact = contact;
    }
}
