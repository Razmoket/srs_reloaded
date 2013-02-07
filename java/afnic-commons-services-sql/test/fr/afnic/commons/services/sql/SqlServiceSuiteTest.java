package fr.afnic.commons.services.sql;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses(value = { SqlOperationServiceTest.class,
                       SqlQualificationServiceTest.class,
                       SqlAuthorizationServiceTest.class,
                       SqlWhoisContactServiceTest.class,
                       SqlDomainServiceTest.class,
                       //SqlOperationFormServiceTest.class,
                       SqlTicketServiceTest.class,
                       SqlUserServiceTest.class
})
public class SqlServiceSuiteTest {

}
