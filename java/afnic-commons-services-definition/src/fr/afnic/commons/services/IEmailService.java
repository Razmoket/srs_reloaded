/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/services/IEmailService.java#8 $
 * $Revision: #8 $
 * $Author: barriere $
 */

package fr.afnic.commons.services;

import java.util.List;

import fr.afnic.commons.beans.mail.Email;
import fr.afnic.commons.beans.mail.EmailBox;
import fr.afnic.commons.beans.mail.SentEmail;
import fr.afnic.commons.beans.mail.reception.IEmailBox;
import fr.afnic.commons.beans.mail.template.EmailTemplate;
import fr.afnic.commons.beans.mail.template.EmailTemplateParameter;
import fr.afnic.commons.beans.mail.template.EmailTemplateType;
import fr.afnic.commons.beans.operations.OperationType;
import fr.afnic.commons.beans.operations.qualification.operation.email.ParameterizedEmailTemplate;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

/**
 * Data Access Object permettant de récupérer et d'envoyer des mails
 * 
 * @author ginguene
 * 
 */
public interface IEmailService {

    /**
     * Envoi un Email.
     */
    public SentEmail sendEmail(Email email, UserId userId, TldServiceFacade tld) throws ServiceException;

    public IEmailBox getEmailBox(EmailBox box, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Récupère tous les templates de mails liés à un type.
     */
    public List<EmailTemplate> getTemplates(EmailTemplateType type, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Récupère tous le templates de mails liés à un type d'opération.
     */
    public <OBJECT extends Object> ParameterizedEmailTemplate<OBJECT> getTemplate(OperationType type, Class<OBJECT> parameterClass, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Récupère tous les templates de mails liés à un type.
     */
    public SentEmail sendTemplateMail(int templateId, List<EmailTemplateParameter> parameters, String from, List<String> to, List<String> cc, List<String> bcc, UserId userId, TldServiceFacade tld)
                                                                                                                                                                                                    throws ServiceException;

}
