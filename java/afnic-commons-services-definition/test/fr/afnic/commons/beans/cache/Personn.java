package fr.afnic.commons.beans.cache;

/**
 * Classe utilisé pour des tests
 * @author ginguene
 *
 */
class Personn {

    private final String name;
    private final int id;

    public Personn(int id, String name) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public int getId() {
        return this.id;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.id;
        result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;
        Personn other = (Personn) obj;
        if (this.id != other.id) return false;
        if (this.name == null) {
            if (other.name != null) return false;
        } else if (!this.name.equals(other.name)) return false;
        return true;
    }

    @Override
    public String toString() {
        return "Personn with name " + this.name + " and id " + this.id;
    }

}