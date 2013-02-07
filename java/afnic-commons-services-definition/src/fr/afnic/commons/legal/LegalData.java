package fr.afnic.commons.legal;

import java.io.Serializable;
import java.util.Date;

import fr.afnic.commons.beans.Ticket;
import fr.afnic.commons.beans.domain.Domain;
import fr.afnic.commons.beans.domain.DomainStatus;
import fr.afnic.commons.beans.operations.qualification.PortfolioStatus;
import fr.afnic.commons.services.exception.ServiceException;

public class LegalData implements Serializable {

    private static final long serialVersionUID = 1L;

    private SyreliLegalData syreliLegalData;

    private Domain domain;

    private boolean isOkAGTF;

    private boolean isOkBOA;

    /** pointeur vers le ticket en cours sur le domain, si il existe*/
    private Ticket ticket;

    private PortfolioStatus portfolioStatus;

    public SyreliLegalData getSyreliLegalData() {
        return this.syreliLegalData;
    }

    public void setSyreliLegalData(SyreliLegalData syreliLegalData) {
        this.syreliLegalData = syreliLegalData;
    }

    public boolean isOkAGTF() {
        return this.isOkAGTF;
    }

    public void setOkAGTF(boolean isOkAGTF) {
        this.isOkAGTF = isOkAGTF;
    }

    public boolean isOkBOA() {
        return this.isOkBOA;
    }

    public void setOkBOA(boolean isOkBOA) {
        this.isOkBOA = isOkBOA;
    }

    public Ticket getTicket() {
        return this.ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public PortfolioStatus getPortfolioStatus() {
        return this.portfolioStatus;
    }

    public void setPortfolioStatus(PortfolioStatus portfolioStatus) {
        this.portfolioStatus = portfolioStatus;
    }

    public DomainStatus getDomainStatus() {
        return this.domain.getStatus();
    }

    public Domain getDomain() {
        return this.domain;
    }

    public void setDomain(Domain domain) {
        this.domain = domain;
    }

    public Date getDomainCreateDate() {
        Date vDate = null;
        try {
            vDate = this.domain.getCreateDate();
        } catch (ServiceException e) {
        }
        return vDate;
    }
}
