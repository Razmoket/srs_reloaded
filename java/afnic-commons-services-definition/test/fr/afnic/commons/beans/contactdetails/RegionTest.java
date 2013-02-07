/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.beans.contactdetails;

import junit.framework.Assert;

import org.junit.Test;

public class RegionTest {

    private final static int PICARDIE_ID = 9922;
    private final static int BRETAGNE_ID = 9953;

    @Test
    public void testEquals() {

        Country france = Country.FR;

        Assert.assertTrue(new Region(RegionTest.BRETAGNE_ID, france).equals(new Region(RegionTest.BRETAGNE_ID, france)));

        Assert.assertFalse(new Region(RegionTest.BRETAGNE_ID, france).equals(null));
        Assert.assertFalse(new Region(RegionTest.BRETAGNE_ID, france).equals(new Object()));
        Assert.assertFalse(new Region(RegionTest.BRETAGNE_ID, france).equals(new Region(RegionTest.PICARDIE_ID, france)));
        // Assert.assertFalse(new Region(RegionTest.BRETAGNE_ID, france).equals(new Region(RegionTest.BRETAGNE_ID, england)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewWithNullCountry() {
        new Region(RegionTest.BRETAGNE_ID, null);
    }

    @Test
    public void testNewWithNotNullCountry() {
        Country france = Country.FR;
        Region bretagne = new Region(RegionTest.BRETAGNE_ID, france);
        Assert.assertEquals("bad country", france, bretagne.getCountry());
    }

    @Test
    public void testNewWithValue() {
        String value = Integer.toString(RegionTest.BRETAGNE_ID);
        Region bretagne = new Region(value);

        Assert.assertEquals(RegionTest.BRETAGNE_ID, bretagne.getId());
        Assert.assertEquals(value, bretagne.getDictionaryKey());
        Assert.assertEquals(value, bretagne.getValue());
    }

}
