package fr.afnic.commons.beans;

import junit.framework.TestCase;

import org.junit.Test;

import fr.afnic.commons.test.BeanTestCase;

public class WhoisContactTest extends TestCase {

    @Test
    public void testBean() throws Exception {
        BeanTestCase.assertSettersAndGetters(WhoisContact.class);
    }

}
