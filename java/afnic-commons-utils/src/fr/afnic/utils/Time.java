/*
 * $Id: $ $Revision: $ $Author: $
 */

package fr.afnic.utils;

/**
 * Représente une heure du jour.
 * 
 * @author ginguene
 * 
 */
public class Time {
    private int hour;
    private int minute;

    public int getHour() {
        return this.hour;
    }

    public void setHour(final int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return this.minute;
    }

    public void setMinute(final int minute) {
        this.minute = minute;
    }

}
