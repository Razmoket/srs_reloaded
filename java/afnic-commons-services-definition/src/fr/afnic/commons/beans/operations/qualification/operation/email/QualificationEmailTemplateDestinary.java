package fr.afnic.commons.beans.operations.qualification.operation.email;

import fr.afnic.commons.beans.contactdetails.EmailAddress;
import fr.afnic.commons.beans.operations.qualification.Qualification;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.utils.Preconditions;

public enum QualificationEmailTemplateDestinary {

    Initiator,
    Holder,
    Registrar;

    public EmailAddress getEmailAddress(Qualification qualification) throws ServiceException {
        Preconditions.checkNotNull(qualification, "qualification");

        switch (this) {
        case Initiator:
            return qualification.getInitiatorEmailAddress();

        case Holder:
            return qualification.getHolderEmailAddress();

        case Registrar:
            return qualification.getRegistrarNocEmailAddress();

        default:
            throw new IllegalArgumentException("no EmailAddress defined for " + this);
        }

    }

}
