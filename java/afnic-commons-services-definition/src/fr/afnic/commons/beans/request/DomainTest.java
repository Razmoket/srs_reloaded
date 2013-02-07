package fr.afnic.commons.beans.request;

import org.junit.Test;

import fr.afnic.commons.beans.domain.Domain;
import fr.afnic.commons.test.BeanTestCase;
import fr.afnic.commons.test.BeanTestCaseNameMethodRule;

public class DomainTest {

    @Test
    public void testBean() throws Exception {
        BeanTestCase.assertSettersAndGetters(Domain.class, new BeanTestCaseNameMethodRule("createDate"));
    }

}
