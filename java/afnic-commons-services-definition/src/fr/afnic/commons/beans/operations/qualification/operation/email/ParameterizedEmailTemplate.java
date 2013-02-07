package fr.afnic.commons.beans.operations.qualification.operation.email;

import java.util.List;

import fr.afnic.commons.beans.contactdetails.EmailAddress;
import fr.afnic.commons.beans.mail.Email;
import fr.afnic.commons.beans.mail.SentEmail;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;

public abstract class ParameterizedEmailTemplate<OBJECT extends Object> extends Email {

    public Class<OBJECT> getParameterClass() {
        return this.parameterClass;
    }

    public OBJECT getParameter() {
        return this.parameter;
    }

    /**Objet qui permet de remplir le template*/
    protected OBJECT parameter;

    protected final Class<OBJECT> parameterClass;

    /** le user appelant l'objet */
    protected final UserId userIdCaller;
    /** le tld sur lequel on appelle l'objet */
    protected final TldServiceFacade tldCaller;

    public ParameterizedEmailTemplate(Class<OBJECT> parameterClass, UserId userId, TldServiceFacade tld) {
        this.parameterClass = parameterClass;
        this.userIdCaller = userId;
        this.tldCaller = tld;
    }

    public abstract String buildContent() throws ServiceException;

    public abstract String buildSubject() throws ServiceException;

    public abstract EmailAddress buildFromEmailAddress() throws ServiceException;

    public abstract List<EmailAddress> buildToEmailAddress() throws ServiceException;

    public abstract List<EmailAddress> buildCcEmailAddress() throws ServiceException;

    public abstract List<EmailAddress> buildBccEmailAddress() throws ServiceException;

    public SentEmail send(OBJECT parameter) throws ServiceException {
        this.parameter = parameter;
        this.setSubject(this.buildSubject());
        this.setContent(this.buildContent());
        this.setFromEmailAddress(this.buildFromEmailAddress());
        this.setToEmailAddresses(this.buildToEmailAddress());
        this.setBccEmailAddresses(this.buildBccEmailAddress());
        this.setCcEmailAddresses(this.buildCcEmailAddress());
        return AppServiceFacade.getEmailService().sendEmail(this, this.userIdCaller, this.tldCaller);
    }

}
