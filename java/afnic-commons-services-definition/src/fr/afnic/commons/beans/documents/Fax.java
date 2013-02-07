/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/beans/documents/Fax.java#4 $
 * $Revision: #4 $
 * $Author: barriere $
 */

package fr.afnic.commons.beans.documents;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.utils.ToStringHelper;

/**
 * Représente un document reçu par fax.
 * 
 * 
 * @author ginguene
 * 
 */
public class Fax {

    /** Emetteur du fax */
    private String sender;

    /** Destinataire du fax */
    private String source;

    /** Date de reception du fax */
    private Date receptionDate;

    /** Fichier contenant le contenu du fax */
    private File file;

    public String getSender() {
        return this.sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSource() {
        return this.source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    /**
     * Retourne un clone de la date de reception
     * 
     * @return un clone de la date de reception
     */
    public Date getReceptionDate() {
        if (this.receptionDate != null) {
            return (Date) this.receptionDate.clone();
        } else {
            return null;
        }
    }

    /**
     * Initialise la date de reception du fax<br/>
     * avec un clone du parametre.
     * 
     * @param receptionDate
     *            nouvelle date de reception du fax
     */
    public void setReceptionDate(Date receptionDate) {
        if (receptionDate != null) {
            this.receptionDate = (Date) receptionDate.clone();
        } else {
            this.receptionDate = null;
        }
    }

    public File getFile() {
        return this.file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public GddDocument toGddDocument(UserId userId, TldServiceFacade tld) throws IOException {
        final String title = "Fax received from " + this.getSender();
        final GddDocument document = new GddDocument(userId, tld);
        document.setTitle(title);
        document.setSource(DocumentSource.Fax);
        document.setSender(this.getSender());
        document.setReceptionDate(this.getReceptionDate());

        document.setFileName(this.getFile().getCanonicalPath());
        return document;
    }

    @Override
    public String toString() {
        return new ToStringHelper().addAllObjectAttributes(this).toString();
    }

}
