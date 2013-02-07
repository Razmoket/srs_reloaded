package fr.afnic.utils;

import junit.framework.Assert;

import org.junit.Test;

public class DoubleMapTest {

    @Test
    public void testOk() {
        final DoubleMap<String, Integer> testDoubleMap = new DoubleMap<String, Integer>();

        testDoubleMap.put("un", 1);

        Assert.assertEquals(1, testDoubleMap.getWithKey("un").intValue());
        Assert.assertEquals("un", testDoubleMap.getWithValue(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithExistingKey() {
        final DoubleMap<String, Integer> testDoubleMap = new DoubleMap<String, Integer>();

        testDoubleMap.put("un", 1);
        testDoubleMap.put("un", 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithExistingValue() {
        final DoubleMap<String, Integer> testDoubleMap = new DoubleMap<String, Integer>();

        testDoubleMap.put("un", 1);
        testDoubleMap.put("deux", 1);
    }

}
