/*
 * $Id: MockMailService.java,v 1.3 2010/04/09 07:40:37 ginguene Exp $
 * $Revision: 1.3 $
 * $Author: ginguene $
 */

package fr.afnic.commons.services.mock;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import fr.afnic.commons.beans.mail.Email;
import fr.afnic.commons.beans.mail.EmailBox;
import fr.afnic.commons.beans.mail.EmailTemplateFactory;
import fr.afnic.commons.beans.mail.SentEmail;
import fr.afnic.commons.beans.mail.reception.IEmailBox;
import fr.afnic.commons.beans.mail.template.EmailTemplate;
import fr.afnic.commons.beans.mail.template.EmailTemplateParameter;
import fr.afnic.commons.beans.mail.template.EmailTemplateType;
import fr.afnic.commons.beans.operations.OperationType;
import fr.afnic.commons.beans.operations.qualification.operation.email.ParameterizedEmailTemplate;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.IEmailService;
import fr.afnic.commons.services.contracts.mail.MailServiceParametersMethodChecker;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

/**
 * Implémentation mock du IMailService.
 * 
 * @author ginguene
 * 
 */
public class MockEmailService implements IEmailService {

    /** Clé = id du mail; valeur = mail envoyé */
    private final HashMap<String, SentEmail> map = new HashMap<String, SentEmail>();

    private final HashMap<EmailBox, IEmailBox> emailBoxMap = new HashMap<EmailBox, IEmailBox>();

    /** Vérifie la validité des parametres */
    private final MailServiceParametersMethodChecker checker = new MailServiceParametersMethodChecker();

    @Override
    public SentEmail sendEmail(Email mail, UserId userId, TldServiceFacade tld) throws ServiceException {
        this.checker.checkSendMailParameters(mail, userId);

        String messageId = Long.toString(System.currentTimeMillis());

        // date sans les miliseconde qui ne sont pas conservee dans docushare
        Date date = new Date(1000 * (System.currentTimeMillis() / 1000));

        SentEmail sentmail = new SentEmail(mail, messageId, date);
        this.map.put(messageId, sentmail);
        return sentmail;
    }

    @Override
    public List<EmailTemplate> getTemplates(EmailTemplateType type, UserId userId, TldServiceFacade tld) throws ServiceException {
        List<EmailTemplate> list = new ArrayList<EmailTemplate>();

        for (int i = 0; i < 5; i++) {
            EmailTemplate template = new EmailTemplate();
            template.setName("Template de test " + i);
            template.setIdTemplate(i);
            template.setType(type);
            template.setContent("mon contenu " + i);
            template.setParameters(new ArrayList<EmailTemplateParameter>());
            list.add(template);
        }
        return list;
    }

    @Override
    public SentEmail sendTemplateMail(int templateId, List<EmailTemplateParameter> parameters, String from, List<String> to, List<String> cc, List<String> bcc, UserId userId, TldServiceFacade tld)
                                                                                                                                                                                                    throws ServiceException {
        System.out.println("Send mail " + templateId + ":"
                           + "\n  -parameters: " + parameters.toString()
                           + "\n  -from: " + from
                           + "\n  -to: " + to
                           + "\n  -cc: " + cc
                           + "\n  -bcc: " + bcc);
        return null;
    }

    @Override
    public <OBJECT> ParameterizedEmailTemplate<OBJECT> getTemplate(OperationType type, Class<OBJECT> parameterClass, UserId userId, TldServiceFacade tld) throws ServiceException {
        return EmailTemplateFactory.getTemplate(type, parameterClass);
    }

    @Override
    public IEmailBox getEmailBox(EmailBox box, UserId userId, TldServiceFacade tld) throws ServiceException {
        if (!this.emailBoxMap.containsKey(box)) {
            this.emailBoxMap.put(box, new MockEmailBox(this.map));
        }
        return this.emailBoxMap.get(box);
    }

}
