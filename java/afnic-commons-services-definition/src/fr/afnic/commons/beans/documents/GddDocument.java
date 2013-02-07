/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/beans/documents/GddDocument.java#12 $
 * $Revision: 
 * $Author: barriere $
 */

package fr.afnic.commons.beans.documents;

import org.apache.commons.lang.StringUtils;

import fr.afnic.commons.beans.IRequestOperation;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.tld.TldServiceFacade;

/**
 * Document destiné à l'équipe GDD (Gestion Des Domaines). <br/>
 * C'est un document classique avec comme méta données supplémentaires: <br/>
 * domain: nom du domaine concernée par le document operation: operation à laquelle est lié le document<br/>
 * registrarCode: code du bureau d'enregistrement (le sortant si l'opération nécéssite un entrant et un sortant)<br/>
 * contactHandle: nichandle du titulaire concerné par l'opération (le sortant si l'opération nécéssite<br/>
 * un entrant et un sortant)
 * 
 * @author ginguene
 * 
 */
public class GddDocument extends SimpleDocument implements Cloneable {

    private static final long serialVersionUID = 1L;

    /**
     * Constructeur par défaut
     */
    public GddDocument(UserId userId, TldServiceFacade tld) {
        super(userId, tld);
    }

    /**
     * Constructeur prenant en parametre l'identifiant du document.
     * 
     * @param handle
     */
    public GddDocument(String handle, UserId userId, TldServiceFacade tld) {
        super(userId, tld);
        this.handle = handle;
    }

    /** application/pdf pour les fax */
    private String type;

    /** nom du domaine concernée par le document */
    private String domain;

    /** operation à laquelle est lié le document */
    private IRequestOperation operation;

    /** code du bureau d'enregistrement (le sortant si l'opération nécéssite un entrant et un sortant) */
    private String registrarCode;

    /** nichandle du titulaire concerné par l'opération (le sortant si l'opération nécéssite un entrant et un sortant) */
    private String contactHandle;

    public String getDomain() {
        return this.domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public IRequestOperation getRequestOperation() {
        return this.operation;
    }

    public void setRequestOperation(IRequestOperation operation) {
        this.operation = operation;
    }

    public String getContactHandle() {
        return this.contactHandle;
    }

    public void setContactHandle(String contactHandle) {
        this.contactHandle = contactHandle;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRegistrarCode() {
        return this.registrarCode;
    }

    public void setRegistrarCode(String registrarCode) {
        this.registrarCode = registrarCode;
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof GddDocument)) {
            return false;
        }
        GddDocument document = (GddDocument) object;

        boolean equalsReceptionDate = false;

        if (this.receptionDate != null) {
            equalsReceptionDate = this.receptionDate.equals(document.getReceptionDate());
        } else {
            equalsReceptionDate = (document.getReceptionDate() == null);
        }

        return StringUtils.equals(this.type, document.getType())
               && StringUtils.equals(this.handle, document.getHandle())
               && StringUtils.equals(this.contactHandle, document.getContactHandle())
               && StringUtils.equals(this.domain, document.getDomain())
               && StringUtils.equals(this.registrarCode, document.getRegistrarCode())
               && StringUtils.equals(this.sender, document.getSender())
               && StringUtils.equals(this.title, document.getTitle())
               && StringUtils.equals(this.comment, document.getComment())
               && equalsReceptionDate;

    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        if (this.handle != null) {
            return this.handle.hashCode();
        } else {
            return super.hashCode();
        }
    }

    /**
     * Constructeur par copie
     * 
     * @param gddDocument
     * @throws CloneNotSupportedException
     */
    public GddDocument copy(GddDocument gddDocument) throws CloneNotSupportedException {
        return (GddDocument) super.clone();
    };

}
