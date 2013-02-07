/*
 * $Id: $
 * $Revision: $
 */

package fr.afnic.utils;

import java.util.Locale;

import org.apache.commons.lang.StringUtils;

/**
 * La classe Locale de java est quelque peu mal conçue, cette classe en simplifie et en eclaircide l'utilisation.<br/>
 * 
 * 
 * 
 * 
 * 
 * 
 */
public final class LocaleUtils {

    private LocaleUtils() {

    }

    public static Locale getDefaultLanguageLocale() {
        return LocaleUtils.getLanguageLocale(Locale.getDefault());
    }

    public static Locale getLanguageLocale(Locale locale) {
        return LocaleUtils.getLocale(Locale.getDefault().getLanguage());
    }

    public static Locale getLocale(String language) {
        for (Locale locale : Locale.getAvailableLocales()) {
            if (LocaleUtils.isLocaleMatching(locale, language)) {
                return locale;
            }
        }
        return Locale.getDefault();
    }

    private static boolean isLocaleMatching(Locale locale, String language) {
        return StringUtils.startsWithIgnoreCase(locale.getISO3Language(), language)
                && LocaleUtils.isLanguageLocale(locale);

    }

    public static boolean isLanguageLocale(Locale locale) {
        return StringUtils.isEmpty(locale.getCountry());
    }

    public static boolean isCountryLocale(Locale locale) {
        return StringUtils.isNotEmpty(locale.getCountry());
    }

}
