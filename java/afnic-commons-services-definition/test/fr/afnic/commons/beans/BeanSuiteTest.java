package fr.afnic.commons.beans;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import fr.afnic.commons.beans.request.DnsServerTest;
import fr.afnic.commons.beans.request.DomainTest;
import fr.afnic.commons.checkers.FirstLevelDomainNameCheckerTest;

@RunWith(Suite.class)
@SuiteClasses(value = {
                       DomainTest.class,
                       FirstLevelDomainNameCheckerTest.class,
                       VersionTest.class,
                       WhoisContactTest.class,
                       AuthorizationRequestTest.class,
                       TradeRequestTest.class,
                       AuthorizationTest.class,
                       TicketTest.class,
                       DnsServerTest.class

})
public class BeanSuiteTest {

}
