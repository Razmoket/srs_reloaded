package fr.afnic.commons.beans.billing;

import java.util.Date;

public class BillableTicketInfo {

    private String ticketId;

    private Date commandDate;

    private String domainName;
    private String article;
    private String tld;
    private String billedCustomer;
    private String payersCustomer;

    public String getTicketId() {
        return this.ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public Date getCommandDate() {
        return this.commandDate;
    }

    public void setCommandDate(Date commandDate) {
        this.commandDate = commandDate;
    }

    public String getDomainName() {
        return this.domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public String getArticle() {
        return this.article;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public String getTld() {
        return this.tld;
    }

    public void setTld(String tld) {
        this.tld = tld;
    }

    public String getBilledCustomer() {
        return this.billedCustomer;
    }

    public void setBilledCustomer(String billedCustomer) {
        this.billedCustomer = billedCustomer;
    }

    public String getPayersCustomer() {
        return this.payersCustomer;
    }

    public void setPayersCustomer(String payersCustomer) {
        this.payersCustomer = payersCustomer;
    }

}
