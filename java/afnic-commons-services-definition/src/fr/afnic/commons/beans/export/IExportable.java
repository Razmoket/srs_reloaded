package fr.afnic.commons.beans.export;

import java.io.Serializable;

import fr.afnic.commons.services.exception.ServiceException;

public interface IExportable extends Serializable {
    public CharSequence createStream() throws ServiceException;
}
