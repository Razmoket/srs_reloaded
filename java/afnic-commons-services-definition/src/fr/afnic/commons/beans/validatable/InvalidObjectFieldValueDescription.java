package fr.afnic.commons.beans.validatable;

/**
 * Description d'une donnée invalide correspondant à une mauvaise valeur attribué à l'attribut d'un objet.
 * 
 * 
 * @author ginguene
 * 
 */
public class InvalidObjectFieldValueDescription extends InvalidDataDescription {

    private static final long serialVersionUID = 7877648729083630387L;

    protected String value;
    protected String field;
    protected Object object;

    public InvalidObjectFieldValueDescription(Object object, String field, String value) {
        super(object.getClass().getSimpleName() + "." + field + " is not valid [" + value + "]");
        this.object = object;
        this.field = field;
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public String getField() {
        return this.field;
    }

    public Object getObject() {
        return this.object;
    }

}
