/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/beans/corporateentity/Siret.java#1 $
 * $Revision: #1 $
 * $Author: ginguene $
 */

package fr.afnic.commons.beans.corporateentity;

import fr.afnic.commons.checkers.IInternalChecker;
import fr.afnic.commons.checkers.SiretChecker;

/**
 * Siret d'une entreprise.
 * 
 * Soit on a un siret à 14 chiffres. <br/>
 * Soi on a un siret à 5 chiffres et les 9 autres sont contenus dans le siren. <br/>
 * 
 * @author ginguene
 * 
 */
public class Siret extends CorporateEntityIdentifier {

    private static final long serialVersionUID = 1L;

    /** Utilisé si la value ne fait que 5 caractèes */
    private Siren siren;

    public Siret(String value) {
        super(value);
    }

    public Siret(Siren siren, String value) {
        super(value);
        this.siren = siren;
    }

    public Siren getSiren() {
        return this.siren;
    }

    public void setSiren(Siren siren) {
        this.siren = siren;
    }

    @Override
    protected IInternalChecker createChecker() {
        return new SiretChecker(this);
    }
}
