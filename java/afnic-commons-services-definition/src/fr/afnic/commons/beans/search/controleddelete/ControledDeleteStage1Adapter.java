/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.beans.search.controleddelete;

import java.io.Serializable;
import java.util.Arrays;

import fr.afnic.commons.beans.validatable.IValidatable;
import fr.afnic.commons.beans.validatable.InvalidDataDescription;
import fr.afnic.commons.beans.validatable.InvalidDataException;
import fr.afnic.commons.services.exception.invalidformat.InvalidDomainNameException;

public class ControledDeleteStage1Adapter implements IValidatable, Serializable {

    private static final long serialVersionUID = 1L;

    private String listDomain;
    private String[] splitListDomain;
    private String comment;

    @Override
    public void validate() throws InvalidDataException {
        if ((this.listDomain == null) || ("".equals(this.listDomain))) {
            throw new InvalidDataException(new InvalidDataDescription("La liste des domaines ne peut être vide"));
        }
        this.splitListDomain = this.listDomain.split("\\\n");
        for (String domain : this.splitListDomain) {
            if ((domain.indexOf(".") == -1) || (domain.indexOf(".") == domain.length() - 1)) {
                throw new InvalidDataException(new InvalidDataDescription("Le domaine suivant n'inclut pas de '.' ou celui-ci n'est pas suivi d'une extension : " + domain));
            }
            // contrôle allégé afin de ne pas interdire la suppression de domaine ne respectant pas les nouveau std de nommage
            if (!domain.contains(".")) {
                throw new InvalidDomainNameException(domain);
            }
        }
        if (this.comment != null) {
            if (this.comment.indexOf("_") != -1) {
                throw new InvalidDataException(new InvalidDataDescription("Le commentaire ne peut contenir de \"_\""));
            }
            if (this.comment.indexOf(":") != -1) {
                throw new InvalidDataException(new InvalidDataDescription("Le commentaire ne peut contenir de \":\""));
            }
        }
    }

    public String getListDomain() {
        return this.listDomain;
    }

    public void setListDomain(String listDomain) {
        this.listDomain = listDomain;
    }

    public String getComment() {
        return this.comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String[] getSplitListDomain() {
        return Arrays.copyOf(this.splitListDomain, this.splitListDomain.length);
    }

}
