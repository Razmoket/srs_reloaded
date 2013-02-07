package fr.afnic.commons.beans.mail;

import java.util.HashMap;
import java.util.Map;

import fr.afnic.commons.beans.operations.OperationType;
import fr.afnic.commons.beans.operations.qualification.operation.email.AutoMailReachabilityToHolder;
import fr.afnic.commons.beans.operations.qualification.operation.email.BlockedDomainPortfolioToHolder;
import fr.afnic.commons.beans.operations.qualification.operation.email.BlockedDomainPortfolioToRegistrar;
import fr.afnic.commons.beans.operations.qualification.operation.email.FinishedFromComplaintToInitiator;
import fr.afnic.commons.beans.operations.qualification.operation.email.FinishedLvl1FailedToRegistrar;
import fr.afnic.commons.beans.operations.qualification.operation.email.FinishedLvl1ToInitiator;
import fr.afnic.commons.beans.operations.qualification.operation.email.FinishedLvl1ToRegistrar;
import fr.afnic.commons.beans.operations.qualification.operation.email.FinishedToHolder;
import fr.afnic.commons.beans.operations.qualification.operation.email.FinishedToInitiator;
import fr.afnic.commons.beans.operations.qualification.operation.email.FinishedToRegistrar;
import fr.afnic.commons.beans.operations.qualification.operation.email.FromComplaintToInitiator;
import fr.afnic.commons.beans.operations.qualification.operation.email.ParameterizedEmailTemplate;
import fr.afnic.commons.beans.operations.qualification.operation.email.ProblemBisToRegistrar;
import fr.afnic.commons.beans.operations.qualification.operation.email.ProblemToHolder;
import fr.afnic.commons.beans.operations.qualification.operation.email.ProblemToInitiator;
import fr.afnic.commons.beans.operations.qualification.operation.email.ProblemToRegistrar;
import fr.afnic.commons.beans.operations.qualification.operation.email.ReminderAutoMailReachabilityToHolder;
import fr.afnic.commons.beans.operations.qualification.operation.email.StartValorizationToInitiator;
import fr.afnic.commons.beans.operations.qualification.operation.email.StartValorizationToRegistrar;
import fr.afnic.commons.beans.operations.qualification.operation.email.SuppressedDomainPortfolioToHolder;
import fr.afnic.commons.beans.operations.qualification.operation.email.SuppressedDomainPortfolioToRegistrar;
import fr.afnic.commons.beans.operations.qualification.operation.email.UnblockedDomainPortfolioToHolder;
import fr.afnic.commons.beans.operations.qualification.operation.email.UnblockedDomainPortfolioToRegistrar;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.tld.TldServiceFacade;

public final class EmailTemplateFactory {

    public static final EmailTemplateFactory INSTANCE = new EmailTemplateFactory(null, null);

    private final Map<OperationType, ParameterizedEmailTemplate<?>> map = new HashMap<OperationType, ParameterizedEmailTemplate<?>>();

    private EmailTemplateFactory(UserId userId, TldServiceFacade tld) {
        //fermeture --> pas de notion niveau 1 et 2 alors que les templates fournit sont différents...
        this.map.put(OperationType.NotifyEmailFinishedToRegistrar, new FinishedToRegistrar(userId, tld)); //contenu
        this.map.put(OperationType.NotifyEmailFinishedToInitiator, new FinishedToInitiator(userId, tld));
        this.map.put(OperationType.NotifyEmailFinishedToHolder, new FinishedToHolder(userId, tld));

        //ouverture niveau 2 et demande de PJ
        this.map.put(OperationType.NotifyEmailProblemToRegistrar, new ProblemToRegistrar(userId, tld)); // ok
        this.map.put(OperationType.NotifyEmailProblemBisToRegistrar, new ProblemBisToRegistrar(userId, tld)); // ok
        this.map.put(OperationType.NotifyEmailProblemToHolder, new ProblemToHolder(userId, tld)); //sujet
        this.map.put(OperationType.NotifyEmailProblemToInitiator, new ProblemToInitiator(userId, tld)); //sujet

        //blocage
        this.map.put(OperationType.NotifyEmailBlockedDomainPortfolioToRegistrar, new BlockedDomainPortfolioToRegistrar(userId, tld)); // ok
        this.map.put(OperationType.NotifyEmailBlockedDomainPortfolioToHolder, new BlockedDomainPortfolioToHolder(userId, tld)); //sujet

        //deblocage
        this.map.put(OperationType.NotifyEmailUnblockedDomainPortfolioToRegistrar, new UnblockedDomainPortfolioToRegistrar(userId, tld)); //ok
        this.map.put(OperationType.NotifyEmailUnblockedDomainPortfolioToHolder, new UnblockedDomainPortfolioToHolder(userId, tld)); //sujet

        //suppression
        this.map.put(OperationType.NotifyEmailSuppressedDomainPortfolioToRegistrar, new SuppressedDomainPortfolioToRegistrar(userId, tld)); // ok
        this.map.put(OperationType.NotifyEmailSuppressedDomainPortfolioToHolder, new SuppressedDomainPortfolioToHolder(userId, tld));//sujet

        //ouverture niveau 1
        this.map.put(OperationType.NotifyEmailStartValorizationToRegistrar, new StartValorizationToRegistrar(userId, tld)); //contenu
        this.map.put(OperationType.NotifyEmailStartValorizationToInitiator, new StartValorizationToInitiator(userId, tld)); //contenu

        //controle auto de la joignabilité
        this.map.put(OperationType.NotifyAutoMailReachabilityToHolder, new AutoMailReachabilityToHolder(userId, tld)); //contenu
        this.map.put(OperationType.NotifyReminderAutoMailReachabilityToHolder, new ReminderAutoMailReachabilityToHolder(userId, tld)); //contenu

        //ouverture d'une qualif en pending freeze sur plainte
        this.map.put(OperationType.NotifyEmailFromComplaintToInitiator, new FromComplaintToInitiator(userId, tld)); //contenu
        //cloture d'une qualif en pending freeze sur plainte
        this.map.put(OperationType.NotifyEmailFinishedFromComplaintToInitiator, new FinishedFromComplaintToInitiator(userId, tld)); //contenu

        this.map.put(OperationType.NotifyEmailFinishedLvl1ToInitiator, new FinishedLvl1ToInitiator(userId, tld));
        this.map.put(OperationType.NotifyEmailFinishedLvl1ToRegistrar, new FinishedLvl1ToRegistrar(userId, tld));
        this.map.put(OperationType.NotifyEmailFinishedLvl1FailedToRegistrar, new FinishedLvl1FailedToRegistrar(userId, tld));

    }

    public static <OBJECT extends Object> ParameterizedEmailTemplate<OBJECT> getTemplate(OperationType type, Class<OBJECT> parameterClass) {
        return INSTANCE.getTemplateImpl(type, parameterClass);
    }

    @SuppressWarnings("unchecked")
    private <OBJECT extends Object> ParameterizedEmailTemplate<OBJECT> getTemplateImpl(OperationType type, Class<OBJECT> parameterClass) {
        if (!this.map.containsKey(type)) {
            throw new IllegalArgumentException("no template for type " + type);
        }

        ParameterizedEmailTemplate<?> template = this.map.get(type);
        if (template.getParameterClass().equals(parameterClass)) {
            return (ParameterizedEmailTemplate<OBJECT>) template;
        } else {
            throw new IllegalArgumentException("Template for type " + type + " use " + template.getParameterClass().getSimpleName() + " class [exceptected:" + parameterClass.getSimpleName() + "]");
        }

    }

}
