/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/beans/customer/block/BlockingType.java#8 $
 * $Revision: #8 $
 * $Author: barriere $
 */

package fr.afnic.commons.beans.customer.block;

import java.util.Locale;

import fr.afnic.commons.beans.description.IDescribedInternalObject;
import fr.afnic.commons.services.exception.NotImplementedException;
import fr.afnic.commons.services.exception.ServiceException;

public enum BlockingType implements IDescribedInternalObject {

    PendingDocument("En attente document"),
    ActivityTransfert("Transfert d'activité"),
    BillingProblem("Problème de facturation"),
    MermbershipTermination("Résiliation de l'adhésion"),
    ContractFailure("Manquement lié au contrat"),
    Other("Autre");

    private final String description;

    private BlockingType(String description) {
        this.description = description;
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
        return this.toString();
    }

    @Override
    public void setLocale(Locale locale) throws ServiceException {
        throw new NotImplementedException();
    }

}
