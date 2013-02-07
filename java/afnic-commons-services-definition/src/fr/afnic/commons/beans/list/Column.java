package fr.afnic.commons.beans.list;

import java.io.Serializable;

import fr.afnic.utils.ToStringHelper;

public class Column implements Serializable {

    /**Si le nom de la colonne commence par DESC__ cela signifie que pour le contenu de la colonne, on
     * cherchera dans un .property si une clé correspond au contenu de la colonne pour l'utiliser comme valeur.
     * Par exemple si une colonne se nomme DESC__OPERATION et qu'une ligne a pour valeur CreateDomain pour cette colonne,
     * Lors de l'affichage, on cherchera quel est le texte correspondant à la clé CreateDomain dans un .property*/
    private final String description;
    private final String reference;

    private final boolean isIdentifier;

    public Column(String name, String reference) {
        this(name, reference, false);
    }

    public Column(String name, String reference, boolean isIdentifier) {
        this.description = name;
        this.reference = reference;
        this.isIdentifier = isIdentifier;
    }

    public String getReference() {
        return this.reference;
    }

    public boolean isIdentifier() {
        return this.isIdentifier;
    }

    public String getDescription() {
        return this.description;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.description == null) ? 0 : this.description.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;
        Column other = (Column) obj;
        if (this.description == null) {
            if (other.description != null) return false;
        } else if (!this.description.equals(other.description)) return false;
        return true;
    }

    @Override
    public String toString() {
        return new ToStringHelper().addAllObjectAttributes(this).toString();
    }
}
