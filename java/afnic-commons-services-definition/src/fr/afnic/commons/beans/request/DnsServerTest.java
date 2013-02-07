package fr.afnic.commons.beans.request;

import org.junit.Test;

import fr.afnic.commons.beans.DnsServer;
import fr.afnic.commons.test.BeanTestCase;
import fr.afnic.commons.test.BeanTestCaseNameMethodRule;

public class DnsServerTest {

    @Test
    public void testBean() throws Exception {
        BeanTestCase.assertSettersAndGetters(DnsServer.class, new BeanTestCaseNameMethodRule(""));
    }

}
