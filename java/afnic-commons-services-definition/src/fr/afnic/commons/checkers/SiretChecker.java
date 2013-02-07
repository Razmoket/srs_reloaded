/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.checkers;

import fr.afnic.commons.beans.corporateentity.Siret;
import fr.afnic.commons.services.exception.invalidformat.InvalidFormatException;
import fr.afnic.commons.services.exception.invalidformat.InvalidSiretException;

public class SiretChecker implements IInternalChecker {

    private static final long serialVersionUID = 1L;

    private Siret siret;

    /**
     * Utilisation du siret à vérifier en parametre pour pouvoir récupérer le siren.
     * 
     * @param siret
     */
    public SiretChecker(Siret siret) {
        this.siret = siret;
    }

    public SiretChecker() {
    }

    @Override
    public String check(String siretValue) throws InvalidFormatException {

        if (this.siret != null && this.siret.getSiren() != null && siretValue.length() == 5) {
            siretValue = this.siret.getSiren().getValue() + siretValue;
        }

        if (this.isSiretSyntaxValide(siretValue)) {
            return siretValue;
        } else {
            throw new InvalidSiretException(siretValue);
        }
    }

    private boolean isSiretSyntaxValide(String siret) {
        int total = 0;
        int digit = 0;

        try {
            Long.parseLong(siret);
        } catch (Exception e) {
            return false;
        }

        if (siret.length() != 14) {
            return false;
        }

        for (int i = 0; i < siret.length(); i++) {
            /**
             * Recherche les positions impaires : 1er, 3è, 5è, etc... que l'on multiplie par 2 petite différence avec la définition ci-dessus car ici
             * on travail de gauche à droite
             */
            if ((i % 2) == 0) {
                digit = Integer.parseInt(String.valueOf(siret.charAt(i))) * 2;
                /**
                 * si le résultat est >9 alors il est composé de deux digits tous les digits devant s'additionner et ne pouvant être >19 le calcule
                 * devient : 1 + (digit -10) ou : digit - 9
                 */
                if (digit > 9) digit -= 9;
            } else
                digit = Integer.parseInt(String.valueOf(siret.charAt(i)));
            total += digit;
        }
        /** Si la somme est un multiple de 10 alors le SIRET est valide */
        return ((total % 10) == 0);
    }

}
