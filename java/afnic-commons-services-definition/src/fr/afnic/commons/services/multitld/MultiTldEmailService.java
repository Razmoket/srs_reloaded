package fr.afnic.commons.services.multitld;

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
import fr.afnic.commons.services.IEmailService;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class MultiTldEmailService implements IEmailService {

    protected MultiTldEmailService() {
        super();
    }

    @Override
    public SentEmail sendEmail(Email email, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getEmailService().sendEmail(email, userId, tld);
    }

    @Override
    public IEmailBox getEmailBox(EmailBox box, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getEmailService().getEmailBox(box, userId, tld);
    }

    @Override
    public List<EmailTemplate> getTemplates(EmailTemplateType type, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getEmailService().getTemplates(type, userId, tld);
    }

    @Override
    public <OBJECT> ParameterizedEmailTemplate<OBJECT> getTemplate(OperationType type, Class<OBJECT> parameterClass, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getEmailService().getTemplate(type, parameterClass, userId, tld);
    }

    @Override
    public SentEmail sendTemplateMail(int templateId, List<EmailTemplateParameter> parameters, final String from, List<String> to, List<String> cc, List<String> bcc, UserId userId,
                                      TldServiceFacade tld)
                                                           throws ServiceException {
        return tld.getServiceProvider().getEmailService().sendTemplateMail(templateId, parameters, from, to, cc, bcc, userId, tld);
    }

}
