/*
 * $Id: $
 * $Revision: $
 */

package fr.afnic.commons.services.sql.describer;

import java.util.Locale;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

import fr.afnic.commons.beans.contactdetails.Country;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.facade.MockAppServiceFacade;
import fr.afnic.commons.services.sql.SqlDictionnaryService;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.commons.test.generator.UserGenerator;
import fr.afnic.utils.sql.pool.PoolSqlConnectionFactory;

public class SqlCountryDescriberTest {

    @Before
    public void init() throws Exception {
        XmlBeanFactory beanFactory = new XmlBeanFactory(new ClassPathResource("sql.xml"));
        PoolSqlConnectionFactory sqlNicopeConnectionFactory = beanFactory.getBean("sqlNicopeConnectionFactory", PoolSqlConnectionFactory.class);

        MockAppServiceFacade serviceFacade = new MockAppServiceFacade();
        serviceFacade.setDictionaryService(new SqlDictionnaryService(sqlNicopeConnectionFactory));
        serviceFacade.use();
    }

    @Test
    public void testGetDescription() throws Exception {
        Country england = new Country(2, "GB", UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        Assert.assertEquals("Bad country description", "ROYAUME UNI", england.getDescription(Locale.FRENCH, UserGenerator.getRootUserId(), TldServiceFacade.Fr));
        Assert.assertEquals("Bad country description", "ROYAUME UNI", england.getDescription(Locale.FRANCE, UserGenerator.getRootUserId(), TldServiceFacade.Fr));
        Assert.assertEquals("Bad country description", "UNITED KINGDOM", england.getDescription(Locale.ENGLISH, UserGenerator.getRootUserId(), TldServiceFacade.Fr));

        // Pour les langues non reconnues on utilise l'anglais
        Assert.assertEquals("Bad country description", "UNITED KINGDOM", england.getDescription(Locale.GERMAN, UserGenerator.getRootUserId(), TldServiceFacade.Fr));

        Assert.assertEquals("Bad country description", england.getDescription(Locale.getDefault(), UserGenerator.getRootUserId(), TldServiceFacade.Fr),
                            england.getDescription(UserGenerator.getRootUserId(), TldServiceFacade.Fr));
    }

    @Test
    public void testGetCountries() throws Exception {
        Set<Country> countries = AppServiceFacade.getDictionaryService().getCountries(UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        Assert.assertEquals("Bad countries count", 232, countries.size());

        for (Country country : countries) {
            Assert.assertNotNull("county DictionaryKey cannot be null", country.getDictionaryKey());
            Assert.assertNotNull("county DictionaryKey cannot be null", country.getId());
        }
    }

}
