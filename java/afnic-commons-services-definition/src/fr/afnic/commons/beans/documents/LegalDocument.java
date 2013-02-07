package fr.afnic.commons.beans.documents;

import fr.afnic.commons.beans.operations.qualification.QualificationSource;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class LegalDocument extends SimpleDocument implements Cloneable {

    private String holderHandle;

    private String initiatorEmail;

    private QualificationSource qualificationSource;

    private String domainName;

    public LegalDocument(UserId userId, TldServiceFacade tld) {
        super(userId, tld);
    }

    public LegalDocument(String handle, UserId userId, TldServiceFacade tld) {
        super(userId, tld);
        this.handle = handle;
    }

    public String getHolderHandle() {
        return this.holderHandle;
    }

    public void setHolderHandle(String holderHandle) {
        this.holderHandle = holderHandle;
    }

    public String getInitiatorEmail() {
        return this.initiatorEmail;
    }

    public void setInitiatorEmail(String initiatorEmail) {
        this.initiatorEmail = initiatorEmail;
    }

    public QualificationSource getQualificationSource() {
        return this.qualificationSource;
    }

    public void setQualificationSource(QualificationSource qualificationSource) {
        this.qualificationSource = qualificationSource;
    }

    public String getDomainName() {
        return this.domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

}
