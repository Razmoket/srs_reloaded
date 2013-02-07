package fr.afnic.commons.legal;

public enum AskedAction {
    Delete("suppression"),
    Transmission("transmission");

    private final String name;

    private AskedAction(String s) {
        this.name = s;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
