/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.beans.customer;

import java.util.Locale;

import fr.afnic.commons.beans.description.IDescribedInternalObject;
import fr.afnic.commons.services.exception.NotImplementedException;
import fr.afnic.commons.services.exception.ServiceException;

/**
 * Représente le mode de paiement.
 * 
 * @author ginguene
 * 
 */
public enum PaymentMethod implements IDescribedInternalObject {

    CreditCard("Carte de crédit", "CB"), // Carte de crédit
    BankTransfer("Virement", "VI"), // Virement
    Check("Chèque", "CH"), // chèque
    DirectDebit("Prélèvement", "PR");// Prélèvement bancaire

    /** Traduction dans le constructeur faute de temps */
    private final String description;

    /** Le code correspond au code historique du mode de paiement que l'on retrouve dans les bases de données */
    private final String code;

    private PaymentMethod(String description, String code) {
        this.description = description;
        this.code = code;
    }

    @Override
    public String getDescription() throws ServiceException {
        return this.description;
    }

    @Override
    public String getDescription(Locale locale) throws ServiceException {
        throw new NotImplementedException();
    }

    @Override
    public String getDictionaryKey() {
        return this.code;
    }

    public static PaymentMethod getPaymentMethodByCode(String code) {
        if (code == null) {
            return null;
        }

        for (PaymentMethod paymentMethod : PaymentMethod.values()) {
            if (paymentMethod.getDictionaryKey().equals(code)) {
                return paymentMethod;
            }
        }
        throw new IllegalArgumentException("Given code (" + code + ") is undefined.");
    }

    @Override
    public void setLocale(Locale locale) throws ServiceException {
        throw new NotImplementedException();
    }
}
