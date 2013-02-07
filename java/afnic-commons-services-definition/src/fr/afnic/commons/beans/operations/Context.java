package fr.afnic.commons.beans.operations;

import fr.afnic.commons.beans.boarequest.TopLevelOperation;
import fr.afnic.commons.beans.validatable.AbstractValidatable;
import fr.afnic.commons.beans.validatable.InvalidDataDescription;

/**
 * Objet chargé de contenir les données de différentes opérations sensés manipuler les même données.
 * 
 * @author ginguene
 *
 */
public class Context<OBJECT extends TopLevelOperation> extends AbstractValidatable {

    private OBJECT object;

    public Context(OBJECT object) {
        this.object = object;
    }

    public OBJECT getObject() {
        return this.object;
    }

    public void setObject(OBJECT object) {
        this.object = object;
    }

    @Override
    public InvalidDataDescription checkInvalidData() {
        if (this.object == null) {
            return new InvalidDataDescription("object is null");
        } else {
            return null;
        }

    }

}
