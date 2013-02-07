package fr.afnic.commons.beans;

/**
 * Top Level Domain (extension)
 * 
 * @author ginguene
 *
 */
public enum Tld {

    Fr(1),
    Paris(2),
    Ovh(3),
    Alsace(4);

    private int idTldGrc;

    public int getIdTldGrc() {
        return this.idTldGrc;
    }

    private Tld(int id) {
        this.idTldGrc = id;
    }

    public static Tld findTldById(int toFind) {
        for (Tld tldTemp : Tld.values()) {
            if (tldTemp.getIdTldGrc() == toFind)
                return tldTemp;
        }
        return null;
    }

}
