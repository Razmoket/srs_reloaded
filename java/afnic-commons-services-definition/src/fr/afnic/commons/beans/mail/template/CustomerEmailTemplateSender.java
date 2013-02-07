/*
 * $Id: $ $Revision: $ $Author: $
 */

package fr.afnic.commons.beans.mail.template;

import java.util.ArrayList;
import java.util.List;

import fr.afnic.commons.beans.application.env.Environnement;
import fr.afnic.commons.beans.customer.Customer;
import fr.afnic.commons.beans.logs.ILogger;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.utils.ToStringHelper;

/**
 * Classe chargé d'envoyer un mail qui correspond à un template à destination d'un client.
 * 
 * @author ginguene
 * 
 */
public class CustomerEmailTemplateSender extends EmailTemplateSender {

    private static final ILogger LOGGER = AppServiceFacade.getLoggerService().getLogger(CustomerEmailTemplateSender.class);

    private final int templateId;
    private final Customer customer;

    private List<EmailTemplateParameter> parameters;
    private List<String> to;
    private List<String> cc;
    private List<String> bcc;
    private String from;

    public CustomerEmailTemplateSender(int templateId, Customer customer) {
        this.templateId = templateId;
        this.customer = customer;
        this.populateTemplate();
    }

    public void populateTemplate() {
        this.buildTemplateParameters();
        this.buildToDestinaries();
        this.buildCcDestinaries();
        this.buildBccDestinaries();
        this.buildFrom();
    }

    private void buildTemplateParameters() {
        this.parameters = new CustomerTemplateParameterBuilder(this.customer).build();
    }

    private void buildFrom() {
        this.from = null;
        if (AppServiceFacade.getApplicationService().getCurrentVersion().getEnvironnement() == Environnement.Dev) {
            if (this.templateId != 0) {
                if (this.templateId != 6) {
                    this.from = "Secrétariat Général - dev <testing+dev@nic.fr>";
                } else {
                    this.from = "Service Juridique - dev <testing+dev@nic.fr>";
                }
            } else {
                this.from = "Service Client - dev <testing+dev@nic.fr>";
            }
        }
        if (AppServiceFacade.getApplicationService().getCurrentVersion().getEnvironnement() == Environnement.Preprod) {
            if (this.templateId != 0) {
                if (this.templateId != 6) {
                    this.from = "Secrétariat Général - preprod <testing+preprod@nic.fr>";
                } else {
                    this.from = "Service Juridique - preprod<testing+preprod@nic.fr>";
                }
            } else {
                this.from = "Service Client - preprod <testing+preprod@nic.fr>";
            }
        }
        if (AppServiceFacade.getApplicationService().getCurrentVersion().getEnvironnement() == Environnement.Prod) {
            if (this.templateId != 0) {
                if (this.templateId != 6) {
                    this.from = "Secrétariat Général <secretariat-general@afnic.fr>";
                } else {
                    this.from = "Service Juridique <juridique@nic.fr>";
                }
            } else {
                this.from = "Service Client <sc@afnic.fr>";
            }
        }
    }

    private void buildToDestinaries() {
        this.to = new ArrayList<String>();

        /*if (AppServiceFacade.getApplicationService().getCurrentVersion().getEnvironnement() == Environnement.Dev) {
            this.to.add("alaphilippe@afnic.fr");
            this.to.add("ginguene@afnic.fr");
            this.to.add("nicolas.barriere@afnic.fr");
            this.to.add("edouard.chandavoine@afnic.fr");
            return;
        }

        if (AppServiceFacade.getApplicationService().getCurrentVersion().getEnvironnement() == Environnement.Preprod) {
            this.to.add("testing@afnic.fr");
            return;
        }

        for (CustomerContact contact : this.customer.getCustomerContacts()) {
            if (this.templateId != 0) {
                if (CustomerContactRole.Administrative.equals(contact.getContactType())) {
                    this.to.add(contact.getFirstEmailAddress().getValue());
                }
            } else {
                if (CustomerContactRole.Noc.equals(contact.getContactType())) {
                    this.to.add(contact.getFirstEmailAddress().getValue());
                }
            }
        }*/

        throw new RuntimeException("Not implemented");
    }

    private void buildCcDestinaries() {
        this.cc = new ArrayList<String>();

        if (AppServiceFacade.getApplicationService().getCurrentVersion().getEnvironnement() == Environnement.Dev) {
            return;
        }

        if (AppServiceFacade.getApplicationService().getCurrentVersion().getEnvironnement() == Environnement.Preprod) {
            if (this.templateId != 0) {
                if (this.templateId != 6) {
                    this.cc.add("SecrÃ©tariat GÃ©nÃ©ral - preprod <testing+preprod@nic.fr>");
                } else {
                    this.cc.add("Service Juridique - preprod<testing+preprod@nic.fr>");
                }
            } else {
                this.cc.add("Service Client - preprod <testing+preprod@nic.fr>");
            }
        }
        if (AppServiceFacade.getApplicationService().getCurrentVersion().getEnvironnement() == Environnement.Prod) {
            if (this.templateId != 0) {
                if (this.templateId != 6) {
                    this.cc.add("SecrÃ©tariat GÃ©nÃ©ral <secretariat-general@afnic.fr>");
                } else {
                    this.cc.add("Service Juridique <juridique@nic.fr>");
                }
            } else {
                this.cc.add("Service Client <sc@afnic.fr>");
            }
        }

        throw new RuntimeException("Not implemented");
        /*for (CustomerContact contact : this.customer.getCustomerContacts()) {
            if (this.templateId == 3 || this.templateId == 1) {
                if (CustomerContactRole.BILLING.equals(contact.getContactType())) {
                    this.cc.add(contact.getFirstEmailAddress().getValue());
                }
            }

        }*/
    }

    private void buildBccDestinaries() {
        this.bcc = new ArrayList<String>();

        if (AppServiceFacade.getApplicationService().getCurrentVersion().getEnvironnement() == Environnement.Preprod) {
            this.bcc.add("alaphilippe@afnic.fr");
            this.bcc.add("ginguene@afnic.fr");
            this.bcc.add("nicolas.barriere@afnic.fr");
            this.bcc.add("edouard.chandavoine@afnic.fr");
        }
        /*if (AppServiceFacade.getApplicationService().getCurrentVersion().getEnvironnement() == Environnement.Prod) {
            if (this.templateId == 0) {
                this.bcc.add("turbat@afnic.fr");
            }
        }*/
    }

    @Override
    public void send(UserId userId, TldServiceFacade tld) throws ServiceException {
        if (CustomerEmailTemplateSender.LOGGER.isInfoEnabled()) {
            CustomerEmailTemplateSender.LOGGER.info("sending a new mail " + this.toString());
        }

        AppServiceFacade.getEmailService().sendTemplateMail(this.templateId, this.parameters, this.from, this.to, this.cc, this.bcc, userId, tld);
    }

    @Override
    public String toString() {
        return new ToStringHelper().addAllObjectAttributes(this).toString();
    }

}
