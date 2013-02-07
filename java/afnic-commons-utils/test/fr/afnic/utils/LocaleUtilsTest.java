/*
 * $Id: $
 * $Revision: $
 */

package fr.afnic.utils;

import java.util.Locale;

import junit.framework.Assert;

import org.junit.Test;

public class LocaleUtilsTest {

    @Test
    public void testGetLocale() {
        Assert.assertEquals(Locale.ENGLISH, LocaleUtils.getLocale("EN"));
        Assert.assertEquals(Locale.FRENCH, LocaleUtils.getLocale("FR"));

    }
}
