/*
 * $Id: //depot/main/java/afnic-commons-utils/src/fr/afnic/utils/DateUtils.java#5 $ $Revision: #5 $ $Author: ginguene $
 */

package fr.afnic.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.XMLGregorianCalendar;

import org.joda.time.DateTime;

import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;

/**
 * Permet d'accéder facilement à des date souvent utilisée tel que 'hier', 'demain', le mois dernier, ...
 * 
 * 
 * TODO remplacer par joda-time
 * 
 * @author ginguene
 * 
 */
public final class DateUtils {

    /** Format dd/MM/yyyy */
    protected final SimpleDateFormat dayFormat = new SimpleDateFormat("dd/MM/yyyy");
    protected final SimpleDateFormat secondFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    protected final SimpleDateFormat minuteFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    /** Pour suivre les recommandations findbug, DAY_FORMAT et DOCUMENT_DATE_FORMAT ne sont pas static */
    public static final DateUtils INSTANCE = new DateUtils();

    /**
     * Constructeur privé pour empecher l'implémentation de la classe
     */
    private DateUtils() {

    }

    public static boolean isToday(Date date) {
        if (date == null) {
            return false;
        }
        return org.apache.commons.lang.time.DateUtils.isSameDay(getToday(), date);
    }

    /**
     * Retourne la date au format dd/MM/yyyy. Si le parametre est null, retourne une chaine vide.
     * 
     * @param date
     *            Date à passer au format texte.
     * @return date au format "dd/MM/yyyy" ou ""
     */
    public static String toDayFormat(final Date date) {
        if (date == null) {
            return "";
        } else {
            return DateUtils.INSTANCE.dayFormat.format(date);
        }
    }

    /**
     * Retourne un objet <code>Date</code> à partir d'une chaîne de caractères.
     * 
     * @param datetoParse
     *            la chaine à parcourir.
     * @return un object Date
     * @throws ParseException
     *             si un problème est rencontré lors du parse.
     */
    public static Date parseDayFormat(final String datetoParse) throws ParseException {
        return DateUtils.INSTANCE.dayFormat.parse(datetoParse);
    }

    /**
     * Retourne la date d'hier à la miliseconde près
     * 
     * @return La date d'hier à la miliseconde près.
     */
    public static Date getYesterdayFullDate() {
        final GregorianCalendar calendar = new GregorianCalendar();
        calendar.add(Calendar.DATE, -1);
        return calendar.getTime();
    }

    /**
     * Retourne la date d'il y a nbDays jour à la miliseconde près.
     * 
     * @param nbDasy
     *            Nombre de jour dans le passé dont on souhaite la date.
     * 
     * 
     * @return La date d'il y a nbDays jours à la miliseconde près
     */
    public static Date getNbDaysAgo(final int nbDays) {
        final GregorianCalendar calendar = new GregorianCalendar();
        calendar.add(Calendar.DATE, -1 * nbDays);
        return calendar.getTime();
    }

    /**
     * Prend la date passée en parametre et lui ajoute nbDays jours
     * 
     * @param nbDays
     *            Nombre de jour dont on souhaite incrémenter la date.
     * 
     * @param date
     *            Date que l'on souhaitre incrémenter
     * 
     * 
     * @return La date incrémentée de nbDay jours à la miliseconde près
     */
    public static Date getNbDaysLaterFromDate(final int nbDays, final Date date) {
        final GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, nbDays);
        return calendar.getTime();
    }

    /**
     * Prend la date actuelle et lui ajoute nbMinutes minutes.
     * 
     * @param nbDay
     *            Nombre de jour dont on souhaite incrémenter la date.
     * 
     * 
     * 
     * 
     * @return La date incrémentée de nbMinutes minutes à la miliseconde près
     */
    public static Date getNbMinutesLaterFromNow(final int nbMinutes) {
        return DateUtils.getNbMinutesLaterFromDate(nbMinutes, new Date());
    }

    /**
     * Prend la date passée en parametre et lui ajoute nbMinutes minutes.
     * 
     * @param nbMinutes
     *            Nombre de jour dont on souhaite incrémenter la date.
     * 
     * @param date
     *            Date que l'on souhaitre incrémenter
     * 
     * 
     * @return La date incrémentée de nbMinutes minutes à la miliseconde près
     */
    public static Date getNbMinutesLaterFromDate(final int nbMinutes, final Date date) {
        final GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, nbMinutes);
        return calendar.getTime();
    }

    /**
     * Retourne la date d'hier à minuit
     * 
     * @return La date de demain à 00:00
     */
    public static Date getYesterday() {
        return new DateTime().minusDays(1).toDate();
    }

    /**
     * Retourne aujourd'hui à minuit.
     * 
     * @return La date d'aujourd'hui à 00:00
     */
    public static Date getToday() {
        return DateUtils.getTodayAt(0, 0);
    }

    /**
     * Retourne la date d'aujourd'hui l'heure précisee.<br/>
     * Les secondes et miliseconde sont à 0
     * 
     * @return La date d'aujourd'hui l'heure précisée
     */
    public static Date getTodayAt(final Time time) {
        return DateUtils.getTodayAt(time.getHour(), time.getMinute());
    }

    /**
     * Retourne la date d'aujourd'hui l'heure précisee.<br/>
     * Les secondes et miliseconde sont à 0
     * 
     * @return La date d'aujourd'hui l'heure précisée
     */
    public static Date getTodayAt(final int hours, final int minutes) {
        final Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.HOUR_OF_DAY, hours);
        calendar.set(Calendar.MINUTE, minutes);

        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }

    /**
     * 
     * Retourne la date d'il y a 15 jours à la miliseconde près.
     * 
     * @return La date d'il y a 15 jours à la miliseconde près.
     */
    public static Date getFifteenDaysAgo() {
        final GregorianCalendar calendar = new GregorianCalendar();
        calendar.add(Calendar.DATE, -15);
        return calendar.getTime();
    }

    /**
     * Retourne la date du premier jour du mois dernier à minuit.
     * 
     * @return La date du 1er jour du mois dernier à 00:00
     */
    public static Date getLastMonthFirstDay() {
        final GregorianCalendar calendar = new GregorianCalendar();

        calendar.add(Calendar.MONTH, -1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        return calendar.getTime();
    }

    /**
     * Retourne la date du dernier jour du mois dernier à minuit.
     * 
     * @return La date du dernier jour du mois dernier à 00:00
     */
    public static Date getLastMonthLastDay() {
        // 1er jour du mois en cours - 1 jour
        final GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(DateUtils.getCurrentMonthFirstDay());
        calendar.add(Calendar.DATE, -1);

        return calendar.getTime();
    }

    /**
     * Retourne la date du premier jour du mois en cours dernier à minuit.
     * 
     * @return La date du 1er jour du mois en cours à 00:00
     */
    public static Date getCurrentMonthFirstDay() {
        final GregorianCalendar calendar = new GregorianCalendar();
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        return calendar.getTime();
    }

    /**
     * Retourne la date du dernier jour du mois en cours dernier à minuit.
     * 
     * @return La date du dernier jour du mois en cours à 00:00
     */
    public static Date getCurrentMonthLastDay() {
        // 1er jour du mois prochain - 1 jour
        final GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(DateUtils.getNextMonthFirstDay());

        calendar.add(Calendar.DATE, -1);

        return calendar.getTime();
    }

    /**
     * Retourne la date du premier jour du mois prochain à minuit.
     * 
     * @return La date du 1er jour du mois prochain à 00:00
     */
    public static Date getNextMonthFirstDay() {
        final GregorianCalendar calendar = new GregorianCalendar();

        // 1er jour du mois - 1 jour
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH + 1), 1);

        return calendar.getTime();
    }

    /**
     * Clone une date si le parametre est non null.<br/>
     * si la date passée en parametre est null, retourne null.
     * 
     * 
     * @param date
     *            Date que l'on souhaite cloner.
     * 
     * @return Clone de la date ou null
     */
    public static Date clone(final Date date) {
        if (date != null) {
            return (Date) date.clone();
        } else {
            return null;
        }
    }

    /**
     * Converti une java.utils.date en XMLGregorianCalendar<br/>
     * Conversion utilisée principalement pour le passage en stub soap qui utilise ce format de date.
     * 
     * @param date
     *            Date à convertir
     * @return Un XMLGregorianCalendar correspondant à la date
     * @throws DatatypeConfigurationException
     *             Si l'opération de convertion échoue
     */
    public static XMLGregorianCalendar getXmlGregorianCalendar(final Date date) throws DatatypeConfigurationException {
        if (date == null) {
            return null;
        }

        final GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        final int hour = calendar.get(Calendar.HOUR);
        final int minute = calendar.get(Calendar.MINUTE);
        final int second = calendar.get(Calendar.SECOND);

        return XMLGregorianCalendarImpl.createDateTime(year, month + 1, day, hour, minute, second);
    }

    /**
     * Converti une date au format XMLGregorianCalendar en java.utils.Date.<br/>
     * Si le parametre est null, la fonction retourne null.
     * 
     * 
     * @param xmlGregorianCalendar
     *            Date représentée par un XMLGregorianCalendar
     * @return date correspondant au parametre
     */
    public static Date getDate(final XMLGregorianCalendar xmlGregorianCalendar) {
        if (xmlGregorianCalendar != null) {
            return xmlGregorianCalendar.toGregorianCalendar().getTime();
        } else {
            return null;
        }

    }

    /**
     * 
     * @param date
     *            Date à formatter
     * 
     * @return Date au format texte complet.
     */
    public static String toSecondFormat(final Date date) {
        if (date == null) {
            return "";
        } else {
            return DateUtils.INSTANCE.secondFormat.format(date);
        }
    }

    public static Date parseSecondFormat(final String datetoParse) throws ParseException {
        return DateUtils.INSTANCE.secondFormat.parse(datetoParse);
    }

    public static String toMinuteFormat(final Date date) {
        if (date == null) {
            return "";
        } else {
            return DateUtils.INSTANCE.minuteFormat.format(date);
        }
    }

    public static Date parseMinuteFormat(final String datetoParse) throws ParseException {
        return DateUtils.INSTANCE.minuteFormat.parse(datetoParse);
    }
}
