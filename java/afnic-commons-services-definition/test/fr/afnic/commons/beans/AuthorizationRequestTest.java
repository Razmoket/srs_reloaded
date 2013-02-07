package fr.afnic.commons.beans;

import org.junit.Test;

import fr.afnic.commons.beans.request.AuthorizationRequest;
import fr.afnic.commons.test.BeanTestCase;
import fr.afnic.commons.test.BeanTestCaseNameMethodRule;

public class AuthorizationRequestTest {

    @Test
    public void testBean() throws Exception {
        BeanTestCase.assertSettersAndGetters(AuthorizationRequest.class, new BeanTestCaseNameMethodRule("registrar", "holder", "comment"));
    }

}
