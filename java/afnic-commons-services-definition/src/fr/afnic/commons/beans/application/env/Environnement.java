/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.beans.application.env;

import fr.afnic.commons.services.exception.IllegalArgumentException;

public enum Environnement {

    Dev,
    Preprod,
    Sandbox,
    Prod;

    public String getName() {
        return this.toString().toLowerCase();
    }

    public static Environnement getEnvironnement(String env) throws IllegalArgumentException {
        if (Dev.getName().equals(env)) {
            return Dev;
        }
        if (Preprod.getName().equals(env)) {
            return Preprod;
        }
        if (Sandbox.getName().equals(env)) {
            return Sandbox;
        }
        if (Prod.getName().equals(env)) {
            return Prod;
        }
        throw new IllegalArgumentException("Incoherent environnement parameter " + env);
    }

    public boolean isCustomerEnvironnement() {
        return this == Sandbox || this == Prod;
    }

}
