package fr.afnic.commons.beans.validatable;

import java.util.Collections;
import java.util.List;

/**
 * Description d'une donnée invalide à cause de plusieurs critères.<br/>
 * Des instances de cette classe peuvent etre construit via le builder CompoundedInvalidDataDescriptionBuilder.
 * 
 * @author ginguene
 * 
 */
public class CompoundedInvalidDataDescription extends InvalidDataDescription {

    private static final long serialVersionUID = 1L;

    private final List<InvalidDataDescription> invalidDataDescriptions;

    public CompoundedInvalidDataDescription(String description, List<InvalidDataDescription> invalidDataDescriptions) {
        super(description);
        this.invalidDataDescriptions = Collections.unmodifiableList(invalidDataDescriptions);
    }

    public List<InvalidDataDescription> getInvalidDataDescriptions() {
        return invalidDataDescriptions;
    }

    @Override
    public String getDescription() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(this.description);
        for (InvalidDataDescription invalidDataDescription : invalidDataDescriptions) {
            buffer.append("\n " + invalidDataDescription.getDescription().replaceAll("\n", "\n "));
        }
        return buffer.toString();
    }

}
