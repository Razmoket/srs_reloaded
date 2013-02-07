/*
 * $Id: $
 * $Revision: $
 */

package fr.afnic.commons.beans.description;

import java.util.Locale;

public class Description {

    private Locale locale;
    private String text;

    public Locale getLocale() {
        return this.locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
