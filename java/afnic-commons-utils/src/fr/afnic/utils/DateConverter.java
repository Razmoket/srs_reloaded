/*
 * $Id: //depot/main/java/afnic-commons-utils/src/fr/afnic/utils/DateConverter.java#4 $ $Revision: #4 $ $Author: alaphilippe $
 */

package fr.afnic.utils;

/**
 * Permet de convertir un delai en string de time Xjours Xmin Xs X ms
 * 
 * @author ginguene
 * 
 */
public class DateConverter {

    private TimeUnit minimalTimeUnit = TimeUnit.MILLISECONDS;
    private TimeUnit maximalTimeUnit = TimeUnit.DAY;
    private final long delay;

    private long nbDays;
    private long nbHours;
    private long nbMinutes;
    private long nbSeconds;
    private long nbMiliseconds;

    public DateConverter(final long delay) {
        this.delay = delay;
    }

    public DateConverter(final long delay, final TimeUnit minimalTimeUnit, final TimeUnit maximalTimeUnit) {
        this.delay = delay;
        this.minimalTimeUnit = minimalTimeUnit;
        this.maximalTimeUnit = maximalTimeUnit;
    }

    public static String convertDelayInString(final long delay) {
        return DateConverter.convertDelayInString(delay, TimeUnit.MILLISECONDS, TimeUnit.DAY);
    }

    public static String convertDelayInString(final long delay, final TimeUnit minimalTimeUnit, final TimeUnit maximalTimeUnit) {
        final DateConverter converter = new DateConverter(delay, minimalTimeUnit, maximalTimeUnit);
        return converter.convertDelayInString();
    }

    public String convertDelayInString() {

        this.initTimeFields();

        final StringBuffer buffer = new StringBuffer();
        if (this.hasToDisplayDays()) buffer.append(this.nbDays + "jours ");
        if (this.hasToDisplayHours()) buffer.append(this.nbHours + "h ");
        if (this.hasToDisplayMinutes()) buffer.append(this.nbMinutes + "min ");
        if (this.hasToDisplaySeconds()) buffer.append(this.nbSeconds + "s ");
        if (this.hasToDisplayMiliseconds()) buffer.append(this.nbMiliseconds + "ms ");

        return buffer.toString();
    }

    private void initTimeFields() {
        this.nbDays = java.util.concurrent.TimeUnit.MILLISECONDS.toDays(this.delay);
        this.nbHours = java.util.concurrent.TimeUnit.MILLISECONDS.toHours(this.delay) % 24;
        this.nbMinutes = java.util.concurrent.TimeUnit.MILLISECONDS.toMinutes(this.delay) % 60;
        this.nbSeconds = java.util.concurrent.TimeUnit.MILLISECONDS.toSeconds(this.delay) % 60;
        this.nbMiliseconds = java.util.concurrent.TimeUnit.MILLISECONDS.toMillis(this.delay) % 1000;
    }

    private boolean hasToDisplaySeconds() {
        return this.nbSeconds > 0 && this.hasToDisplayUnitTime(TimeUnit.SECONDS);
    }

    private boolean hasToDisplayMiliseconds() {
        return this.nbMiliseconds > 0 && this.hasToDisplayUnitTime(TimeUnit.MILLISECONDS);
    }

    private boolean hasToDisplayMinutes() {
        return this.nbMinutes > 0 && this.hasToDisplayUnitTime(TimeUnit.HOURS);
    }

    private boolean hasToDisplayHours() {
        return this.nbHours > 0 && this.hasToDisplayUnitTime(TimeUnit.HOURS);
    }

    private boolean hasToDisplayDays() {
        return this.nbDays > 0 && this.hasToDisplayUnitTime(TimeUnit.DAY);
    }

    private boolean hasToDisplayUnitTime(final TimeUnit timeUnit) {
        return timeUnit.getDegree() >= this.minimalTimeUnit.getDegree() && timeUnit.getDegree() <= this.maximalTimeUnit.getDegree();
    }

    /**
     * Type d'unité de temps utilisable
     * 
     * @author ginguene
     * 
     */
    public enum TimeUnit {
        MILLISECONDS(0), SECONDS(1), MINUTES(2), HOURS(3), DAY(4);

        int degree = -1;

        TimeUnit(final int degree) {
            this.degree = degree;
        }

        public int getDegree() {
            return this.degree;
        }
    }

}
