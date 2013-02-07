package fr.afnic.commons.services.sql.converter.mapping;

public enum SqlColumnWhoisContactMapping {

    contactId("contactid"),
    nom("nom"),
    prenom("prenom"),
    data("data"),
    commune("commune"),
    cedex("cedex"),
    zip("zip"),
    mediaData("mediadata"),
    prefix("prefix"),
    num("num"),
    suffix("suffix"),
    registrarCode("registrar_code");

    private final String sqlColumnName;

    private SqlColumnWhoisContactMapping(String desc) {
        this.sqlColumnName = desc;
    }

    @Override
    public String toString() {
        return this.sqlColumnName;
    }
}
