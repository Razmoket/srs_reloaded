/*
 * $Id: $
 * $Revision: $
 */

package fr.afnic.commons.services.sql.describer;

import java.util.Locale;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

import fr.afnic.commons.beans.contactdetails.Country;
import fr.afnic.commons.beans.contactdetails.Region;
import fr.afnic.commons.services.facade.MockAppServiceFacade;
import fr.afnic.commons.services.sql.SqlDictionnaryService;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.commons.test.generator.UserGenerator;
import fr.afnic.utils.sql.pool.PoolSqlConnectionFactory;

public class SqlRegionDescriberTest {

    @Before
    public void init() throws Exception {
        XmlBeanFactory beanFactory = new XmlBeanFactory(new ClassPathResource("sql.xml"));
        PoolSqlConnectionFactory sqlNicopeConnectionFactory = beanFactory.getBean("sqlNicopeConnectionFactory", PoolSqlConnectionFactory.class);

        MockAppServiceFacade serviceFacade = new MockAppServiceFacade();
        serviceFacade.setDictionaryService(new SqlDictionnaryService(sqlNicopeConnectionFactory));
        serviceFacade.use();
    }

    @Test
    public void testGetRegionsCount() throws Exception {
        Country france = new Country(1, "FR", UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        Assert.assertEquals("Bad region count", 34, france.getRegionsCount());

    }

    @Test
    public void testGetDescription() throws Exception {
        Country france = new Country(1, "FR", UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        Region picardie = new Region(9952, france);

        Assert.assertEquals("Bad countries default description", "Pays de la Loire", picardie.getDescription(UserGenerator.getRootUserId(), TldServiceFacade.Fr));
    }

    @Test
    public void testGetRegionWithCountryWithoutRegion() throws Exception {
        Country england = new Country(2, "GB", UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        Assert.assertEquals("Bad region number", 1, england.getRegionsCount());

        // Le All est le premier element retourné
        Region all = england.getRegions().iterator().next();
        Assert.assertEquals("Bad all desc", "Toutes", all.getDescription(UserGenerator.getRootUserId(), TldServiceFacade.Fr));
        Assert.assertEquals("Bad all fr desc", "Toutes", all.getDescription(Locale.FRENCH, UserGenerator.getRootUserId(), TldServiceFacade.Fr));
        Assert.assertEquals("Bad all en desc", "All", all.getDescription(Locale.ENGLISH, UserGenerator.getRootUserId(), TldServiceFacade.Fr));

    }

}
