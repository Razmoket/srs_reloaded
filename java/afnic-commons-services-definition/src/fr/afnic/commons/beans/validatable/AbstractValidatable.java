/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.beans.validatable;

import java.io.Serializable;

import fr.afnic.utils.ToStringHelper;

/**
 * Interface décrivant un object dont le contenu peut etre validé. <br/>
 * Lors de l'appel à validate, si tout est ok, rien ne se passe sinon une InvalidFormatException est levée
 * 
 * @author ginguene
 * 
 */
public abstract class AbstractValidatable implements IValidatable, Serializable {

    private static final long serialVersionUID = 1L;

    private boolean hasNotAlreadyBeenValidated = true;
    private InvalidDataDescription invalidDataDescription = null;
    private final boolean autoResetActivated = this.getClass().isAnnotationPresent(ResetValidate.class);

    /**
     * Lance la validation de l'objet en recourant à l'appel de la fonction validateData().<br/>
     * Si l'on appelle plusieurs fois validate(), checkInvalidData() n'est appelée qu'au premier appel dans un soucis d'optimisation.<br/>
     * Les appels suivant se contentent de reproduire le meme comportement.<br/>
     * Si une des propriétés de l'objet change, il faut appelé resetValidate() pour que lors du prochain appel à validate() la validité de ce dernier
     * soit rééxaminée en appelant de nouveau checkInvalidData().
     * 
     * 
     * @see #validateData()
     * @see #resetValidate()
     * 
     */

    @Override
    public final void validate() throws InvalidDataException {
        if (this.hasNotAlreadyBeenValidated) {
            this.hasNotAlreadyBeenValidated = false;
            this.invalidDataDescription = this.checkInvalidData();
        }

        if (this.invalidDataDescription != null) {
            throw new InvalidDataException(this.invalidDataDescription);
        }
    }

    /***
     * Récupère un InvalidDataDescription qui décript les données invalid de l'objet.<br/>
     * Doit retourner null, si l'objet est valide.
     * 
     * 
     * @return
     */
    public abstract InvalidDataDescription checkInvalidData();

    /**
     * Fait comme si validate() n'avait jamais été appelé.<br/>
     * Ainsi lors du prochain appel à validate(), on est sur que validateData() sera appelé.<br/>
     * <br/>
     * Fonction à appeler dans les setters de la classe fille si ils changent une propriété dont dépent la validié de l'objet.
     * 
     * 
     * 
     */
    public final void resetValidate() {
        this.hasNotAlreadyBeenValidated = true;
    }

    @Override
    public String toString() {
        return new ToStringHelper().addAllObjectAttributes(this).toString();
    }

    public boolean isAutoResetActivated() {
        return this.autoResetActivated;
    }

}
