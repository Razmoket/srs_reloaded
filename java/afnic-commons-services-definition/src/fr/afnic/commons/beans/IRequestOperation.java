package fr.afnic.commons.beans;

import fr.afnic.commons.beans.description.IDescribedExternallyObject;

public interface IRequestOperation extends IDescribedExternallyObject {

    public TicketOperation getTicketOperation();

    public String getOperationId();

}
