/*
 * $Id: $ $Revision: $ $Author: $
 */

package fr.afnic.utils;

import junit.framework.Assert;

import org.junit.Test;

public class HashMapListTest {

    @Test
    public void test() {

        final HashMapList<String, Integer> map = HashMapList.create();
        map.add("impair", 1);
        map.add("impair", 3);
        map.add("impair", 5);

        map.add("pair", 2);
        map.add("pair", 4);

        Assert.assertEquals(3, map.get("impair").size());

        Assert.assertEquals(Integer.valueOf(1), map.get("impair").get(0));
        Assert.assertEquals(Integer.valueOf(3), map.get("impair").get(1));
        Assert.assertEquals(Integer.valueOf(5), map.get("impair").get(2));

        Assert.assertEquals(2, map.get("pair").size());
        Assert.assertEquals(Integer.valueOf(2), map.get("pair").get(0));
        Assert.assertEquals(Integer.valueOf(4), map.get("pair").get(1));

    }
}
