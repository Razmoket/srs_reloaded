/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/beans/statistics/StatisticsFactory.java#4 $
 * $Revision: #4 $
 * $Author: ginguene $
 */

package fr.afnic.commons.beans.statistics;

/**
 * Permet de créer toutes les statitiques.
 * 
 */
public final class StatisticsFactory {

    private static final IntegerFormatter INTEGER_FORMATTER = new IntegerFormatter();
    private static final PercentFormatter PERCENT_FORMATTER = new PercentFormatter();

    private static final Project BOA = new Project("Boa");

    /** Constrructeur privé pour empecher l'instanciation */
    private StatisticsFactory() {

    }

    public static final Statistic AUTHORIZATION_INVALIDATED = new Statistic(BOA, "AuthorizationInvalidated", INTEGER_FORMATTER);
    public static final Statistic AUTHORIZATION_GENERATED = new Statistic(BOA, "AuthorizationGenerated", INTEGER_FORMATTER);
    public static final Statistic AUTHORIZATION_SEND_MAIL = new Statistic(BOA, "AuthorizationSendMail", INTEGER_FORMATTER);
    public static final Statistic AUTHORIZATION_REJECT = new Statistic(BOA, "AuthorizationReject", INTEGER_FORMATTER);
    public static final Statistic AUTHORIZATION_ABORD = new Statistic(BOA, "AuthorizationAbord", INTEGER_FORMATTER);
    public static final Statistic IDENTIFICATION_BLOCKED_PORFOLIO = new Statistic(BOA, "IdentificationBlockedPortfolio", INTEGER_FORMATTER);
    public static final Statistic IDENTIFICATION_SUPPRESSED_PORFOLIO = new Statistic(BOA, "IdentificationSuppressedPortfolio", INTEGER_FORMATTER);
    public static final Statistic IDENTIFICATION_PROBLEM = new Statistic(BOA, "IdentificationProblem", INTEGER_FORMATTER);
    public static final Statistic IDENTIFICATION_OK = new Statistic(BOA, "IdentificationOk", INTEGER_FORMATTER);
    public static final Statistic IDENTIFICATION_DATE_REMINDER_CREATE = new Statistic(BOA, "IdentificationDateReminderCreate", INTEGER_FORMATTER);
    public static final Statistic TRADE_SEND_MAIL = new Statistic(BOA, "TradeSendMail", INTEGER_FORMATTER);
    public static final Statistic TRADE_APPROVE = new Statistic(BOA, "TradeApprove", INTEGER_FORMATTER);
    public static final Statistic TRADE_CANCEL = new Statistic(BOA, "TradeCancel", INTEGER_FORMATTER);
    public static final Statistic MONTHLY_DOCUMENT_TREATMENT_PCT = new Statistic(BOA, "MonthlyDocumentTreatmentPct", PERCENT_FORMATTER);
    public static final Statistic NB_PENDING_CONTACT_WHEN_STARTING_DAY = new Statistic(BOA, "NbPendingContactWhenStartingDay", INTEGER_FORMATTER);
    public static final Statistic NB_DELAYED_CONTACT_WHEN_STARTING_DAY = new Statistic(BOA, "NbDelayedContactWhenStartingDay", INTEGER_FORMATTER);

    /**
     * Regarde si les parametres correspondent à une statistique connues.<br/>
     * Si c'est le cas, on la retourne. Cela permet de récupérer le format associé.<br/>
     * Cette méthode pourrait devenir inutile si l'on stockait le format d'affichage dans la base de données. *
     * 
     * 
     * 
     * @param project
     *            Projet de la statistique
     * 
     * @param label
     *            Label de la statistique.
     * @return La statistique correspondant aux parametres.
     */
    public static Statistic createStatistic(Project project, String label) {
        Statistic statistic = new Statistic(project, label);
        if (!BOA.equals(project)) {
            return statistic;
        }

        if (AUTHORIZATION_GENERATED.equals(statistic)) {
            return AUTHORIZATION_GENERATED;
        }
        if (AUTHORIZATION_SEND_MAIL.equals(statistic)) {
            return AUTHORIZATION_SEND_MAIL;
        }
        if (AUTHORIZATION_REJECT.equals(statistic)) {
            return AUTHORIZATION_REJECT;
        }
        if (AUTHORIZATION_ABORD.equals(statistic)) {
            return AUTHORIZATION_ABORD;
        }
        if (IDENTIFICATION_BLOCKED_PORFOLIO.equals(statistic)) {
            return IDENTIFICATION_BLOCKED_PORFOLIO;
        }
        if (IDENTIFICATION_SUPPRESSED_PORFOLIO.equals(statistic)) {
            return IDENTIFICATION_SUPPRESSED_PORFOLIO;
        }
        if (IDENTIFICATION_PROBLEM.equals(statistic)) {
            return IDENTIFICATION_PROBLEM;
        }
        if (IDENTIFICATION_OK.equals(statistic)) {
            return IDENTIFICATION_OK;
        }
        if (IDENTIFICATION_DATE_REMINDER_CREATE.equals(statistic)) {
            return IDENTIFICATION_DATE_REMINDER_CREATE;
        }
        if (TRADE_SEND_MAIL.equals(statistic)) {
            return TRADE_SEND_MAIL;
        }
        if (TRADE_APPROVE.equals(statistic)) {
            return TRADE_APPROVE;
        }
        if (TRADE_CANCEL.equals(statistic)) {
            return TRADE_CANCEL;
        }
        if (MONTHLY_DOCUMENT_TREATMENT_PCT.equals(statistic)) {
            return MONTHLY_DOCUMENT_TREATMENT_PCT;
        }
        if (NB_PENDING_CONTACT_WHEN_STARTING_DAY.equals(statistic)) {
            return NB_PENDING_CONTACT_WHEN_STARTING_DAY;
        }
        if (NB_DELAYED_CONTACT_WHEN_STARTING_DAY.equals(statistic)) {
            return NB_DELAYED_CONTACT_WHEN_STARTING_DAY;
        }
        return statistic;
    }
}
