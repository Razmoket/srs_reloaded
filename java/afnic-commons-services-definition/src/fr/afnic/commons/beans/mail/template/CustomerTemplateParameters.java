/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.beans.mail.template;

/**
 * Représente un parametre dans un template de mail
 * 
 * @author ginguene
 * 
 */
public enum CustomerTemplateParameters implements ITemplateParameters {

    Year("ANNEE"),
    CustomerCustomerNumber("NUMCONV"),
    CustomerName("NOM"),
    CustomerAcountManagerFirstName("CHARGE DE CLIENTELE PRENOM"),
    CustomerAcountManagerLastName("CHARGE DE CLIENTELE NOM"),
    CustomerLogin("CODEISP"),
    CustomerPassword("PASSWORD");

    private String key;

    private CustomerTemplateParameters(String key) {
        this.key = key;
    }

    @Override
    public String getKey() {
        return "###" + key + "###";
    }

}
