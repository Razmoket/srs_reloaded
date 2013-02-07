package fr.afnic.commons.beans.cache;

import java.util.HashMap;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.commons.test.generator.UserGenerator;

public class IdentifiedObjectCacheTest {

    private IdentifiedObjectCache<Integer, Personn> cache = null;

    private static final HashMap<Integer, Personn> storage = new HashMap<Integer, Personn>();
    private static final Personn DEFAULT_PERSONN = new Personn(15, "oui oui");
    private static final Personn OTHER_PERSONN = new Personn(23, "non non");

    @Before
    public void init() {
        storage.clear();
        storage.put(DEFAULT_PERSONN.getId(), DEFAULT_PERSONN);
        storage.put(OTHER_PERSONN.getId(), OTHER_PERSONN);

        this.cache = new IdentifiedObjectCache<Integer, Personn>() {

            @Override
            protected Integer getIdFromValue() {
                return this.value.getId();
            }

            @Override
            protected Personn getValueFromId(UserId userId, TldServiceFacade tld) throws ServiceException {
                return storage.get(this.getId());
            }
        };

    }

    @Test
    public void getOnEmptyCache() throws ServiceException {
        TestCase.assertNull(this.cache.getId());
        TestCase.assertNull(this.cache.getValue(UserGenerator.getRootUserId(), TldServiceFacade.Fr));
        TestCase.assertFalse(this.cache.isSet());
    }

    @Test
    public void setId() throws ServiceException {

        TestCase.assertNull(this.cache.getId());

        this.cache.setId(DEFAULT_PERSONN.getId());

        TestCase.assertEquals(DEFAULT_PERSONN.getId(), this.cache.getId().intValue());

        //Vérifie que le cache va bien récupérer un objet à partir de l'id
        TestCase.assertEquals(DEFAULT_PERSONN, this.cache.getValue(UserGenerator.getRootUserId(), TldServiceFacade.Fr));

    }

    @Test
    public void setValue() throws ServiceException {
        TestCase.assertNull(this.cache.getId());

        this.cache.setValue(DEFAULT_PERSONN);
        TestCase.assertEquals(DEFAULT_PERSONN, this.cache.getValue(UserGenerator.getRootUserId(), TldServiceFacade.Fr));
        TestCase.assertEquals(DEFAULT_PERSONN.getId(), this.cache.getId().intValue());
    }

    @Test
    public void setValueOnAlreadySettedId() throws ServiceException {

        TestCase.assertNull(this.cache.getId());

        this.cache.setId(DEFAULT_PERSONN.getId());
        this.cache.setValue(OTHER_PERSONN);

        TestCase.assertEquals(OTHER_PERSONN.getId(), this.cache.getId().intValue());
        TestCase.assertEquals(OTHER_PERSONN, this.cache.getValue(UserGenerator.getRootUserId(), TldServiceFacade.Fr));

    }

    @Test
    public void setIdOnAlreadySettedValue() throws ServiceException {

        TestCase.assertNull(this.cache.getId());

        this.cache.setValue(DEFAULT_PERSONN);
        this.cache.setId(OTHER_PERSONN.getId());

        TestCase.assertEquals(OTHER_PERSONN.getId(), this.cache.getId().intValue());
        TestCase.assertEquals(OTHER_PERSONN, this.cache.getValue(UserGenerator.getRootUserId(), TldServiceFacade.Fr));

    }

    @Test
    public void isSetWithId() throws ServiceException {
        this.cache.setId(OTHER_PERSONN.getId());
        TestCase.assertTrue(this.cache.isSet());
    }

    @Test
    public void isSetWithValue() throws ServiceException {
        this.cache.setValue(OTHER_PERSONN);
        TestCase.assertTrue(this.cache.isSet());
    }

    @Test
    public void isSetWithValueAfterSetNull() throws ServiceException {
        this.cache.setValue(OTHER_PERSONN);
        this.cache.setValue(null);
        TestCase.assertFalse(this.cache.isSet());
    }

    @Test
    public void isSetWithIdAfterSetNull() throws ServiceException {
        this.cache.setId(OTHER_PERSONN.getId());
        this.cache.setId(null);
        TestCase.assertFalse(this.cache.isSet());
    }

}
